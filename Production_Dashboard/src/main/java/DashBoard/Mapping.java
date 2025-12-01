package DashBoard;

import Dto.OpcEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaSubscription;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringParameters;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class Mapping {

    private OpcUaClient client;
    private volatile OpcEntity cachedData = new OpcEntity();
    
    // WebSocket session management
    private final List<WebSocketSession> webSocketSessions = new CopyOnWriteArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    private static final String ENDPOINT_URL = "opc.tcp://127.0.0.1:49320";

    @PostConstruct
    public void init() {
        try {
            client = OpcUaClient.create(ENDPOINT_URL);
            client.connect().get(5, TimeUnit.SECONDS);
            System.out.println("OPC UA client connected");
            createSubscription();
        } catch (Exception e) {
            System.err.println("Failed to connect to OPC UA server: " + e.getMessage());
            System.err.println("Starting in disconnected mode. Data will be zero/empty.");
        }
    }

    @PreDestroy
    public void shutdown() throws Exception {
        if (client != null) {
            client.disconnect().get();
            System.out.println("OPC UA client disconnected");
        }
    }

    private Integer toInt(Object value) {
        if (value == null) return 0;
        if (value instanceof Integer) return (Integer) value;
        if (value instanceof Short) return ((Short) value).intValue();
        if (value instanceof UInteger) return ((UInteger) value).intValue();
        throw new IllegalArgumentException("Unexpected data type: " + value.getClass());
    }

    public OpcEntity mapnodes() {
        return cachedData;
    }


    public void addWebSocketSession(WebSocketSession session) {
        webSocketSessions.add(session);
        System.out.println("WebSocket session added. Total sessions: " + webSocketSessions.size());
    }

    public void removeWebSocketSession(WebSocketSession session) {
        webSocketSessions.remove(session);
        System.out.println("WebSocket session removed. Total sessions: " + webSocketSessions.size());
    }

    public String getDataAsJson() throws Exception {
        return objectMapper.writeValueAsString(cachedData);
    }

    // BROADCAST OPC UA DATA TO ALL WEBSOCKET CLIENTS
    private void broadcastData() {
        try {
            String json = objectMapper.writeValueAsString(cachedData);
            TextMessage message = new TextMessage(json);
            
            int sentCount = 0;
            for (WebSocketSession session : webSocketSessions) {
                if (session.isOpen()) {
                    session.sendMessage(message);
                    sentCount++;
                }
            }
            if (sentCount > 0) {
                System.out.println("Broadcasted to " + sentCount + " WebSocket sessions");
            }
        } catch (Exception e) {
            System.err.println("Broadcast failed: " + e.getMessage());
        }
    }

    public void createSubscription() throws ExecutionException, InterruptedException {
        UaSubscription subscription = client.getSubscriptionManager()
                .createSubscription(1000.0).get();

        List<NodeId> nodeIds = List.of(
                NodeId.parse("ns=2;s=Channel1.Device1.actual"),
                NodeId.parse("ns=2;s=Channel1.Device1.target"),
                NodeId.parse("ns=2;s=Channel1.Device1.uptime"),
                NodeId.parse("ns=2;s=Channel1.Device1.downtime"),
                NodeId.parse("ns=2;s=Channel1.Device1.takt")
        );

        List<MonitoredItemCreateRequest> requests = new ArrayList<>();
        int clientHandle = 1;

        for (NodeId nodeId : nodeIds) {
            ReadValueId readValueId = new ReadValueId(nodeId, AttributeId.Value.uid(), null, null);
            MonitoringParameters params = new MonitoringParameters(
                    UInteger.valueOf(clientHandle++), 1000.0, null, UInteger.valueOf(10), true);
            requests.add(new MonitoredItemCreateRequest(readValueId, MonitoringMode.Reporting, params));
        }

        UaSubscription.ItemCreationCallback onItemCreated = 
            (monitoredItem, id) -> monitoredItem.setValueConsumer((item, value) -> {
                synchronized (cachedData) {
                    String nodeIdStr = item.getReadValueId().getNodeId().toParseableString();
                    Integer newVal = toInt(value.getValue().getValue());

                    if (nodeIdStr.endsWith("actual")) cachedData.setActual(newVal);
                    else if (nodeIdStr.endsWith("target")) cachedData.setTarget(newVal);
                    else if (nodeIdStr.endsWith("uptime")) cachedData.setUptime(newVal);
                    else if (nodeIdStr.endsWith("downtime")) cachedData.setDowntime(newVal);
                    else if (nodeIdStr.endsWith("takt")) cachedData.setRooltarget(newVal);
                }
                

                broadcastData();
                
                System.out.println("Subscription update: " + item.getReadValueId().getNodeId() + " = " + value.getValue().getValue());
            });

        subscription.createMonitoredItems(TimestampsToReturn.Both, requests, onItemCreated).get();
        refreshCachedData();
    }
    
    private void refreshCachedData() throws ExecutionException, InterruptedException {
        if (client == null) return;
        
        List<NodeId> nodeIds = List.of(
                NodeId.parse("ns=2;s=Channel1.Device1.actual"),
                NodeId.parse("ns=2;s=Channel1.Device1.target"),
                NodeId.parse("ns=2;s=Channel1.Device1.uptime"),
                NodeId.parse("ns=2;s=Channel1.Device1.downtime"),
                NodeId.parse("ns=2;s=Channel1.Device1.takt")
        );

        List<DataValue> values = client.readValues(0.0, TimestampsToReturn.Both, nodeIds).get();

        synchronized (cachedData) {
            cachedData.setActual(toInt(values.get(0).getValue().getValue()));
            cachedData.setTarget(toInt(values.get(1).getValue().getValue()));
            cachedData.setUptime(toInt(values.get(2).getValue().getValue()));
            cachedData.setDowntime(toInt(values.get(3).getValue().getValue()));
            cachedData.setRooltarget(toInt(values.get(4).getValue().getValue()));
        }
        

        broadcastData();
    }
}

/*package Engine;


import Dto.OpcEntity;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpcCommunication implements CommandLineRunner {

    public final String endpointurl = "opc.tcp://127.0.0.1:49320";
    OpcEntity opc = new OpcEntity();

    @Override
    public void run(String... args) throws Exception {
        opccommunication();

    }

    public void opccommunication() {

        try {
            OpcUaClient client = OpcUaClient.create(endpointurl);

            client.connect().get();
            System.out.println("Connected!");

            NodeId nodeId = NodeId.parse("ns=2;s=Channel1.Device1.13R");
            DataValue value = client.readValue(0, TimestampsToReturn.Both, nodeId).get();
            System.out.println("Tag value: " + value.getValue());

        } catch (Exception e) {
            System.err.println("OPC UA Communication error: " + e.getMessage());
            e.printStackTrace();
        }

    }
}

 */

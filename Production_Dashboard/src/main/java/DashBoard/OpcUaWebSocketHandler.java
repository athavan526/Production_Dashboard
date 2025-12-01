package DashBoard;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class OpcUaWebSocketHandler extends TextWebSocketHandler {
    
    private final Mapping mapping;
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    public OpcUaWebSocketHandler(Mapping mapping) {
        this.mapping = mapping;
    }
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("WebSocket connected: " + session.getId());
        mapping.addWebSocketSession(session);
        sendCurrentData(session);
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        System.out.println("WebSocket closed: " + session.getId());
        mapping.removeWebSocketSession(session);
    }
    
    private void sendCurrentData(WebSocketSession session) {
        try {
            String json = mapping.getDataAsJson();
            session.sendMessage(new TextMessage(json));
        } catch (Exception e) {
            try {
                session.sendMessage(new TextMessage("{\"error\":\"" + e.getMessage() + "\"}"));
            } catch (Exception ex) {
                System.err.println("Failed to send error: " + ex.getMessage());
            }
        }
    }
    
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        if ("REFRESH".equalsIgnoreCase(message.getPayload())) {
            sendCurrentData(session);
        }
    }
}

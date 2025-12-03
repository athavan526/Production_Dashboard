
# Spring Boot OPC UA Live Production Dashboard 🚀

[![Java](https://img.shields.io/badge/Java-17%2B-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green.svg)](https://spring.io/projects/spring-boot)
[![Eclipse Milo](https://img.shields.io/badge/Eclipse%20Milo-OPC%20UA-orange.svg)](https://github.com/eclipse/milo)
[![WebSocket](https://img.shields.io/badge/WebSocket-Live%20Data-yellow.svg)](https://developer.mozilla.org/en-US/docs/Web/API/WebSocket)


**Complete Industrial IoT solution**: Mitsubishi iQ-R PLC → Kepware OPC UA → Spring Boot Dashboard → Live WebSocket Browser Updates. Real-time OEE, production actual/target, uptime monitoring for MES/SCADA systems.

## 🎯 Features

- **Mitsubishi iQ-R Integration** via Kepware SLMP4E driver
- **Eclipse Milo OPC UA Client** - 5-node subscriptions (actual/target/uptime/downtime/takt)
- **Live WebSocket Broadcasting** - `CopyOnWriteArrayList<WebSocketSession>` multi-client
- **Thread-Safe** - Synchronized `cachedData` updates + concurrent session management
- **Production Ready** - `@PostConstruct/@PreDestroy`, 5s connect timeout, error recovery

## 📡 Complete Data Flow
Mitsubishi iQ-R PLC (R08CPU)
↓ Ethernet/SLMP (D100-D104)
Kepware KEPServerEX (Channel1.Device1)
↓ OPC UA (opc.tcp://127.0.0.1:49320)
Spring Boot + Eclipse Milo Client
↓ WebSocket Broadcast (/opcua-data)
Multiple Browser Dashboards (Live Updates)

## 📊 Monitored PLC Registers → OPC UA Nodes

| PLC Address | Kepware Tag | Dashboard Field | Purpose |
|-------------|-------------|-----------------|---------|
| `D100` | `Channel1.Device1.actual` | `setActual()` | Production Count |
| `D101` | `Channel1.Device1.target` | `setTarget()` | Production Target |
| `D102` | `Channel1.Device1.uptime` | `setUptime()` | Machine Runtime (s) |
| `D103` | `Channel1.Device1.downtime` | `setDowntime()` | Downtime (s) |
| `D104` | `Channel1.Device1.takt` | `setRooltarget()` | Takt Time |

## 🎥 Demo Video





https://github.com/user-attachments/assets/d432e91e-e952-47ef-ab23-31f83654c3bf





### Prerequisites
- Java 17+, Maven 3.6+
- Mitsubishi iQ-R PLC (R08CPU) with Ethernet module
- Kepware KEPServerEX v6.12+ (Mitsubishi Ethernet driver)
- GX Works3 for PLC programming


### 4. Live Dashboard
http://localhost:8080/dashboard



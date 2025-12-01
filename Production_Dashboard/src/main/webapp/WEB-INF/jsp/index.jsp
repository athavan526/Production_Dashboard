<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>Production Dashboard</title>

    <style>
    body {
        font-family: Arial, sans-serif;
        background: #121212;
        margin: 0;
        padding: 0;
        color: #e0e0e0;
    }

    .container {
        width: 95%;
        margin: auto;
        padding: 20px;
        background-color: #1e1e1e;
        border-radius: 12px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.8);
    }

    h1 {
        font-size: 30px;
        margin-bottom: 20px;
        color: #ffffff;
    }

    .kpi-row {
        display: flex;
        gap: 20px;
        margin-bottom: 20px;
    }

    .kpi {
        flex: 1;
        background: #292929;
        padding: 20px;
        border-radius: 12px;
        box-shadow: 0 4px 12px rgba(255, 255, 255, 0.05);
    }

    .kpi-title {
        font-size: 18px;
        color: #bbbbbb;
    }

    .kpi-value {
        font-size: 32px;
        font-weight: bold;
        color: #ffffff;
    }

    .progress-card {
        background: #292929;
        padding: 20px;
        border-radius: 12px;
        box-shadow: 0 4px 12px rgba(255, 255, 255, 0.05);
        margin-bottom: 20px;
    }

    .progress-title {
        font-size: 20px;
        color: #ddd;
        margin-bottom: 10px;
    }

    .progress-bar-container {
        width: 100%;
        height: 30px;
        background: #444444;
        border-radius: 20px;
        overflow: hidden;
    }

    .progress-bar {
        height: 30px;
        width: 0%;
        background: #76c7c0; /* teal/cyan shade */
        transition: width 1s ease-in-out;
    }

    .chart-box {
        background: #292929;
        padding: 20px;
        border-radius: 12px;
        height: 250px;
        box-shadow: 0 4px 12px rgba(255, 255, 255, 0.05);
        margin-bottom: 20px;
    }

    .chart-title {
        font-size: 20px;
        color: #ddd;
        margin-bottom: 10px;
    }

    /* Optional: darker scrollbar style for modern browsers */
    .container::-webkit-scrollbar {
        width: 8px;
    }

    .container::-webkit-scrollbar-thumb {
        background-color: #555;
        border-radius: 4px;
    }
</style>
    <script>
        // TEMPORARY DEMO VALUES
        function loadData() {
            const data = {
                target: 100,
                actual: 75,
                uptime: 92,
                downtime: 8,
                quality: 100 // Always 100%
            };

            document.getElementById("target").innerText = data.target;
            document.getElementById("actual").innerText = data.actual;
            document.getElementById("uptime").innerText = data.uptime +;
            document.getElementById("downtime").innerText = data.downtime ;
            document.getElementById("quality").innerText = data.quality + "%";

            let percent = ((data.actual / data.target) * 100).toFixed(1);
            document.getElementById("prodPercent").innerText = percent + "%";
            document.getElementById("progressFill").style.width = percent + "%";
        }

        window.onload = loadData;
    </script>
</head>
<body>
    <div class="container">
        <h1>Production Dashboard</h1>

        <!-- TOP KPI ROW -->
        <div class="kpi-row">
            <div class="kpi">
                <div class="kpi-title">Target</div>
                <div class="kpi-value" id="target">0</div>
            </div>
            <div class="kpi">
                <div class="kpi-title">Actual</div>
                <div class="kpi-value" id="actual">0</div>
            </div>
            <div class="kpi">
                <div class="kpi-title">Quality</div>
                <div class="kpi-value" id="quality">100%</div>
            </div>
        </div>

        <div class="kpi-row">
            <div class="kpi">
                <div class="kpi-title">Uptime</div>
                <div class="kpi-value" id="uptime">0%</div>
            </div>
            <div class="kpi">
                <div class="kpi-title">Downtime</div>
                <div class="kpi-value" id="downtime">0%</div>
            </div>
        </div>

        <!-- PRODUCTION PERCENT CARD -->
        <div class="progress-card">
            <div class="progress-title">Production Percentage</div>
            <div style="font-size:28px; font-weight:bold; margin-bottom:10px;" id="prodPercent">0%</div>
            <div class="progress-bar-container">
                <div class="progress-bar" id="progressFill"></div>
            </div>
        </div>

        <!-- CHART PLACEHOLDER -->
        <div class="chart-box">
            <div class="chart-title">Runtime vs Downtime (Coming Soon)</div>
            <div style="color:#aaa; text-align:center; margin-top:80px;">Chart will appear here</div>
        </div>

    </div>
    <canvas id="prodChart" style="margin-top:30px;"></canvas>

  <script>
      // 1. Helper function to convert Seconds to HH:MM:SS
      function secondsToHms(d) {
          d = Number(d);
          if (d <= 0) return "00:00:00";

          var h = Math.floor(d / 3600);
          var m = Math.floor(d % 3600 / 60);
          var s = Math.floor(d % 3600 % 60);

          // Add leading zeros (e.g., 5 becomes 05)
          var hDisplay = h < 10 ? "0" + h : h;
          var mDisplay = m < 10 ? "0" + m : m;
          var sDisplay = s < 10 ? "0" + s : s;

          return hDisplay + ":" + mDisplay + ":" + sDisplay;
      }

      const ws = new WebSocket('ws://' + window.location.host + '/opcua-data');

      ws.onopen = () => console.log('WebSocket connected');
      ws.onclose = () => console.log('WebSocket disconnected');
      ws.onerror = (error) => console.error('WebSocket error', error);

      ws.onmessage = (event) => {
          try {
              const data = JSON.parse(event.data);

              // Ensure inputs are numbers
              const target = Number(data.target) || 0;
              const actual = Number(data.actual) || 0;
              const uptimeSec = Number(data.uptime) || 0;
              const downtimeSec = Number(data.downtime) || 0;

              // --- UPDATE VALUES ---
              document.getElementById("target").innerText = target;
              document.getElementById("actual").innerText = actual;

              // Apply the HH:MM:SS formatting here
              document.getElementById("uptime").innerText = secondsToHms(uptimeSec);
              document.getElementById("downtime").innerText = secondsToHms(downtimeSec);

              document.getElementById("quality").innerText = "100%";

              // Safety check for rooltarget
              const roolElem = document.getElementById("rooltarget");
              if (roolElem) {
                  roolElem.innerText = data.rooltarget || 0;
              }

              // Calculate Percentage
              let productionPercent = 0;
              if (target > 0) {
                  productionPercent = (actual * 100.0) / target;
              }
              document.getElementById("prodPercent").innerText = productionPercent.toFixed(1) + "%";
              document.getElementById("progressFill").style.width = productionPercent + "%";

          } catch (e) {
              console.error("Error processing message:", e);
          }
      };
  </script>

</body>
</html>
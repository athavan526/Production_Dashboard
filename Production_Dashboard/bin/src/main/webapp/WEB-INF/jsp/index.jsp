<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>Production Dashboard</title>
    <style>
        body { font-family: Arial, sans-serif; background:#f4f6f9; margin:0; padding:0; }
        .container { width:95%; margin:auto; padding:20px; }
        h1 { font-size:30px; margin-bottom:20px; }
        .kpi-row { display:flex; gap:20px; margin-bottom:20px; }
        .kpi {
            flex:1; background:white; padding:20px; border-radius:12px;
            box-shadow:0 4px 12px rgba(0,0,0,0.08);
        }
        .kpi-title { font-size:18px; color:#555; }
        .kpi-value { font-size:32px; font-weight:bold; color:#222; }

        .progress-card { background:white; padding:20px; border-radius:12px; box-shadow:0 4px 12px rgba(0,0,0,0.08); margin-bottom:20px; }
        .progress-title { font-size:20px; margin-bottom:10px; }
        .progress-bar-container {
            width:100%; height:30px; background:#e1e1e1; border-radius:20px; overflow:hidden;
        }
        .progress-bar { height:30px; width:0%; background:#4caf50; transition:width 1s ease-in-out; }

        /* Chart placeholder */
        .chart-box { background:white; padding:20px; border-radius:12px; height:250px;
            box-shadow:0 4px 12px rgba(0,0,0,0.08); margin-bottom:20px; }
        .chart-title { font-size:20px; margin-bottom:10px; }
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
            document.getElementById("uptime").innerText = data.uptime + "%";
            document.getElementById("downtime").innerText = data.downtime + "%";
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
    const ctx = document.getElementById('prodChart').getContext('2d');
    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: ['Target', 'Actual'],
            datasets: [{
                label: 'Production Comparison',
                data: [<%= target %>, <%= actual %>],
                backgroundColor: ['#4e73df', '#1cc88a']
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { labels: { color: 'white' }}
            },
            scales: {
                x: { ticks: { color: 'white' } },
                y: { ticks: { color: 'white' } }
            }
        }
    });
</script>

</body>
</html>
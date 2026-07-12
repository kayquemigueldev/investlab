document.addEventListener("DOMContentLoaded", () => {
    const canvas = document.getElementById("investmentChart");
    const dataContainer = document.getElementById(
        "investmentChartData"
    );

    if (!canvas || !dataContainer) {
        console.error("No canvas loaded");
        return;
    }

    const points = Array.from(dataContainer.children).map(element => ({
        date: element.dataset.date,
        invested: Number(element.dataset.invested),
        balance: Number(element.dataset.balance)
    }));

    if (points.length === 0) {
        console.error("No points loaded");
        return;
    }

    const context = canvas.getContext("2d");
    const tooltip = document.getElementById("chartTooltip");
    const tooltipDate = document.getElementById("tooltipDate");
    const tooltipInvested = document.getElementById("tooltipInvested");
    const tooltipBalance = document.getElementById("tooltipBalance");

    const currencyFormatter = new Intl.NumberFormat("pt-BR", {
        style: "currency",
        currency: "BRL"
    });

    let plottedPoints = [];

    function resizeCanvas() {
        const ratio = window.devicePixelRatio || 1;
        const rectangle = canvas.getBoundingClientRect();

        canvas.width = Math.round(rectangle.width * ratio);
        canvas.height = Math.round(rectangle.height * ratio);

        context.setTransform(ratio, 0, 0, ratio, 0, 0);

        drawChart();
    }

    function drawChart() {
        const width = canvas.clientWidth;
        const height = canvas.clientHeight;

        context.clearRect(0, 0, width, height);

        const padding = {
            top: 28,
            right: 28,
            bottom: 48,
            left: 74
        };

        const chartWidth = width - padding.left - padding.right;
        const chartHeight = height - padding.top - padding.bottom;

        const maximumValue = Math.max(
            ...points.map(point =>
                Math.max(point.invested, point.balance)
            )
        );

        const roundedMaximum = Math.ceil(maximumValue / 1000) * 1000
            || 1000;

        drawGrid(
            width,
            chartWidth,
            chartHeight,
            padding,
            roundedMaximum
        );

        const investedCoordinates = createCoordinates(
            points.map(point => point.invested),
            chartWidth,
            chartHeight,
            padding,
            roundedMaximum
        );

        const balanceCoordinates = createCoordinates(
            points.map(point => point.balance),
            chartWidth,
            chartHeight,
            padding,
            roundedMaximum
        );

        drawArea(
            balanceCoordinates,
            padding.top + chartHeight
        );

        drawLine(investedCoordinates, "#6f8f86", 2);
        drawLine(balanceCoordinates, "#45e6a8", 3);

        drawPoints(balanceCoordinates);
        drawLabels(chartWidth, chartHeight, padding);

        plottedPoints = balanceCoordinates.map(
            (coordinate, index) => ({
                ...coordinate,
                data: points[index]
            })
        );
    }

    function drawGrid(
        width,
        chartWidth,
        chartHeight,
        padding,
        maximum
    ) {
        const gridLines = 4;

        context.font = "12px -apple-system, BlinkMacSystemFont, sans-serif";
        context.textAlign = "right";
        context.textBaseline = "middle";

        for (let index = 0; index <= gridLines; index++) {
            const percentage = index / gridLines;
            const y = padding.top + chartHeight
                - chartHeight * percentage;

            context.beginPath();
            context.strokeStyle = "rgba(155, 176, 170, 0.14)";
            context.font = "20px -apple-system, BlinkMacSystemFont, sans-serif";
            context.lineJoin = "round";
            context.lineWidth = 1;
            context.moveTo(padding.left, y);
            context.lineTo(padding.left + chartWidth, y);
            context.stroke();

            const value = maximum * percentage;

            context.fillStyle = "#9bb0aa";
            context.fillText(
                compactCurrency(value),
                padding.left - 12,
                y
            );
        }

        context.beginPath();
        context.strokeStyle = "rgba(155, 176, 170, 0.22)";
        context.moveTo(padding.left, padding.top);
        context.lineTo(
            padding.left,
            padding.top + chartHeight
        );
        context.lineTo(
            padding.left + chartWidth,
            padding.top + chartHeight
        );
        context.stroke();
    }

    function createCoordinates(
        values,
        chartWidth,
        chartHeight,
        padding,
        maximum
    ) {
        const divisor = Math.max(values.length - 1, 1);

        return values.map((value, index) => ({
            x: padding.left + chartWidth * (index / divisor),
            y: padding.top
                + chartHeight
                - chartHeight * (value / maximum)
        }));
    }

    function drawLine(coordinates, color, lineWidth) {
        if (coordinates.length === 0) {
            return;
        }

        context.beginPath();
        context.strokeStyle = color;
        context.lineWidth = lineWidth;
        context.lineJoin = "round";
        context.lineCap = "round";

        coordinates.forEach((coordinate, index) => {
            if (index === 0) {
                context.moveTo(coordinate.x, coordinate.y);
                return;
            }

            context.lineTo(coordinate.x, coordinate.y);
        });

        context.stroke();
    }

    function drawArea(coordinates, bottom) {
        if (coordinates.length === 0) {
            return;
        }

        const gradient = context.createLinearGradient(
            0,
            0,
            0,
            bottom
        );

        gradient.addColorStop(0, "rgba(69, 230, 168, 0.22)");
        gradient.addColorStop(1, "rgba(69, 230, 168, 0)");

        context.beginPath();
        context.moveTo(coordinates[0].x, bottom);

        coordinates.forEach(coordinate => {
            context.lineTo(coordinate.x, coordinate.y);
        });

        context.lineTo(
            coordinates[coordinates.length - 1].x,
            bottom
        );

        context.closePath();
        context.fillStyle = gradient;
        context.fill();
    }

    function drawPoints(coordinates) {
        coordinates.forEach(coordinate => {
            context.beginPath();
            context.arc(
                coordinate.x,
                coordinate.y,
                4,
                0,
                Math.PI * 2
            );
            context.fillStyle = "#08110f";
            context.fill();
            context.lineWidth = 2;
            context.strokeStyle = "#45e6a8";
            context.stroke();
        });
    }

    function drawLabels(chartWidth, chartHeight, padding) {
        const visibleIndexes = new Set([
            0,
            Math.floor((points.length - 1) / 2),
            points.length - 1
        ]);

        context.fillStyle = "#9bb0aa";
        context.font = "12px -apple-system, BlinkMacSystemFont, sans-serif";
        context.textAlign = "center";
        context.textBaseline = "top";

        const divisor = Math.max(points.length - 1, 1);

        visibleIndexes.forEach(index => {
            const x = padding.left
                + chartWidth * (index / divisor);

            context.fillText(
                points[index].date,
                x,
                padding.top + chartHeight + 16
            );
        });
    }

    function compactCurrency(value) {
        if (value >= 1000000) {
            return `R$ ${(value / 1000000).toFixed(1)} mi`;
        }

        if (value >= 1000) {
            return `R$ ${(value / 1000).toFixed(1)} mil`;
        }

        return `R$ ${value.toFixed(0)}`;
    }

    function showTooltip(event) {
        const rectangle = canvas.getBoundingClientRect();
        const mouseX = event.clientX - rectangle.left;

        const closestPoint = plottedPoints.reduce(
            (closest, point) => {
                const distance = Math.abs(point.x - mouseX);
                const closestDistance = Math.abs(
                    closest.x - mouseX
                );

                return distance < closestDistance
                    ? point
                    : closest;
            }
        );

        tooltipDate.textContent = closestPoint.data.date;
        tooltipInvested.textContent = currencyFormatter.format(
            closestPoint.data.invested
        );
        tooltipBalance.textContent = currencyFormatter.format(
            closestPoint.data.balance
        );

        tooltip.style.left = `${closestPoint.x}px`;
        tooltip.style.top = `${closestPoint.y}px`;
        tooltip.hidden = false;
    }

    canvas.addEventListener("mousemove", showTooltip);

    canvas.addEventListener("mouseleave", () => {
        tooltip.hidden = true;
    });

    window.addEventListener("resize", resizeCanvas);

    resizeCanvas();
});
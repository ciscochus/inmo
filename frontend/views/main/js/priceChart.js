


window.createPriceChart = function(){
        var rawData = JSON.parse($("#priceChart-data").text());
        var config = {
            type: 'line',
            data: {
                datasets: [{
                    label: 'Evolución del precio',
                    backgroundColor: 'rgb(255, 99, 132)',
                    borderColor: 'rgb(255, 99, 132)',
                    data: rawData
                }]
            },
            options: {
                parsing: {
                    xAxisKey: 'x',
                    yAxisKey: 'y'
                }
            }
        }
    
        var myChart = new Chart(
            document.getElementById('canvas-price'),
            config
          );
    
        $("#canvas-price").prop("init",true);
    };
    
window.togglePriceChart = function(){
        if($("#canvas-price").prop("init") === undefined){
            createPriceChart();
        } else {
            $("#priceChart").toggle();
        }
            
    };


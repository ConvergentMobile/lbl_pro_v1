$(function () {
	var brandname=$('#brandname').val();
	var store=$('#store').val();
	var datav=[];
	 $.ajax({
			type : "get",
			url : "getCitationStoreHistoryInfo.htm",
			data:{
				store:store,brandname:brandname
			},
			cache : false,
			success : function(response) {
				//alert(response);
				var graph= $.parseJSON(response);


			$(graph).each(function(i,client){
				//alert(client.monthName);
				//var arr=JSON.stringify(client.monthName);
				
				datav.push({name: client.monthName, y: client.citationCount});
			});
			
		    $('#container').highcharts({
		        chart: {
		            type: 'column',
		            height: 285,
		            marginTop: 30
		        },
		        colors: ['#d0c8c3', '#b0cc00'],
		        title: {
		            text: ''
		        },
		        xAxis: {
		            type: 'category',
		        },
		        yAxis: {
		            title: ''
		        },
		        legend: {
		            enabled: false
		        },

		        plotOptions: {
		            series: {
		                borderWidth: 0,
		                dataLabels: {
		                    enabled: false,
		                    format: '{point.y:.1f}%'
		                },
		                shadow: false,
		                states: {
		                    hover: {
		                        brightness: -0.1
		                    }
		                }
		            }
		        },

		        series: [{
		            name: "Brands",
		            colorByPoint: true,
		            data: datav
		        }],


		    });
		
			}
	 });
});



$(function () {
	var brandname=$('#brandname').val();
	var datav=[];
	 $.ajax({
			type : "get",
			url : "getCitationBrandGraphInfo.htm",
			data:{
				brandname:brandname,
			},
			cache : false,
			success : function(response) {
				//alert(response);
				var graph= $.parseJSON(response);


			$(graph).each(function(i,client){
				//alert(client.monthName);
				
				datav.push({name: client.monthName, y: client.citationCount});
			});
    // Create the chart
    $('#container2').highcharts({
        chart: {
            type: 'column',
            height: 285,
            marginTop: 30
        },
        colors: ['#d0c8c3', '#b0cc00'],
        title: {
            text: ''
        },
        xAxis: {
            type: 'category',
        },
        yAxis: {
            title: ''
        },
        legend: {
            enabled: false
        },

        plotOptions: {
            series: {
                borderWidth: 0,
                dataLabels: {
                    enabled: false,
                    format: '{point.y:.1f}%'
                },
                shadow: false,
                states: {
                    hover: {
                        brightness: -0.1
                    }
                }
            }
        },

        series: [{
            name: "Brands",
            colorByPoint: true,
            data: datav
        }],


    });
			}
	 });
});
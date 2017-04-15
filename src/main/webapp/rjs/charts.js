$(function () {

Highcharts.theme = {
   colors: ["#82b2e4", "#d40000"],
   chart: {
      backgroundColor: null,
      style: {
         fontFamily: "OpenSansRegular"
      }
   },
   title: {
      style: {
         color: 'black',
         fontSize: '16px',
         fontWeight: 'bold'
      }
   },
   tooltip: {
      borderWidth: 0
   },
   legend: {
      itemStyle: {
         fontWeight: 'normal',
         fontSize: '13px',
         color: 'white'
      }
   },
   
   plotOptions: {
      series: {
         shadow: true
      },
      candlestick: {
         lineColor: '#404048'
      },
      map: {
         shadow: false
      }
   },

   // Highstock specific
   navigator: {
      xAxis: {
         gridLineColor: '#D0D0D8'
      }
   },
   rangeSelector: {
      buttonTheme: {
         fill: 'white',
         stroke: '#C0C0C8',
         'stroke-width': 1,
         states: {
            select: {
               fill: '#D0D0D8'
            }
         }
      }
   },
   
      
};

Highcharts.setOptions(Highcharts.theme);

    $('#container').highcharts({
        chart: {
            type: 'pie',
            height: 285,
            backgroundColor: {
              linearGradient: [0, 0, 500, 500],
              stops: [
                  [0, 'rgba(255, 255, 255, 0)'],
                  [1, 'rgba(240, 240, 255, 0)']
              ]
            },
            
            options3d: {
              enabled: true,
                alpha: 70,
                beta: 0,
            }
        },

        plotOptions: {
                pie: {
                    depth: 40,
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false
                    },
                    showInLegend: true
                }
            },

          title: {
            text: ''
        },

        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },

        series: [{
            type: 'pie',
            name: 'Browser share',
            data: [
                ['Found',   98.0],
                {
                    name: 'Not Found',
                    y: 2.0,
                    sliced: true,
                    selected: true
                }
            ]
        }]
    });
});


$(function () {

Highcharts.theme = {
   colors: ["#82b2e4", "#d40000"],
   chart: {
      backgroundColor: null,
      style: {
         fontFamily: "OpenSansRegular"
      }
   },
   title: {
      style: {
         color: '#4e5b62',
         fontSize: '13px',
         fontWeight: 'normal'
      }
   },
   tooltip: {
      borderWidth: 0
   },
   legend: {
      itemStyle: {
         fontWeight: 'normal',
         fontSize: '13px',
         color: 'white'
      }
   },
   
   plotOptions: {
      series: {
         shadow: true
      },
      candlestick: {
         lineColor: '#404048'
      },
      map: {
         shadow: false
      }
   },

   // Highstock specific
   navigator: {
      xAxis: {
         gridLineColor: '#D0D0D8'
      }
   },
   rangeSelector: {
      buttonTheme: {
         fill: 'white',
         stroke: '#C0C0C8',
         'stroke-width': 1,
         states: {
            select: {
               fill: '#D0D0D8'
            }
         }
      }
   },
   
      
};

Highcharts.setOptions(Highcharts.theme);

    $('#container2').highcharts({
        chart: {
            type: 'pie',
            height: 130,
            marginTop: 0,
            spacingBottom: -30,
            backgroundColor: {
              linearGradient: [0, 0, 500, 500],
              stops: [
                  [0, 'rgba(255, 255, 255, 0)'],
                  [1, 'rgba(240, 240, 255, 0)']
              ]
            },
            
            options3d: {
              enabled: true,
                alpha: 70,
                beta: 0,
            }
        },

        plotOptions: {
                pie: {
                    depth: 20,
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false
                    },
                    showInLegend: false
                }
            },

          title: {
            text: 'Listings Found',
            y: 5
        },

        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },

        series: [{
            type: 'pie',
            name: 'Browser share',
            data: [
                ['Found',   98.0],
                {
                    name: 'Not Found',
                    y: 2.0,
                    sliced: true,
                    selected: true
                }
            ]
        }]
    });
});

$(function () {

Highcharts.theme = {
   colors: ["#82b2e4", "#d40000"],
   chart: {
      backgroundColor: null,
      style: {
         fontFamily: "OpenSansRegular"
      }
   },
   title: {
      style: {
         color: '#4e5b62',
         fontSize: '13px',
         fontWeight: 'normal'
      }
   },
   tooltip: {
      borderWidth: 0
   },
   legend: {
      itemStyle: {
         fontWeight: 'normal',
         fontSize: '13px',
         color: 'white'
      }
   },
   
   plotOptions: {
      series: {
         shadow: true
      },
      candlestick: {
         lineColor: '#404048'
      },
      map: {
         shadow: false
      }
   },

   // Highstock specific
   navigator: {
      xAxis: {
         gridLineColor: '#D0D0D8'
      }
   },
   rangeSelector: {
      buttonTheme: {
         fill: 'white',
         stroke: '#C0C0C8',
         'stroke-width': 1,
         states: {
            select: {
               fill: '#D0D0D8'
            }
         }
      }
   },
   
      
};

Highcharts.setOptions(Highcharts.theme);

    $('#container3').highcharts({
        chart: {
            type: 'pie',
            height: 130,
            marginTop: 0,
            spacingBottom: -30,
            backgroundColor: {
              linearGradient: [0, 0, 500, 500],
              stops: [
                  [0, 'rgba(255, 255, 255, 0)'],
                  [1, 'rgba(240, 240, 255, 0)']
              ]
            },
            
            options3d: {
              enabled: true,
                alpha: 70,
                beta: 0,
            }
        },

        plotOptions: {
                pie: {
                    depth: 20,
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false
                    },
                    showInLegend: false
                }
            },

          title: {
            text: 'Listings Found',
            y: 5
        },

        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },

        series: [{
            type: 'pie',
            name: 'Browser share',
            data: [
                ['Found',   98.0],
                {
                    name: 'Not Found',
                    y: 2.0,
                    sliced: true,
                    selected: true
                }
            ]
        }]
    });
});

$(function () {

Highcharts.theme = {
   colors: ["#82b2e4", "#d40000"],
   chart: {
      backgroundColor: null,
      style: {
         fontFamily: "OpenSansRegular"
      }
   },
   title: {
      style: {
         color: '#4e5b62',
         fontSize: '13px',
         fontWeight: 'normal'
      }
   },
   tooltip: {
      borderWidth: 0
   },
   legend: {
      itemStyle: {
         fontWeight: 'normal',
         fontSize: '13px',
         color: 'white'
      }
   },
   
   plotOptions: {
      series: {
         shadow: true
      },
      candlestick: {
         lineColor: '#404048'
      },
      map: {
         shadow: false
      }
   },

   // Highstock specific
   navigator: {
      xAxis: {
         gridLineColor: '#D0D0D8'
      }
   },
   rangeSelector: {
      buttonTheme: {
         fill: 'white',
         stroke: '#C0C0C8',
         'stroke-width': 1,
         states: {
            select: {
               fill: '#D0D0D8'
            }
         }
      }
   },
   
      
};

Highcharts.setOptions(Highcharts.theme);

    $('#container4').highcharts({
        chart: {
            type: 'pie',
            height: 130,
            marginTop: 0,
            spacingBottom: -30,
            backgroundColor: {
              linearGradient: [0, 0, 500, 500],
              stops: [
                  [0, 'rgba(255, 255, 255, 0)'],
                  [1, 'rgba(240, 240, 255, 0)']
              ]
            },
            
            options3d: {
              enabled: true,
                alpha: 70,
                beta: 0,
            }
        },

        plotOptions: {
                pie: {
                    depth: 20,
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false
                    },
                    showInLegend: false
                }
            },

          title: {
            text: 'Listings Found',
            y: 5
        },

        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },

        series: [{
            type: 'pie',
            name: 'Browser share',
            data: [
                ['Found',   98.0],
                {
                    name: 'Not Found',
                    y: 2.0,
                    sliced: true,
                    selected: true
                }
            ]
        }]
    });
});

$(function () {
	$.ajax({
		type : "get",
		url : "getGoogleGraphInfo.htm",
		cache : false,
		data:{
			store:"000902141",
		},
		success : function(response) {
		alert(response)	;
		}
	});

Highcharts.theme = {
   colors: ["#82b2e4", "#d40000"],
   chart: {
      backgroundColor: null,
      style: {
         fontFamily: "OpenSansRegular"
      }
   },
   title: {
      style: {
         color: '#4e5b62',
         fontSize: '13px',
         fontWeight: 'normal'
      }
   },
   tooltip: {
      borderWidth: 0
   },
   legend: {
      itemStyle: {
         fontWeight: 'normal',
         fontSize: '13px',
         color: 'white'
      }
   },
   
   plotOptions: {
      series: {
         shadow: true
      },
      candlestick: {
         lineColor: '#404048'
      },
      map: {
         shadow: false
      }
   },

   // Highstock specific
   navigator: {
      xAxis: {
         gridLineColor: '#D0D0D8'
      }
   },
   rangeSelector: {
      buttonTheme: {
         fill: 'white',
         stroke: '#C0C0C8',
         'stroke-width': 1,
         states: {
            select: {
               fill: '#D0D0D8'
            }
         }
      }
   },
   
      
};

Highcharts.setOptions(Highcharts.theme);

    $('#container5').highcharts({
        chart: {
            type: 'pie',
            height: 130,
            marginTop: 0,
            spacingBottom: -30,
            backgroundColor: {
              linearGradient: [0, 0, 500, 500],
              stops: [
                  [0, 'rgba(255, 255, 255, 0)'],
                  [1, 'rgba(240, 240, 255, 0)']
              ]
            },
            
            options3d: {
              enabled: true,
                alpha: 70,
                beta: 0,
            }
        },

        plotOptions: {
                pie: {
                    depth: 20,
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false
                    },
                    showInLegend: false
                }
            },

          title: {
            text: 'Listings Found',
            y: 5
        },

        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },

        series: [{
            type: 'pie',
            name: 'Browser share',
            data: [
                ['Found',   98.0],
                {
                    name: 'Not Found',
                    y: 2.0,
                    sliced: true,
                    selected: true
                }
            ]
        }]
    });
});

$(function () {

Highcharts.theme = {
   colors: ["#82b2e4", "#d40000"],
   chart: {
      backgroundColor: null,
      style: {
         fontFamily: "OpenSansRegular"
      }
   },
   title: {
      style: {
         color: '#4e5b62',
         fontSize: '13px',
         fontWeight: 'normal'
      }
   },
   tooltip: {
      borderWidth: 0
   },
   legend: {
      itemStyle: {
         fontWeight: 'normal',
         fontSize: '13px',
         color: 'white'
      }
   },
   
   plotOptions: {
      series: {
         shadow: true
      },
      candlestick: {
         lineColor: '#404048'
      },
      map: {
         shadow: false
      }
   },

   // Highstock specific
   navigator: {
      xAxis: {
         gridLineColor: '#D0D0D8'
      }
   },
   rangeSelector: {
      buttonTheme: {
         fill: 'white',
         stroke: '#C0C0C8',
         'stroke-width': 1,
         states: {
            select: {
               fill: '#D0D0D8'
            }
         }
      }
   },
   
      
};

Highcharts.setOptions(Highcharts.theme);

    $('#container6').highcharts({
        chart: {
            type: 'pie',
            height: 130,
            marginTop: 0,
            spacingBottom: -30,
            backgroundColor: {
              linearGradient: [0, 0, 500, 500],
              stops: [
                  [0, 'rgba(255, 255, 255, 0)'],
                  [1, 'rgba(240, 240, 255, 0)']
              ]
            },
            
            options3d: {
              enabled: true,
                alpha: 70,
                beta: 0,
            }
        },

        plotOptions: {
                pie: {
                    depth: 20,
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false
                    },
                    showInLegend: false
                }
            },

          title: {
            text: 'Listings Found',
            y: 5
        },

        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },

        series: [{
            type: 'pie',
            name: 'Browser share',
            data: [
                ['Found',   98.0],
                {
                    name: 'Not Found',
                    y: 2.0,
                    sliced: true,
                    selected: true
                }
            ]
        }]
    });
});

$(function () {

Highcharts.theme = {
   colors: ["#82b2e4", "#d40000"],
   chart: {
      backgroundColor: null,
      style: {
         fontFamily: "OpenSansRegular"
      }
   },
   title: {
      style: {
         color: '#4e5b62',
         fontSize: '13px',
         fontWeight: 'normal'
      }
   },
   tooltip: {
      borderWidth: 0
   },
   legend: {
      itemStyle: {
         fontWeight: 'normal',
         fontSize: '13px',
         color: 'white'
      }
   },
   
   plotOptions: {
      series: {
         shadow: true
      },
      candlestick: {
         lineColor: '#404048'
      },
      map: {
         shadow: false
      }
   },

   // Highstock specific
   navigator: {
      xAxis: {
         gridLineColor: '#D0D0D8'
      }
   },
   rangeSelector: {
      buttonTheme: {
         fill: 'white',
         stroke: '#C0C0C8',
         'stroke-width': 1,
         states: {
            select: {
               fill: '#D0D0D8'
            }
         }
      }
   },
   
      
};

Highcharts.setOptions(Highcharts.theme);

    $('#container7').highcharts({
        chart: {
            type: 'pie',
            height: 130,
            marginTop: 0,
            spacingBottom: -30,
            backgroundColor: {
              linearGradient: [0, 0, 500, 500],
              stops: [
                  [0, 'rgba(255, 255, 255, 0)'],
                  [1, 'rgba(240, 240, 255, 0)']
              ]
            },
            
            options3d: {
              enabled: true,
                alpha: 70,
                beta: 0,
            }
        },

        plotOptions: {
                pie: {
                    depth: 20,
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false
                    },
                    showInLegend: false
                }
            },

          title: {
            text: 'Listings Found',
            y: 5
        },

        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },

        series: [{
            type: 'pie',
            name: 'Browser share',
            data: [
                ['Found',   98.0],
                {
                    name: 'Not Found',
                    y: 2.0,
                    sliced: true,
                    selected: true
                }
            ]
        }]
    });
});

$(function () {

Highcharts.theme = {
   colors: ["#82b2e4", "#d40000"],
   chart: {
      backgroundColor: null,
      style: {
         fontFamily: "OpenSansRegular"
      }
   },
   title: {
      style: {
         color: '#4e5b62',
         fontSize: '13px',
         fontWeight: 'normal'
      }
   },
   tooltip: {
      borderWidth: 0
   },
   legend: {
      itemStyle: {
         fontWeight: 'normal',
         fontSize: '13px',
         color: 'white'
      }
   },
   
   plotOptions: {
      series: {
         shadow: true
      },
      candlestick: {
         lineColor: '#404048'
      },
      map: {
         shadow: false
      }
   },

   // Highstock specific
   navigator: {
      xAxis: {
         gridLineColor: '#D0D0D8'
      }
   },
   rangeSelector: {
      buttonTheme: {
         fill: 'white',
         stroke: '#C0C0C8',
         'stroke-width': 1,
         states: {
            select: {
               fill: '#D0D0D8'
            }
         }
      }
   },
   
      
};

Highcharts.setOptions(Highcharts.theme);

    $('#container8').highcharts({
        chart: {
            type: 'pie',
            height: 130,
            marginTop: 0,
            spacingBottom: -30,
            backgroundColor: {
              linearGradient: [0, 0, 500, 500],
              stops: [
                  [0, 'rgba(255, 255, 255, 0)'],
                  [1, 'rgba(240, 240, 255, 0)']
              ]
            },
            
            options3d: {
              enabled: true,
                alpha: 70,
                beta: 0,
            }
        },

        plotOptions: {
                pie: {
                    depth: 20,
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false
                    },
                    showInLegend: false
                }
            },

          title: {
            text: 'Listings Found',
            y: 5
        },

        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },

        series: [{
            type: 'pie',
            name: 'Browser share',
            data: [
                ['Found',   98.0],
                {
                    name: 'Not Found',
                    y: 2.0,
                    sliced: true,
                    selected: true
                }
            ]
        }]
    });
});

$(function () {

Highcharts.theme = {
   colors: ["#82b2e4", "#d40000"],
   chart: {
      backgroundColor: null,
      style: {
         fontFamily: "OpenSansRegular"
      }
   },
   title: {
      style: {
         color: '#4e5b62',
         fontSize: '13px',
         fontWeight: 'normal'
      }
   },
   tooltip: {
      borderWidth: 0
   },
   legend: {
      itemStyle: {
         fontWeight: 'normal',
         fontSize: '13px',
         color: 'white'
      }
   },
   
   plotOptions: {
      series: {
         shadow: true
      },
      candlestick: {
         lineColor: '#404048'
      },
      map: {
         shadow: false
      }
   },

   // Highstock specific
   navigator: {
      xAxis: {
         gridLineColor: '#D0D0D8'
      }
   },
   rangeSelector: {
      buttonTheme: {
         fill: 'white',
         stroke: '#C0C0C8',
         'stroke-width': 1,
         states: {
            select: {
               fill: '#D0D0D8'
            }
         }
      }
   },
   
      
};

Highcharts.setOptions(Highcharts.theme);

    $('#container9').highcharts({
        chart: {
            type: 'pie',
            height: 130,
            marginTop: 0,
            spacingBottom: -30,
            backgroundColor: {
              linearGradient: [0, 0, 500, 500],
              stops: [
                  [0, 'rgba(255, 255, 255, 0)'],
                  [1, 'rgba(240, 240, 255, 0)']
              ]
            },
            
            options3d: {
              enabled: true,
                alpha: 70,
                beta: 0,
            }
        },

        plotOptions: {
                pie: {
                    depth: 20,
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false
                    },
                    showInLegend: false
                }
            },

          title: {
            text: 'Listings Found',
            y: 5
        },

        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },

        series: [{
            type: 'pie',
            name: 'Browser share',
            data: [
                ['Found',   98.0],
                {
                    name: 'Not Found',
                    y: 2.0,
                    sliced: true,
                    selected: true
                }
            ]
        }]
    });
});

$(function () {

Highcharts.theme = {
   colors: ["#82b2e4", "#d40000"],
   chart: {
      backgroundColor: null,
      style: {
         fontFamily: "OpenSansRegular"
      }
   },
   title: {
      style: {
         color: '#4e5b62',
         fontSize: '13px',
         fontWeight: 'normal'
      }
   },
   tooltip: {
      borderWidth: 0
   },
   legend: {
      itemStyle: {
         fontWeight: 'normal',
         fontSize: '13px',
         color: 'white'
      }
   },
   
   plotOptions: {
      series: {
         shadow: true
      },
      candlestick: {
         lineColor: '#404048'
      },
      map: {
         shadow: false
      }
   },

   // Highstock specific
   navigator: {
      xAxis: {
         gridLineColor: '#D0D0D8'
      }
   },
   rangeSelector: {
      buttonTheme: {
         fill: 'white',
         stroke: '#C0C0C8',
         'stroke-width': 1,
         states: {
            select: {
               fill: '#D0D0D8'
            }
         }
      }
   },
   
      
};

Highcharts.setOptions(Highcharts.theme);

    $('#container10').highcharts({
        chart: {
            type: 'pie',
            height: 130,
            marginTop: 0,
            spacingBottom: -30,
            backgroundColor: {
              linearGradient: [0, 0, 500, 500],
              stops: [
                  [0, 'rgba(255, 255, 255, 0)'],
                  [1, 'rgba(240, 240, 255, 0)']
              ]
            },
            
            options3d: {
              enabled: true,
                alpha: 70,
                beta: 0,
            }
        },

        plotOptions: {
                pie: {
                    depth: 20,
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false
                    },
                    showInLegend: false
                }
            },

          title: {
            text: 'Listings Found',
            y: 5
        },

        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },

        series: [{
            type: 'pie',
            name: 'Browser share',
            data: [
                ['Found',   98.0],
                {
                    name: 'Not Found',
                    y: 2.0,
                    sliced: true,
                    selected: true
                }
            ]
        }]
    });
});

$(function () {

Highcharts.theme = {
   colors: ["#82b2e4", "#d40000"],
   chart: {
      backgroundColor: null,
      style: {
         fontFamily: "OpenSansRegular"
      }
   },
   title: {
      style: {
         color: '#4e5b62',
         fontSize: '13px',
         fontWeight: 'normal'
      }
   },
   tooltip: {
      borderWidth: 0
   },
   legend: {
      itemStyle: {
         fontWeight: 'normal',
         fontSize: '13px',
         color: 'white'
      }
   },
   
   plotOptions: {
      series: {
         shadow: true
      },
      candlestick: {
         lineColor: '#404048'
      },
      map: {
         shadow: false
      }
   },

   // Highstock specific
   navigator: {
      xAxis: {
         gridLineColor: '#D0D0D8'
      }
   },
   rangeSelector: {
      buttonTheme: {
         fill: 'white',
         stroke: '#C0C0C8',
         'stroke-width': 1,
         states: {
            select: {
               fill: '#D0D0D8'
            }
         }
      }
   },
   
      
};

Highcharts.setOptions(Highcharts.theme);

    $('#container11').highcharts({
        chart: {
            type: 'pie',
            height: 130,
            marginTop: 0,
            spacingBottom: -30,
            backgroundColor: {
              linearGradient: [0, 0, 500, 500],
              stops: [
                  [0, 'rgba(255, 255, 255, 0)'],
                  [1, 'rgba(240, 240, 255, 0)']
              ]
            },
            
            options3d: {
              enabled: true,
                alpha: 70,
                beta: 0,
            }
        },

        plotOptions: {
                pie: {
                    depth: 20,
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: false
                    },
                    showInLegend: false
                }
            },

          title: {
            text: 'Listings Found',
            y: 5
        },

        tooltip: {
            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
        },

        series: [{
            type: 'pie',
            name: 'Browser share',
            data: [
                ['Found',   98.0],
                {
                    name: 'Not Found',
                    y: 2.0,
                    sliced: true,
                    selected: true
                }
            ]
        }]
    });
});

$(function () {
    $('#container13').highcharts({
        chart: {
            type: 'line',
            height: 285,
            marginTop: 40
        },
        colors: ['#78a7df'],
        title: {
            text: '',
            x: -20 //center
        },
        subtitle: {
            text: '',
            x: -20
        },
        xAxis: {
            categories: ['Sept.', 'Oct.', 'Nov.', 'Dec.']
        },
        yAxis: {
            title: {
                text: ''
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        plotOptions: {
            line: {
                dataLabels: {
                    enabled: true
                },
                enableMouseTracking: false,
                lineWidth: 4
            },
            series: {
                shadow: false
            }
        },
        tooltip: {
            valueSuffix: '°C'
        },
        legend: {
            enabled: false
        },
        series: [{
            name: 'Tokyo',
            data: [45, 63, 71, 82],
            marker: {
                fillColor: '#cc0000',
                width: 10,
                height: 10
            }
        }]
    });
});
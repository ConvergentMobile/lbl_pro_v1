$(function () {
var googleAccuracy=$('#averageAccuracy').val();
var googleInaccurate=100-googleAccuracy;


var datav=[];
datav.push(['Found', parseInt(googleAccuracy)],{name:'Not Found',y:parseInt(googleInaccurate),sliced: true,selected: true});

Highcharts.theme = {
   colors: ["#e8e7e5", "#cc0000"],
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
         color: '#4e5b62'
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
                    showInLegend: true,
                    states: {
                      hover: {
                          brightness: -0.1
                      }
                    }
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
            data: datav
        }]
    });
});


$(function () {
	var googleAccuracy=$('#googleErrors').val();
	var googleInaccurate=100-googleAccuracy;


	var datav=[];
	datav.push(['Found', parseInt(googleAccuracy)],{name:'Not Found',y:parseInt(googleInaccurate),sliced: true,selected: true});
	
Highcharts.theme = {
   colors: ["#e8e7e5", "#cc0000"],
   chart: {
      backgroundColor: null,
      style: {
         fontFamily: "OpenSansRegular",
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
                    showInLegend: false,
                    states: {
                      hover: {
                          brightness: -0.1
                      }
                    }
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
            data:datav
        }]
    });

});

$(function () {
	var googleAccuracy=$('#bingErrors').val();
	var googleInaccurate=100-googleAccuracy;


	var datav=[];
	datav.push(['Found', parseInt(googleAccuracy)],{name:'Not Found',y:parseInt(googleInaccurate),sliced: true,selected: true});
	
Highcharts.theme = {
   colors: ["#e8e7e5", "#cc0000"],
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
                    showInLegend: false,
                    states: {
                      hover: {
                          brightness: -0.1
                      }
                    }
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
            data: datav
        }]
    });
});

$(function () {
	var googleAccuracy=$('#YahooErrors').val();
	var googleInaccurate=100-googleAccuracy;


	var datav=[];
	datav.push(['Found', parseInt(googleAccuracy)],{name:'Not Found',y:parseInt(googleInaccurate),sliced: true,selected: true});
	
Highcharts.theme = {
   colors: ["#e8e7e5", "#cc0000"],
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
                    showInLegend: false,
                    states: {
                      hover: {
                          brightness: -0.1
                      }
                    }
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
           data: datav
        }]
    });
});

$(function () {


	var googleAccuracy=$('#yelpErrors').val();
	var googleInaccurate=100-googleAccuracy;


	var datav=[];
	datav.push(['Found', parseInt(googleAccuracy)],{name:'Not Found',y:parseInt(googleInaccurate),sliced: true,selected: true});
	
Highcharts.theme = {
   colors: ["#e8e7e5", "#cc0000"],
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
                    showInLegend: false,
                    states: {
                      hover: {
                          brightness: -0.1
                      }
                    }
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
            data: datav
        }]
    });
});

$(function () {

	var googleAccuracy=$('#mapquestErrors').val();
	var googleInaccurate=100-googleAccuracy;


	var datav=[];
	datav.push(['Found', parseInt(googleAccuracy)],{name:'Not Found',y:parseInt(googleInaccurate),sliced: true,selected: true});
	
Highcharts.theme = {
   colors: ["#e8e7e5", "#cc0000"],
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
                    showInLegend: false,
                    states: {
                      hover: {
                          brightness: -0.1
                      }
                    }
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
            data: datav
        }]
    });
});

$(function () {

	var googleAccuracy=$('#ypErrors').val();
	var googleInaccurate=100-googleAccuracy;


	var datav=[];
	datav.push(['Found', parseInt(googleAccuracy)],{name:'Not Found',y:parseInt(googleInaccurate),sliced: true,selected: true});
	
Highcharts.theme = {
   colors: ["#e8e7e5", "#cc0000"],
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
                    showInLegend: false,
                    states: {
                      hover: {
                          brightness: -0.1
                      }
                    }
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
            data: datav
        }]
    });
});

$(function () {

	var googleAccuracy=$('#superpagesErrors').val();
	var googleInaccurate=100-googleAccuracy;


	var datav=[];
	datav.push(['Found', parseInt(googleAccuracy)],{name:'Not Found',y:parseInt(googleInaccurate),sliced: true,selected: true});
	
Highcharts.theme = {
   colors: ["#e8e7e5", "#cc0000"],
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
                    showInLegend: false,
                    states: {
                      hover: {
                          brightness: -0.1
                      }
                    }
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
            data: datav
        }]
    });
});

$(function () {

	var googleAccuracy=$('#citysearchErrors').val();
	var googleInaccurate=100-googleAccuracy;


	var datav=[];
	datav.push(['Found', parseInt(googleAccuracy)],{name:'Not Found',y:parseInt(googleInaccurate),sliced: true,selected: true});
	
Highcharts.theme = {
   colors: ["#e8e7e5", "#cc0000"],
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
                    showInLegend: false,
                    states: {
                      hover: {
                          brightness: -0.1
                      }
                    }
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
            data: datav
        }]
    });
});

$(function () {

	var googleAccuracy=$('#yellobookErrors').val();
	var googleInaccurate=100-googleAccuracy;


	var datav=[];
	datav.push(['Found', parseInt(googleAccuracy)],{name:'Not Found',y:parseInt(googleInaccurate),sliced: true,selected: true});
	
Highcharts.theme = {
   colors: ["#e8e7e5", "#cc0000"],
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
                    showInLegend: false,
                    states: {
                      hover: {
                          brightness: -0.1
                      }
                    }
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
            data: datav
        }]
    });
});

$(function () {

	var googleAccuracy=$('#whitepagesErrors').val();
	var googleInaccurate=100-googleAccuracy;


	var datav=[];
	datav.push(['Found', parseInt(googleAccuracy)],{name:'Not Found',y:parseInt(googleInaccurate),sliced: true,selected: true});
	
Highcharts.theme = {
   colors: ["#e8e7e5", "#cc0000"],
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
                    showInLegend: false,
                    states: {
                      hover: {
                          brightness: -0.1
                      }
                    }
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
            data: datav
        }]
    });
});

$(function () {
	var brandname=$('#brandname').val();
	var datav=[];
    // Create the chart
    $('#container13').highcharts({
        chart: {
            type: 'column',
            height: 285,
            marginTop: 30
        },
        colors: ['#d0c8c3', '#cc0000'],
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
    var chart = $('#container13').highcharts();
    $.ajax({
 		type : "get",
 		url : "getTotalListingGraphInfo.htm",
 		cache : false,
 		//dataType: "json",       
 		data:{
 			brandname:brandname
 		},
 		success : function(response) {
 			//alert(response);
 			var graph= $.parseJSON(response);
 			$(graph).each(function(i,client){
 				//var arr=JSON.stringify(client.monthName);
 			
 				datav.push({name: client.monthName, y: client.listingCount});
 			});


 			chart.series[0].setData(datav);
 		}
 	});
});
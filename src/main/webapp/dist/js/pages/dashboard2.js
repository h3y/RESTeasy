$(function () {

  'use strict';

  var color = ["#f56954","#00a65a","#f39c12","#00c0ef","#3c8dbc","#d2d6de","#FFCC00","#6A5ACD","#FFDB8B","#414A4C"];
  /*var randomColorFactor = function() {
    return Math.round(Math.random() * 255);
  };
  var randomColor = function() {
    return 'rgba(' + randomColorFactor() + ',' + randomColorFactor() + ',' + randomColorFactor() + ',' + (1) + ')';
  };*/
  //-------------
  //- PIE CHART -
  //-------------
  // Get context with jQuery - using jQuery's .get() method.
  var PieData = [{
   color:"#f56954",
   highlight:"#f56954",
   label:"222",
   value:"1",
 }];
    $.ajax({
            "type":'GET',
            "url": 'restful/api/featuresgroup/',
            "dataType": 'json',
              success:  function (data)
              {
                  createDataList(data);
              },
              error: function (jqXHR, textStatus, errorThrown)
              {
                  alert('Error get data from ajax');
              }
    });

function createDataList(JsonObject){


  $.each(JsonObject,function(i,val){
    PieData.push({value:val[1],label:val[0],color:color[i],highlight:color[i]});
  });

    var pieChartCanvas = $("#pieChart").get(0).getContext("2d");
  var pieChart = new Chart(pieChartCanvas).Pie(PieData, pieOptions);
  console.log(pieChart.generateLegend());
};


  var pieOptions = {
    //Boolean - Whether we should show a stroke on each segment
    segmentShowStroke: true,
    //String - The colour of each segment stroke
    segmentStrokeColor: "#fff",
    //Number - The width of each segment stroke
    segmentStrokeWidth: 1,
    //Number - The percentage of the chart that we cut out of the middle
    percentageInnerCutout: 50, // This is 0 for Pie charts
    //Number - Amount of animation steps
    animationSteps: 100,
    //String - Animation easing effect
    animationEasing: "easeOutBounce",
    //Boolean - Whether we animate the rotation of the Doughnut
    animateRotate: true,
    //Boolean - Whether we animate scaling the Doughnut from the centre
    animateScale: false,
    //Boolean - whether to make the chart responsive to window resizing
    responsive: true,
    // Boolean - whether to maintain the starting aspect ratio or not when responsive, if set to false, will take up entire container label.toLowerCase()
    maintainAspectRatio: false,
    //String - A legend template
    legendTemplate: "<ul class=\"<%='mylegend'%>-legend\"><%for (var i=0; i<PieData.length; i++){%><li><span style=\"background-color:<%=PieData[i].color%>\"></span><%if(PieData[i].label){%><%=PieData[i].label%><%}%></li><%}%></ul>",
    //String - A tooltip template
    tooltipTemplate: "<%=PieData.length %> <%=label%> users"
  };
  //Create pie or douhnut chart
  // You can switch between pie and douhnut using the method below.
  //-----------------
  //- END PIE CHART -
  //-----------------
});
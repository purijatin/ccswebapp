(function(){
	$(document).ready(function(){
		$("#home").on("click",function(){
			$(this).parent().addClass("active");
			$("#results").parent().removeClass("active");
			$("#results-container").fadeOut("fast",function(){
			$(".jumbotron").fadeIn("normal");
			});
			
		});

		$("#results").on("click",function(){
			$(this).parent().addClass("active");
			$("#home").parent().removeClass("active");
			$.get("assets/json/results.json").done(function(data){
					console.log(data.options);var votes=[],names=[];
					for(var i=0;i<data.options.length;i++){
						console.log(data.options[i].votes);
						console.log(data.options[i].option);
						votes[i]=data.options[i].votes;
						names[i]=data.options[i].option;
					}
					/*var chart = d3.select(container).append("div").attr("class", "chart");
					chart.selectAll("div").data(options).enter().append("div").style("width", function(d) { return d * 10 + "px"; }).text(function(d) { return d; });
					var container=$("#results-container");
					container;*/
					$(".jumbotron").fadeOut("fast",function() {
						$("#results-container").fadeIn("normal");
					});
					
					$("#step-4").html('');
      var chart,
      width = 400,
      bar_height = 20,
      height = bar_height * names.length;
 
  /* step 1 */
  chart = d3.select($("#step-1")[0])
    .append('svg')
    .attr('class', 'chart')
    .attr('width', width)
    .attr('height', height);
 
  /* step 2 */
  var x, y;
 
  chart = d3.select($("#step-2")[0])
    .append('svg')
    .attr('class', 'chart')
    .attr('width', width)
    .attr('height', height);
 
  x = d3.scale.linear()
     .domain([0, d3.max(votes)])
     .range([0, width]);
 
  y = d3.scale.ordinal()
    .domain(votes)
    .rangeBands([0, height]);
 
  chart.selectAll("rect")
     .data(votes)
     .enter().append("rect")
     .attr("x", 0)
     .attr("y", y)
     .attr("width", x)
     .attr("height", bar_height);
 
  /* step 3 */
  chart = d3.select($("#step-3")[0])
    .append('svg')
    .attr('class', 'chart')
    .attr('width', width)
    .attr('height', height);
 
  chart.selectAll("rect")
    .data(votes)
    .enter().append("rect")
    .attr("x", 0)
    .attr("y", y)
    .attr("width", x)
    .attr("height", y.rangeBand());
 
  chart.selectAll("text")
    .data(votes)
    .enter().append("text")
    .attr("x", x)
    .attr("y", function(d){ return y(d) + y.rangeBand()/2; } )
    .attr("dx", -5)
    .attr("dy", ".36em")
    .attr("text-anchor", "end")
    .text(String);
 
  /* step 4 */
  var left_width = 100;
 
  chart = d3.select($("#step-4")[0])
    .append('svg')
    .attr('class', 'chart')
    .attr('width', left_width + width)
    .attr('height', height);
 
  chart.selectAll("rect")
    .data(votes)
    .enter().append("rect")
    .attr("x", left_width)
    .attr("y", y)
    .attr("width", x)
    .attr("height", y.rangeBand());
 
  chart.selectAll("text.score")
    .data(votes)
    .enter().append("text")
    .attr("x", function(d) { return x(d) + left_width; })
    .attr("y", function(d){ return y(d) + y.rangeBand()/2; } )
    .attr("dx", -5)
    .attr("dy", ".36em")
    .attr("text-anchor", "end")
    .attr('class', 'score')
    .text(String);
 
  chart.selectAll("text.name")
    .data(names)
    .enter().append("text")
    .attr("x", left_width / 2)
    .attr("y", function(d){ return y(d) + y.rangeBand()/2; } )
    .attr("dy", ".36em")
    .attr("text-anchor", "middle")
    .attr('class', 'name')
    .text(String);
 
  /* step 5 */
  var gap = 2;
  // redefine y for adjusting the gap
  y = d3.scale.ordinal()
    .domain(votes)
    .rangeBands([0, (bar_height + 2 * gap) * names.length]);
 
 
  chart = d3.select($("#step-5")[0])
    .append('svg')
    .attr('class', 'chart')
    .attr('width', left_width + width + 40)
    .attr('height', (bar_height + gap * 2) * names.length + 30)
    .append("g")
    .attr("transform", "translate(10, 20)");
 
  chart.selectAll("line")
    .data(x.ticks(d3.max(votes)))
    .enter().append("line")
    .attr("x1", function(d) { return x(d) + left_width; })
    .attr("x2", function(d) { return x(d) + left_width; })
    .attr("y1", 0)
    .attr("y2", (bar_height + gap * 2) * names.length);
 
  chart.selectAll(".rule")
    .data(x.ticks(d3.max(votes)))
    .enter().append("text")
    .attr("class", "rule")
    .attr("x", function(d) { return x(d) + left_width; })
    .attr("y", 0)
    .attr("dy", -6)
    .attr("text-anchor", "middle")
    .attr("font-size", 10)
    .text(String);
 
  chart.selectAll("rect")
    .data(votes)
    .enter().append("rect")
    .attr("x", left_width)
    .attr("y", function(d) { return y(d) + gap; })
    .attr("width", x)
    .attr("height", bar_height);
 
  chart.selectAll("text.score")
    .data(votes)
    .enter().append("text")
    .attr("x", function(d) { return x(d) + left_width; })
    .attr("y", function(d, i){ return y(d) + y.rangeBand()/2; } )
    .attr("dx", -5)
    .attr("dy", ".36em")
    .attr("text-anchor", "end")
    .attr('class', 'score')
    .text(String);
 
  chart.selectAll("text.name")
    .data(names)
    .enter().append("text")
    .attr("x", left_width / 2)
    .attr("y", function(d, i){ return y(d) + y.rangeBand()/2; } )
    .attr("dy", ".36em")
    .attr("text-anchor", "middle")
    .attr('class', 'name')
    .text(String);

				});
		});//click

	function chart(){

	}
	});//ready

})();
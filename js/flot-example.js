/*
	flot-example.js
*/

$(document).ready(init);

function init() {
	// example 1 - basic line graphs
	
	var json = '{"q":"What is your name?//sample question","options":[{"option":"Jatin","votes":"30"},{"option":"Nehil","votes":"70"}]}';
	
	
	// example 2 - basic bar graph
	$.plot(
		$("#flot-example-2"),
		[
			{
				label: "Total Things Per Year",
				data: [ [2011, 450], [2012, 550], [2013, 320], [2014, 700] ],
				bars: {
					show: true,
					barWidth: 0.5,
					align: "center"
				}	
			}
		],
		{
			xaxis: {
				ticks: [
					[2011, "2011"],
					[2012, "2012"],
					[2013, "2013"],
					[2014, "2014"]
				]
			}	
		}
	);
	
	
}

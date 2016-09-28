// When the page loads inject the graph and start updating it.
$(function() {
  graph.inject();
  uiHelper.decorate();
  uiHelper.start();
});

//Represents a time series graph that is capable of updating itself with new data.
var Graph = function() {

  var graph, totalDurationToGraphInSeconds = 120;

  return {
     // @returns {number} the total duration of time, in seconds, this graph will display.
    getTotalDurationToGraphInSeconds : function() {
      return totalDurationToGraphInSeconds;
    },
    
    // Creates the graph and injects in into the element with id="graph".
    inject : function() {
      graph = $.plot("#graph", {},
          {
            // Define the colors and y-axis margins for the graph.
            grid : {
              borderWidth : 1,
              minBorderMargin : 20,
              labelMargin : 10,
              backgroundColor : {
                colors : [ "#fff", "#E6F1FF" ]
              },
              margin : {
                top : 8,
                bottom : 20,
                left : 20
              },
            },
            // turn off shadows and make sure the lines and points display
            series : {
              shadowSize : 0,
              lines: { show: true },
              points: { show: true }
            },
            // Set up the y-axis to initially show 0-10. This is dynamically adjusted as data is updated.
            yaxis : {
              min : 0,
              max : 10
            },
            // The x-axis is time-based. The browser's timezone will be
            // used to interpret timestamps. The range is dynamically adjusted as data is updated.
            xaxis : {
              mode : "time",
              timezone : "browser",
              timeformat : "%M:%S",
              min : (new Date()).getTime()
                  - (totalDurationToGraphInSeconds * 1000),
              max : (new Date()).getTime()
            },
            // Show the legend of hashtags in the upper left corner of the graph.
            legend : {
              show : true,
              position : "nw"
            }
          });

      // Create y-axis label and inject it into the graph container
      var yaxisLabel = $("<div class='axisLabel yaxisLabel'></div>").text(
          "Hashtag popularity").appendTo("#graph");
      // Center the y-axis along the left side of the graph
      yaxisLabel.css("margin-top", yaxisLabel.width() / 2 - 20);
    },


     // Update the graph to use the data provided. 
     // @param {Object}
     //         flotData Flot formatted data object that should include at a
     //         minimum series labels and their data in the format: { label: "my
     //         series", data: [[0, 10], [1, 100]] }
    update : function(flotData) {
      graph.setData(flotData);

      // Calculate min and max value to update y-axis range.
      var getValue = function(tuple) {
        // Flot data values are stored as the second element of each data array
        return tuple[1];
      };
      var max = Number.MIN_VALUE;
      flotData.forEach(function(d) {
        m = Math.max.apply(Math, d.data.map(getValue));
        max = Math.max(m, max);
      });
      var min = Number.MAX_VALUE;
      flotData.forEach(function(d) {
        m = Math.min.apply(Math, d.data.map(getValue));
        min = Math.min(m, min);
      });

      // Adjust the y-axis for min/max of our new data
      graph.getOptions().yaxes[0].max = Math.min(max, max)
      graph.getOptions().yaxes[0].min = min

      // Adjust the x-axis to move in real time and show at most the total
      // duration to graph as configured above
      graph.getOptions().xaxes[0].min = (new Date()).getTime()
          - (totalDurationToGraphInSeconds * 1000),
          graph.getOptions().xaxes[0].max = (new Date()).getTime()

      // Redraw the graph data and axes
      graph.draw();
      graph.setupGrid();
    }
  }
}

//Methods used to manipulate visible elements of the page.
var UIHelper = function(data, graph) {
  // How frequently should we poll for new data and update the graph?
  var updateIntervalInMillis = 500;
  // How often should the top N display be updated?
  var intervalsPerTopNUpdate = 5;
  // How far back should we fetch data at every interval?
  var rangeOfDataToFetchEveryIntervalInSeconds = 2;
  // What should N be for our Top N display?
  var topNToCalculate = 3;
  // Keep track of when we last updated the top N display.
  var topNIntervalCounter = 1;
  // Controls the update loop.
  var running = true;

 
   // Fetch counts from server
   // @param {number} secondsAgo The range in seconds since now to fetch counts for.
   // @param {function} callback The function to invoke when data has been updated.
  var updateData = function(secondsAgo, callback) {
	  
    // Fetch data from our data provider
    provider.getData(secondsAgo, function(newData) {
      // Store the data locally
      data.addNewData(newData);
      // Remove data that's outside the window of data we are displaying. This
      // is unnecessary to keep around.
      //data.removeDataOlderThan((new Date()).getTime()
      //        - (graph.getTotalDurationToGraphInSeconds() * 1000));

      if (callback) {
        callback();
      }
    });
  }

   // Update the top N display.
  var updateTopN = function() {
    var topN = data.getTopN(topNToCalculate);

    var table = $("<table/>").addClass("topN");
    $.each(topN, function(_, v) {
      console.loog
      var row = $("<tr/>");
      row.append($("<td/>").addClass('referrerColumn').text(v.hashtag));
      row.append($("<td/>").addClass('countColumn').text(v.count));
      table.append(row);
    });

    $("#topN").html(table);
  }

  // Update the graph with new data.
  var update = function() {
    // Update our local data
    updateData(rangeOfDataToFetchEveryIntervalInSeconds);
    // Update top N every intervalsPerTopNUpdate intervals
    if (topNIntervalCounter++ % intervalsPerTopNUpdate == 0) {
      updateTopN(data);
      topNIntervalCounter = 1;
    }

    // Update the graph with our new data, transformed into the data series format Flot expects
    graph.update(data.toFlotData());

    // If its still running schedule this method to be executed again at the next interval
    if (running) {
      setTimeout(arguments.callee, updateIntervalInMillis);
    }
  }

   // Set the page description header.
   // @param {string} desc Page description.
  var setDescription = function(desc) {
    $("#description").text(desc);
  }

  return {
     // Decorate the page. This will update various UI elements.
    decorate : function() {
      setDescription("This graph displays the last "
          + graph.getTotalDurationToGraphInSeconds()
          + " seconds of counts as calculated by the Hashtag Popularity Snapshot App.");
      $("#topNDescription").text(
          "Top " + topNToCalculate + " hashtags by counts (Updated every "
              + (intervalsPerTopNUpdate * updateIntervalInMillis) + "ms):");
    },

     // Starts updating the graph at our defined interval.
    start : function() {
      setDescription("Loading data...");
      var _this = this;
      // Load an initial range of data, decorate the page, and start the update polling process.
      updateData(rangeOfDataToFetchEveryIntervalInSeconds,
          function() {
            // Decorate again now that we're done with the initial load
            _this.decorate();
            // Start our polling update
            running = true;
            update();
          });
    },

    // Stop updating the graph.
    stop : function() {
      running = false;
    }
  }
};

 // Provides easy access to hashtag count data.
var CountDataProvider = function() {
  var _endpoint = "http://" + location.host + "/hashtag-snapshot/GetCounts";

   // Builds a URL
  buildUrl = function() {
    return _endpoint;
  };

  return {
     // Set the endpoint to request counts with.
    setEndpoint : function(endpoint) {
      _endpoint = endpoint;
    },

     // Requests new data and passes it to the callback provided. The data is
     // expected to be returned in the following format. 
     // [{
     //   "timestamp" : 1397156430562,
     //   "hashtagCounts" : [
     //     {"hashtag":"hashtag","count":1002},
     //     {"hashtag":"hashtag","count":901}
     //   ]
     // }]
     // @param {function} callback The function to call when data has been returned from the endpoint.
    getData : function(secodsAgo, callback) {
      $.ajax({
        url : buildUrl()
      }).done(callback);
    }
  }
}

 // Internal representation of count data. The data is stored in an associative
 // array by timestamp. The internal representation is then transformed
 // to what Flot expects with toFlotData().
var CountData = function() {
  // Data format:
  // {
  //   "hashtag" : {
  //     "label" : "hashtag",
  //     "data" : {
  //       "1396559634129" : 150
  //     }
  //   }
  // }
  var data = {};

  // Totals format:
  // {
  //   "hashtag" : 102333
  // }
  var totals = {};

   // Update the total count for a given hashtag.
   // @param {string} hashtag Hashtag to update the total for.
  var updateTotal = function(hashtag) {
    //loop through all the counts and sum them if there is data for this hashtag
    if (data[hashtag]) {
      totals[hashtag] = 0;
      $.each(data[hashtag].data, function(ts, count) {
        totals[hashtag] += count;
      });
    } else {
      // No data for the hashtag, remove the total if it exists
      delete totals[hashtag];
    }
  }

  return {
     // @returns {object} The internal representation of hashtag data.
    getData : function() {
      return data;
    },

     // @returns {object} An associative array of hashtags to their total counts.
    getTotals : function() {
      return totals;
    },

     // Compute local top N using the entire range of data we currently have.
     // @param {number} n The number of top hashtags to calculate.
     // @returns {object[]} The top hashtags by count in descending order.
    getTopN : function(n) {
      // Create an array out of the totals so we can sort it
      var totalsAsArray = $.map(totals, function(count, hashtag) {
        return {
          'hashtag' : hashtag,
          'count' : count
        };
      });
      // Sort descending by count
      var sorted = totalsAsArray.sort(function(a, b) {
        return b.count - a.count;
      });
      // Return the first N
      return sorted.slice(0, Math.min(n, sorted.length));
    },

     // Merges new count data in to our existing data set.
     // @param {object} Count data returned by our data provider.
    addNewData : function(newCountData) {
      // Expected data format:
      // [{
      //   "timestamp" : 1397156430562,
      //   "hashtagCounts" : [{"hashtag":"hashtag","count":1002}]
      // }]
      newCountData.forEach(function(count) {
        // Add individual hastag counts
        count.hashtagCounts.forEach(function(tagCount) {
          // Reuse or create a new data series entry for this hashtag
          tagData = data[tagCount.hashtag] || {
            label : tagCount.hashtag,
            data : {}
          };
          // Set the count
          tagData.data[count.timestamp] = tagCount.count;
          // Update the hashtag data
          data[tagCount.hashtag] = tagData;
          // Update our totals whenever new data is added
          updateTotal(tagCount.hashtag);
        });
      });
    },

     // Removes data older than a specific time. This will also prune hashtags
     // that have no data points.
     // @param {number} timestamp Any data older than this time will be removed.
    removeDataOlderThan : function(timestamp) {
      // For each hashtag
      $.each(data, function(hashtag, hashtagData) {
        var shouldUpdateTotals = false;
        // For each data point
        $.each(hashtagData.data, function(ts, count) {
          // If the data point is older than the provided time
          if (ts < timestamp) {
            // Remove the timestamp from the data
            delete hashtagData.data[ts];
            // Indicate we need to update the totals for the hashtag since we
            // removed data
            shouldUpdateTotals = true;
            // If the hashtag has no more data remove the hashtag entirely
            if (Object.keys(hashtagData.data).length == 0) {
              // Remove the empty hashtag - it has no more data
              delete data[hashtag];
            }
          }
        });
        if (shouldUpdateTotals) {
          // Update the totals if we removed any data
          updateTotal(hashtag);
        }
      });
    },

     // Convert our internal data to a Flot data object.
     // @returns {object[]} Array of data series for every hashtag we know of.
    toFlotData : function() {
      flotData = [];
      $.each(data, function(hashtag, hashtagData) {
        flotData.push({
          label : hashtag,
          // Flot expects time series data to be in the format:
          // [[timestamp as number, value]]
          data : $.map(hashtagData.data, function(count, ts) {
            return [ [ parseInt(ts) * 1000, count ] ];
          })
        });
      });
      return flotData;
    }
  }
}

var data = new CountData();
var provider = new CountDataProvider();
var graph = new Graph();
var uiHelper = new UIHelper(data, graph);

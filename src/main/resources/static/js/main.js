/**
 * Created by alexey.ivlev on 30.09.15.
 */

function setupFriendsList(){
    $('li.parent').addClass('plusimageapply');
    $('li.parent').children('.child').addClass('selectedimage');
    $('li.parent').children('.child').hide();
    $('li.parent').each(
        function(column) {
            $(this).click(function(event){
                if (this == event.target) {
                    if($(this).is('.plusimageapply')) {
                        $(this).children('.child').show();
                        $(this).removeClass('plusimageapply');
                        $(this).addClass('minusimageapply');
                    }
                    else
                    {
                        $(this).children('.child').hide();
                        $(this).removeClass('minusimageapply');
                        $(this).addClass('plusimageapply');
                    }
                }
            });
        }
    );

    sigma.classes.graph.addMethod('neighbors', function(nodeId) {
        var k,
            neighbors = {},
            index = this.allNeighborsIndex[nodeId] || {};

        for (k in index)
            neighbors[k] = this.nodesIndex[k];

        return neighbors;
    });


    sigma.parsers.json(
        'http://localhost:9191/js/data.json',
        {
            container: 'container',
            settings: {
                defaultNodeColor: '#ec5148'
            }
        },
        function(s) {
            // We first need to save the original colors of our
            // nodes and edges, like this:
            s.graph.nodes().forEach(function(n) {
                n.originalColor = n.color;
            });
            s.graph.edges().forEach(function(e) {
                e.originalColor = e.color;
            });

            // When a node is clicked, we check for each node
            // if it is a neighbor of the clicked one. If not,
            // we set its color as grey, and else, it takes its
            // original color.
            // We do the same for the edges, and we only keep
            // edges that have both extremities colored.
            s.bind('clickNode', function(e) {
                var nodeId = e.data.node.id,
                    toKeep = s.graph.neighbors(nodeId);
                toKeep[nodeId] = e.data.node;

                s.graph.nodes().forEach(function(n) {
                    if (toKeep[n.id])
                        n.color = n.originalColor;
                    else
                        n.color = '#eee';
                });

                s.graph.edges().forEach(function(e) {
                    if (toKeep[e.source] && toKeep[e.target])
                        e.color = e.originalColor;
                    else
                        e.color = '#eee';
                });

                // Since the data has been modified, we need to
                // call the refresh method to make the colors
                // update effective.
                s.refresh();

                getTags(e.data.node.screenName)
            });

            // When the stage is clicked, we just color each
            // node and edge with its original color.
            s.bind('clickStage', function(e) {
                s.graph.nodes().forEach(function(n) {
                    n.color = n.originalColor;
                });

                s.graph.edges().forEach(function(e) {
                    e.color = e.originalColor;
                });

                // Same as in the previous event:
                s.refresh();
            });
        });
}

function getTags(screenName){
    $.getJSON('/getTags/' + screenName)
        .done(
        function onsuccess(result, status, jqhxr){
            console.log(jqhxr)
            var tweets = $.parseJSON(result.tweets);
            $('#tags > ul').empty();
            for(var i = 0; i < tweets.length; i++){
                var size = parseInt(tweets[i].retweetCount) / 10  + 5;
                $('#tags > ul').append('<li style="font-size:' + size + 'ex"><a href="#">' + tweets[i].tags + '</a> </li>')
            }
            try {
                TagCanvas.Start('myCanvas','tags',{
                    textColour: '#ff0000',
                    outlineColour: '#ff00ff',
                    reverse: true,
                    depth: 0.8,
                    maxSpeed: 0.05,
                    weight: true,
                    weightMode: 'both'

                });
            } catch(e) {
                // something went wrong, hide the canvas container
                $('#canvasContainer').hide();
                alert("Can't get tags for user - \"" + screenName + "\"")
            }
        })
        .fail(function onerror(jqhxr, status, error){
            console.log("Request Failed: " + jqhxr.status + " - " + jqhxr.statusText);
            alert("Request Failed: " + jqhxr.status + " - " + jqhxr.statusText)
        });
}

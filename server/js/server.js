// Appetite Service that runs on node.js

// -- Load up the libraries
var sys   = require("sys"),
   http   = require("http"),
   posix  = require("posix"),
   apps   = require("./apps").apps,
   client = require("./client");

// -- Create a proxy to the Appetite data
var a = new client.Appetite(apps);

// -- The responders handle the various URLs
var responders = {
    // /apps?type=top_rated&by_category=[true|false]&start=[1+]&size=[1+]&channel=[w][b][c]&query=foo
    apps: function(params) {
        var opts = {};
        if (params.type)  opts.type  = params.type;
        if (params.size)  opts.size  = parseInt(params.size);
        if (params.start) opts.start = parseInt(params.start);
        if (params.query) opts.query = params.query;
        
        return {
            body: a.find(opts)
        }  
    },
    // /icons?count=[1+]&uniqueIcons=[true|false]
    icons: function(params) {      
        return {
          body: a.icons(parseInt(params.count))
        };
    },
    // /app?id=[id]&locale=*
    app: function(params) {
        if (!params.id) {
            return {
                error: 501,
                body: 'I need an id!' 
            };
        }

        return {
            body: a.app(params.id)
        }
    }  
};

// -- Create the HTTP server binding
var port = process.ENV['APPETITE_PORT'] || 8000;
var host = process.ENV['APPETITE_HOST'] || 'localhost';

http.createServer(function(request, response) {
    var path = request.uri.path.substring(1);
    var output;
    
    if (typeof responders[path] != "function") {
        output = {
            error: 500,
            body: "Unable to respond to the given path: " + path
        }
    } else {
        output = responders[path](request.uri.params);
    }

    if (output.error) {
        response.sendHeader(output.error, {"Content-Type": output.contentType || "text/javascript"});
        response.sendBody(output.body);
    } else {
        response.sendHeader(200, {"Content-Type": output.contentType || "text/javascript"});
        response.sendBody(JSON.stringify(output.body));
    }
    response.finish();
}).listen(port, host);

sys.puts("Appetite Server is Listening to " + host + ":" + port);

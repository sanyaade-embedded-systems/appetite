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
    // /apps?type=top_rated&by_category=[true|false]&start=[1+]&count=[1+]&channel=[w][b][c]&query=foo
    apps: function(params) {
        var opts = {};
        if (params.type)  opts.type  = params.type;
        if (params.count)  opts.count  = parseInt(params.count);
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
    },
    DEFAULT: function(params) {
        return {
            body: "<html><head><title>Project Appetite API</title></head><body><h1>Project Appetite API Sample</h1>Thanks for playing with the Project Appetite API page. There are the following endpoints that you can access to get JSON responses containing apps from the app catalog backend:<h3>Querying apps</h3> <code>/apps?type=top_rated|top_paid|top_free|top_overall|top_grossing|newest&start=1+&count=1+&channel=[b][c][w]&query=query</code><br><br> This API is used to query the backend and filter down to the apps that you with to see. The full options are:<br><ul><li>type: this orders the results based on the type that you are looking for (default: alphabetical on app title)<li>start: where to start in the data set (default: 1)<li>count: how many apps to return in the data set<li>channel: whether to filter on channel. b=beta, c=palm app catalog, and w=web distro (and NOT in the others) (default: 'bcw' ... all of them)<li>query: a simple substring filter on the title or description</ul><h3>Retrieving an app by id</h3> <code>/app?id=guid</code>: This API is used if you know the guid for a given application and you want to get its data (e.g. you are displaying a page for one app).<h3>Icons</h3> <code>/icons?count=#</code>: Return a set of icons (optional count for sizing)",
            contentType: "text/html",
            sendRaw: true
        }
    }
};


// -- Create the HTTP server binding
var host = process.ENV['APPETITE_HOST'] || 'localhost';
var port = process.ENV['APPETITE_PORT'] || 8000;

if (process.ARGV.length > 3) { // overwrite with command line args
    port = process.ARGV[3];
}
if (process.ARGV.length > 2) {
    host = process.ARGV[2];
}

http.createServer(function(request, response) {
    var path = request.uri.path.substring(1);
    var output;

    var responder = (typeof responders[path] == "function") ? responders[path] : responders['DEFAULT'];
    output = responder(request.uri.params);

    if (output.error) {
        response.sendHeader(output.error, {"Content-Type": output.contentType || "text/javascript"});
        response.sendBody(output.body);
    } else {
        var body = (output.sendRaw) ? output.body : JSON.stringify(output.body);
        response.sendHeader(200, {"Content-Type": output.contentType || "text/javascript"});
        response.sendBody(body);
    }
    response.finish();
}).listen(port, host);

sys.puts("Appetite Server is Listening to " + host + ":" + port);

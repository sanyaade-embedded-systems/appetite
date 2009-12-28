// AppCake Service 

var sys = require("sys"),
   http = require("http"),
   posix = require("posix"),
   data = require("./data");

http.createServer(function(request, response) {
    response.sendHeader(200, {'Content-Type': 'text/javascript'});
    response.sendBody(JSON.stringify(data));
    response.finish();
}).listen(8000);

sys.puts("Server running at http://127.0.0.1:8000/");

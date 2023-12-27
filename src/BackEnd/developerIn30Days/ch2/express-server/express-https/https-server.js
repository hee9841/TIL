var express = require("express");
var fs = require("fs");
var https = require("https");
var app = express();


app.get("/", function (req, res) {
    res.send("Hello Wold!");
});

https
    .createServer(
        {
            key: fs.readFileSync('key파일위치'),
            cert: fs.readFileSync('cert파일위치'),
            ca: fs.readFileSync('ca파일위치'),
        },
        app
    )
    .listen(3000, function () {
        console.log(
            "Example app listening on port 3000! Go to https://localhost:3000/"
        );
    });

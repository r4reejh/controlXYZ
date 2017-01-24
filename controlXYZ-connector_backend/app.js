var express = require("express");
var serialport=require("serialport");
var port = new serialport("COM3", {
	baudRate: 9600,
    dataBits: 8,
    parity: 'none',
    stopBits: 1,
    flowControl: false,
    parser: serialport.parsers.readline("\r\n")
});

var app = express();

app.get("/:id", function(req, res) {
	var dat=req.params.id;
	dat.toString();
	sendDat(dat);
    res.send(req.params.id);
});

function sendDat(i){
	console.log("inside function");
	console.log(i);
	port.write(i);
}

app.listen(3000);

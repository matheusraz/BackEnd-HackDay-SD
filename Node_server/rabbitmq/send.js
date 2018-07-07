#!/usr/bin/env node

const amqp = require('amqplib/callback_api');


setInterval(function(){
    amqp.connect('amqp://172.17.0.2', function(err, conn) {
        conn.createChannel(function(err, ch) {
          var q = 'hello';
          var msg = 'Hello World!';
      
          ch.assertQueue(q, {durable: false});
          // Note: on Node 6 Buffer.from(msg) should be used
          ch.sendToQueue(q, new Buffer(msg));
          console.log(" [x] Sent %s", msg);
        });
      });

      console.log("mandei!")
},10*1000)
#!/usr/bin/env node

const amqp = require('amqplib/callback_api');
const fs = require('fs');

const start = (kompany) => {
  amqp.connect('amqp://172.17.0.2', function(err, conn) {
      conn.createChannel(function(err, ch) {
        var q = 'entry';
        var msg = '';
        
        const busRaw = fs.readFileSync('final.json');
        const bus = JSON.parse(busRaw);

        //console.log(bus);

         bus.forEach(element => {
           let now = JSON.parse(element);
           if(now.Matricula == kompany) {
              msg = JSON.stringify(now);
              ch.assertQueue(q, {durable: false});
              // Note: on Node 6 Buffer.from(msg) should be used
              ch.sendToQueue(q, new Buffer(msg));
              console.log(" [x] Sent %s", msg);
           }
         });
      });
    });

    return {status: 200, message: 'ônibus identificados e infos enviadas para o Esper.'};
}

exports.start = start;
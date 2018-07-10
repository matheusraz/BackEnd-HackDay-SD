#!/usr/bin/env node

const amqp = require('amqplib/callback_api');
const elasticdb = require('../elasticdb/elasticdb');
const fs = require('fs');

const start = () => {
  amqp.connect('amqp://172.17.0.2', function(err, conn) {
      conn.createChannel(function(err, ch) {
        var q = 'entry';
        var msg = '';

        // const busRaw = fs.readFileSync('final.json');
        // const bus = JSON.parse(busRaw);
        
        elasticdb.getAllContent().then((result) => {
          let bus = result.hits.hits;
          let ind = 0;
          setInterval (() => {
            let data = bus[ind]._source;
            //console.log(data);
            msg = JSON.stringify(data);
            ch.assertQueue(q, {durable: false});
            // Note: on Node 6 Buffer.from(msg) should be used
            ch.sendToQueue(q, new Buffer(msg));
            ind++;
          }, 5*1000);
        });

      });
    });

    return {status: 200, message: 'ônibus identificados e infos enviadas para o Esper.'};
}

exports.start = start;

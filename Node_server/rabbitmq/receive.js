#!/usr/bin/env node

const amqp = require('amqplib/callback_api');

const start = () => {
    amqp.connect('amqp://172.17.0.2', function(err, conn) {
    conn.createChannel(function(err, ch) {
      var q = 'data';

      ch.assertQueue(q, {durable: false});
      console.log(" [*] Waiting for messages in %s. To exit press CTRL+C", q);
      ch.consume(q, function(msg) {
        let result = msg.content.toString();
        console.log(" [x] Received %s", result);
        return result;
      }, {noAck: true});
    });
  });
}

exports.start = start;
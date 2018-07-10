#!/usr/bin/env node

const amqp = require('amqplib/callback_api');

exports.send = send = (company, matricula) => {
    amqp.connect('amqp://172.17.0.2', (err ,con) => {
        con.createChannel((err, ch) => {
            let q = 'params';
            let msg = '';

            let obj = JSON.stringify({
                Company: company,
                Matricula: matricula
            });

            ch.assertQueue(q, {durable:false});
            ch.sendToQueue(q, new Buffer(obj));
        });
    });
}
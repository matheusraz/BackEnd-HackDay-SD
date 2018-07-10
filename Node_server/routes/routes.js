const rmqSend = require('../rabbitmq/sendToEsper');
const elasticdb = require('../elasticdb/elasticdb');
const rmqRec = require('../rabbitmq/receive');

module.exports = serverRouter = (server) => {
    server.get('/bancoEsper', (req, res) => {
        let result = rmqSend.start();
        res.json(result);
    });

    server.get('/getAllCompanies', (req, res) => {
        elasticdb.getAllContent().then((result) => {res.json(result)});
    });

    server.get('/getResponseEsper', (req, res) => {
        let result = rmqRec.start();
        res.json(result);
    });
}
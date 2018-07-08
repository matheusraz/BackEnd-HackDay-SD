const rmqSend = require('../rabbitmq/send')

module.exports = serverRouter = (server) => {
    server.get('/getLocation/:kompany', (req, res) => {
        let result = rmqSend.start(req.params.kompany);
        res.json(result);
    });
}
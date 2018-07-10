const express = require('express');
const bodyparser = require('body-parser');
const server = express();
const port = process.env.PORT || 8080;
const rmqReceive = require('./rabbitmq/receive');
const routes = require('./routes/routes');
const elasticdb = require('./elasticdb/elasticdb');

server.use(bodyparser.json());
server.use(bodyparser.urlencoded({extended:true}));

routes(server);

server.listen(port, (err) => {
    if(err){
        console.log('Erro ao subir servidor');
    } else {
        console.log(`Server escutando na porta ${port}`);
        elasticdb.carga();
        rmqReceive.start();
    }
});
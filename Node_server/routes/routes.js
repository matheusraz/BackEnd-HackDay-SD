const rmqSend = require('../rabbitmq/sendToEsperDB');
const elasticdb = require('../elasticdb/elasticdb');
const rmqRec = require('../rabbitmq/receive');
const rmqSP = require('../rabbitmq/sendParamsToEsper');

module.exports = serverRouter = (server) => {
    server.get('/bancoEsper', (req, res) => {
        let result = rmqSend.start();
        res.json(result);
    });

    server.get('/getAllCompanies', (req, res) => {

        const verif = (array,item) => {
            let ind = 0;
            while(ind < array.length){
                if(array[ind] == item) {
                    return true;
                }
                ind++;
            }
            return false;
        }

        elasticdb.getAllContent().then((result) => {
            let bus = result.hits.hits;
            let ind = 0;
            let array = [];
            while(bus[ind] != undefined) {
                let data = bus[ind]._source;
                if(verif(array, data.Nome) == false) {
                    array.push(data.Nome);
                }
                ind++;
            }
            res.send(array);
        });
    });

    server.get('/getAllMats/:company', (req, res) => {

        const verif = (array,item) => {
            let ind = 0;
            while(ind < array.length){
                if(array[ind] == item) {
                    return true;
                }
                ind++;
            }
            return false;
        }

        elasticdb.getAllContent().then((result) => {
            let bus = result.hits.hits;
            let ind = 0;
            let array = [];
            while(bus[ind] != undefined) {
                let data = bus[ind]._source;
                if(data.Nome = req.params.company && verif(array, data.Matricula) == false) {
                    array.push(data.Matricula);
                }
                ind++;
            }
            res.send(array);
        });
    });

    server.get('/getResponseEsper', (req, res) => {
        let result = rmqRec.start();
        res.json(result);
    });

    server.get('/sendParams/:company/:matricula', (req, res) => {
        rmqSP.send(req.params.company, req.params.matricula);
        res.send('deu');
    });
}
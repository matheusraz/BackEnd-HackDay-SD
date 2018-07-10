package cin.ufpe.rabbitmq;

import cin.ufpe.if998.BusTrackerEvent;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.rabbitmq.client.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class BusTrackerEventRMQ {

    private final static String QUEUE_NAME = "entry";

    public static void main(String args[]) throws Exception {

        String reciveCompany;
        long reciveMatricula;
        EPServiceProvider engine = EPServiceProviderManager.getDefaultProvider();
        engine.getEPAdministrator().getConfiguration().addEventType(BusTrackerEvent.class);

        //selecionar os nomes das empresas
        //String eplc = "select nome from BusTrackerEvent";
        //selecionar os onibus das empresas

        reciveCompany = "E1";
        reciveMatricula = 1626;

        //consultas epl
        //String eplb = "select matricula from BusTrackerEvent where nome="+reciveCompany;

        String epla = String.format("select timestamp,latitude,longitude from BusTrackerEvent#time( 10 sec) where matricula=%d and nome='%s'", reciveMatricula,reciveCompany);

        //tratamento
        EPStatement statement = engine.getEPAdministrator().createEPL(epla);

        statement.addListener((newData, oldData) -> {
            String timestamp = (String) newData[0].get("timestamp");
            double latitude = (double) newData[0].get("latitude");
            double longitude = (double) newData[0].get("longitude");
            System.out.println(String.format("O trace é: %s, com a latitude: %f e com a longitude: %f.",timestamp,latitude,longitude));
            //System.out.println("A temperatura media nos ultimos 100 eventos é: "+ temperatureMedia+" °C.\n");

        });

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("172.17.0.2");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                JSONObject json = null;
                try {
                    json = new JSONObject(message);
                    //System.out.println(json);
                    BusTrackerEvent bus = new BusTrackerEvent(Long.parseLong(String.valueOf(json.get("Unidade"))),
                            (String) json.get("Nome"),
                            Integer.parseInt(String.valueOf(json.get("Matricula"))),
                            (String) json.get("Instante"),
                            Double.parseDouble(String.valueOf(json.get("CoordY"))),
                            Double.parseDouble(String.valueOf(json.get("CoordX"))));
                    //System.out.println("Matricula: "+bus.getMatricula()+"\nData/Hora: "+bus.getTimestamp());
                    engine.getEPRuntime().sendEvent(bus);

                    JSONObject jsonA = new JSONObject();
                    jsonA.put("time", bus.getTimestamp());
                    jsonA.put("latitude", bus.getLatitude());
                    jsonA.put("longitude",bus.getLongitude());

                    try {
                        Send.sendEvents(jsonA);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}
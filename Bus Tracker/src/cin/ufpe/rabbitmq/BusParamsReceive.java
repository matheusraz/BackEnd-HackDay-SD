package cin.ufpe.rabbitmq;

import com.rabbitmq.client.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class BusParamsReceive {
    private final static String QUEUE_NAME = "params";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("172.17.02");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for PARAMS. To exit press CTRL+C");

        Consumer consumerParams = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                try {
                    JSONObject obj = new JSONObject(message);
                    BusTrackerEventRMQ.teste(obj.getString("Company"),obj.getString("Matricula"));
                    System.out.println(" [x] Received PARAM: '" + message + "'");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumerParams);
    }
}

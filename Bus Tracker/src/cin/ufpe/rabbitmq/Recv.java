package cin.ufpe.rabbitmq;

import com.rabbitmq.client.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Recv {

    private final static String QUEUE_NAME = "entry";

    private List<JSONObject> data;

    public Recv() throws Exception {
        this.data = new ArrayList<>();
        Receive();
    }

    public List<JSONObject> Receive() throws Exception {
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
                    data.add(json);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println(json);

            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
        return data;
    }

    public List<JSONObject> getData() {
        return data;
    }
}
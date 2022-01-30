package org.java.nosql.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RabbitConsumer {
    private static final String QUEUE_NAME ="queue_demo";
    private static final String IP_ADDRESS ="127.0.0.1";
    private static final int POST = 5672;

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Address[] addresses = new Address[]{
                new Address(IP_ADDRESS,POST)
        };

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPassword("guest");
        Connection connection = factory.newConnection(addresses);
        final Channel channel = connection.createChannel();
        channel.basicQos(64);
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String tag,Envelope e, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                System.out.println("message:"+ new String(body));
                try {
                    TimeUnit.SECONDS.sleep(1);
                }catch (Exception exception){
                    exception.printStackTrace();
                }
                channel.basicAck(e.getDeliveryTag(),false);


            }
        };
        channel.basicConsume(QUEUE_NAME,consumer);
        TimeUnit.SECONDS.sleep(5);
        channel.close();
        connection.close();
    }
}

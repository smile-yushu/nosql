package org.java.nosql.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class RPCClient {
    private Connection connection;
    private Channel channel;
    private String requestQueueName = "rpc_queue";
    private String replyQueueName;
    private QueueingConsumer consumer;



    private static final String EXCHANGE_NAME ="exchange_demo";
    private static final String ROUTING_KEY ="routing_key_demo";
    private static final String QUEUE_NAME ="queue_demo";
    private static final String IP_ADDRESS ="localhost";
    private static final int POST = 5672;

    public RPCClient() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(IP_ADDRESS);
        factory.setPort(POST);
        factory.setUsername("guest");
        factory.setPassword("guest");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        replyQueueName = channel.queueDeclare().getQueue();
        consumer= new QueueingConsumer(channel);
        channel.basicConsume(replyQueueName,true,consumer);

    }

    public String call(String message) throws IOException, InterruptedException {
        String response = null;
        String corrId  = UUID.randomUUID().toString();
        AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();
        channel.basicPublish("",requestQueueName,props,message.getBytes());
        while (true){
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response = new String(delivery.getBody());
                break;
            }
        }

        return response;
    }

    public void close() throws IOException {
        connection.close();
    }

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        RPCClient fibRpc = new RPCClient();
        System.out.println(" [x]  Requesting fib(30)");
        String  response = fibRpc.call("30");
        System.out.println(" [.] Got'" + response+"'");
        fibRpc.close();
    }
}

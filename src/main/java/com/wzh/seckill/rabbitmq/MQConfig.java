package com.wzh.seckill.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2020/5/13.
 */
@Configuration
public class MQConfig {
    public static final String QUEUE = "queue";
    public static final String TOPIC_QUEUE1 = "topic.queue1";
    public static final String TOPIC_QUEUE2 = "topic.queue2";
    public static final String HEADER_QUEUE = "header.queue";
    public static final String TOPIC_EXCHANGE = "topicExchage";
    public static final String ROUTING_KEY1 = "topic.key1";
    public static final String ROUTING_KEY2 = "topic.#";

    public static final String FANOUT_EXCHANGE = "fanoutExchage";
    public static final String HEADERS_EXCHANGE = "headersExchage";

    public static final String SECKILL_QUEUE = "seckill.queue";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public Queue miaoshaQueue() {

        return new Queue(SECKILL_QUEUE, true);
    }

    @Bean
    public Queue topicQueue1() {
        return new Queue(TOPIC_QUEUE1, true);
    }

    @Bean
    public Queue topicQueue2() {
        return new Queue(TOPIC_QUEUE2, true);
    }


    /**
     * Topic模式 交换机Exchange
     *
     * @return
     */


    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE);
    }


    @Bean
    public Binding topicBinding1() {
        return BindingBuilder.bind(topicQueue1()).to(topicExchange()).with(ROUTING_KEY1);
    }

    @Bean
    public Binding topicBinding2() {
        return BindingBuilder.bind(topicQueue2()).to(topicExchange()).with(ROUTING_KEY2);
    }


    /**
     * Fanout模式  广播交换机exchange
     */

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    @Bean
    public Binding FanoutBinding1() {
        return BindingBuilder.bind(topicQueue1()).to(fanoutExchange());
    }

    @Bean
    public Binding FanoutBinding2() {
        return BindingBuilder.bind(topicQueue2()).to(fanoutExchange());
    }


    /**
     * Headers模式  广播交换机exchange
     */
    @Bean
    public HeadersExchange headersExchange() {
        return new HeadersExchange(HEADERS_EXCHANGE);
    }

    @Bean
    public Queue headerQueue1() {
        return new Queue(HEADER_QUEUE, true);
    }

    @Bean
    public Binding headerBinding() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("header1", "value1");
        map.put("header2", "value2");
        //message满足map中的key和value才能放入队列
        return BindingBuilder.bind(headerQueue1()).to(headersExchange()).whereAll(map).match();
    }

}

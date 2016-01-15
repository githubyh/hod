package com.yh.hod.flume;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.apache.flume.Channel;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.EventDeliveryException;
import org.apache.flume.Transaction;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;

public class CmccKafkaSink extends AbstractSink implements Configurable {

	private static final Logger log = LoggerFactory
			.getLogger(CmccKafkaSink.class);

	public static final String KEY_HDR = "key";
	public static final String TOPIC_HDR = "topic";
	private static final String CHARSET = "UTF-8";
	private Properties kafkaProps;
	private Producer<String, byte[]> producer;
	private String topic;
	private int batchSize;// 一次事务的event数量，整体提交
	private List<KeyedMessage<String, byte[]>> messageList;

	@Override
	public Status process() throws EventDeliveryException {
		// TODO Auto-generated method stub
		Status result = Status.READY;
		Channel channel = getChannel();
		Transaction transaction = null;
		Event event = null;
		String eventTopic = null;
		String eventKey = null;
		try {
			long processedEvent = 0;
			transaction = channel.getTransaction();
			transaction.begin();// 事务开始
			messageList.clear();
			for (; processedEvent < batchSize; processedEvent++) {
				event = channel.take();// 从channel取出一个事件
				if (event == null) {
					break;
				}
				// Event对象有头和体之分
				Map<String, String> headers = event.getHeaders();
				byte[] eventBody = event.getBody();
				if ((eventTopic = headers.get(TOPIC_HDR)) == null) {// 判断event头部中的topic是否为null
					eventTopic = topic;
				}
				eventKey = headers.get(KEY_HDR);

				if (log.isDebugEnabled()) {
					log.debug("{Event}" + eventTopic + ":" + eventKey + ":"
							+ new String(eventBody, CHARSET));
					log.debug("event #{}", processedEvent);
				}

				KeyedMessage<String, byte[]> data = new KeyedMessage<String, byte[]>(
						eventTopic, eventKey, eventBody);
				messageList.add(data);

			}
			if (processedEvent > 0) {
				producer.send(messageList);
			}
			transaction.commit();// batchSize个事件处理完成，一次事务提交

		} catch (Exception e) {
			String errorMsg = "Failed to publish events !";
			log.error(errorMsg, e);
			result = Status.BACKOFF;
			if (transaction != null) {
				try {
					transaction.rollback();
					log.debug("transaction rollback success !");
				} catch (Exception ex) {
					log.error(errorMsg, ex);
					throw Throwables.propagate(ex);
				}
			}
			throw new EventDeliveryException(errorMsg, e);
		} finally {
			if (transaction != null) {
				transaction.close();
			}
		}
		return result;
	}

	@Override
	public synchronized void start() {
		// TODO Auto-generated method stub
		ProducerConfig config = new ProducerConfig(kafkaProps);
		producer = new Producer<String, byte[]>(config);
		super.start();
	}

	@Override
	public synchronized void stop() {
		// TODO Auto-generated method stub
		producer.close();
		super.stop();
	}

	@Override
	public void configure(Context context) {
		// TODO Auto-generated method stub
		batchSize = context.getInteger(Constants.BATCH_SIZE,
				Constants.DEFAULT_BATCH_SIZE);
		messageList = new ArrayList<KeyedMessage<String, byte[]>>(batchSize);
		log.debug("Using batch size: {}", batchSize);
		topic = context.getString(Constants.TOPIC, Constants.DEFAULT_TOPIC);
		if (topic.equals(Constants.DEFAULT_TOPIC)) {
			log.warn("The property 'topic' is not set .  Using the default topic name ["
					+ Constants.DEFAULT_TOPIC + "]");
		} else {
			log.info("Using the configured topic:[" + topic
					+ "] this may be over-ridden by event headers");
		}
		kafkaProps = KafkaUtil.getKafkaConfig(context);
		if (log.isDebugEnabled()) {
			log.debug("Kafka producer properties : " + kafkaProps);
		}

	}

}
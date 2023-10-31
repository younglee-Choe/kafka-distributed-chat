package leele.kafkadistributedchatserver.kafka.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
//import leele.kafkadistributedchatserver.kafka.mock.MockData;
//import leele.kafkadistributedchatserver.user.dto.UserDTO;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class Producer {
    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("✅ Producer");

        Properties props = new Properties();
        props.put("bootstrap.servers","{HOSTNAME}:{PORT}");   // Brokers
        // StringSerializer를 사용해 Byte 타입으로 Kafka에 데이터 전송
        props.put("key.serializer" , "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer" , "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

//        MockData mockData = new MockData();
//        UserDTO[] data = mockData.main();

        try {
            for(int i=0; i<10; i++){
//                String json = objectMapper.writeValueAsString(data[i]);

                // Producer가 보낼 Record를 생성
                ProducerRecord<String, String> record = new ProducerRecord<>("leele-test", "test" + i);
                // Producer는 Record를 전송하고 결과값을 콜백 인스턴스가 처리 (비동기방식)
                producer.send(record, new ProducerCallback(record));
            }
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            producer.close();
        }
    }
}
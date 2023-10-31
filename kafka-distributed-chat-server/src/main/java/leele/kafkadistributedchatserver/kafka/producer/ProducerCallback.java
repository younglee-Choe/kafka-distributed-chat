package leele.kafkadistributedchatserver.kafka.producer;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class ProducerCallback implements Callback {
    private ProducerRecord<String,String> record;

    public ProducerCallback(ProducerRecord<String, String> record) {
        this.record = record;
    }

    @Override
    public void onCompletion(RecordMetadata metadata, Exception e) {
        if(e != null){
            e.printStackTrace();
        } else {
            System.out.printf("Topic : %s , Partition : %d , Offset : %d, Key : %s, Received Message :%s\n",metadata.topic(),metadata.partition(),metadata.offset(),record.key(),record.value());
        }

    }
}

package kitchenpos.table.application;

import kitchenpos.table.dto.OrderTableRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class OrderTableEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(OrderTableEventPublisher.class);

    private final StreamBridge streamBridge;

    public OrderTableEventPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void publishChangeEmptyEvent(Long orderTableId, OrderTableRequest orderTableRequest) {
        orderTableRequest.setId(orderTableId);
        streamBridge.send("changeTableEmpty", orderTableRequest);
        logger.info("[ 주문테이블 비우기 이벤트 발행 ] : {}", orderTableRequest);
    }

}

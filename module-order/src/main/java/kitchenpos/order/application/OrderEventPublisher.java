package kitchenpos.order.application;

import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.TableGroupResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private final StreamBridge streamBridge;

    public OrderEventPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void publishCreateOrderEvent(OrderResponse orderResponse) {
        streamBridge.send("orderCreate", orderResponse);
        logger.info("[ 주문 생성 유효성 검증 이벤트 발행 ] : {}", orderResponse);
    }

    public void publishCancelUngroupTableEvent(TableGroupResponse tableGroupResponse) {
        streamBridge.send("cancelUngroupTable", tableGroupResponse);
        logger.info("[ 단체지정 해제 취소 이벤트 발행 ] : {}", tableGroupResponse);
    }

    public void publishCancelChangeTableEmptyEvent(OrderTableRequest orderTableRequest) {
        streamBridge.send("cancelChangeTableEmpty", orderTableRequest);
        logger.info("[ 주문테이블 비우기 취소 이벤트 발행 ] : {}", orderTableRequest);
    }

}

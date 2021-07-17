package kitchenpos.table.application;

import kitchenpos.table.dto.OrderResponse;
import kitchenpos.table.dto.TableGroupResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class TableGroupEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(TableGroupEventPublisher.class);

    private final StreamBridge streamBridge;

    public TableGroupEventPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void publishUngroupTableEvent(TableGroupResponse tableGroupResponse) {
        streamBridge.send("ungroupTable", tableGroupResponse);
        logger.info("[ 단체지정 해제 이벤트 발행 ] : {}", tableGroupResponse);
    }

    public void publishCancelOrderCreateEvent(OrderResponse orderResponse) {
        streamBridge.send("cancelOrderCreate", orderResponse);
        logger.info("[ 주문생성 취소 이벤트 발행 ] : {}", orderResponse);
    }

}

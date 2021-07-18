package kitchenpos.product.application;

import kitchenpos.product.dto.MenuResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class ProductEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(ProductEventPublisher.class);

    private final StreamBridge streamBridge;

    public ProductEventPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void publishCancelMenuCreateEvent(MenuResponse menuResponse) {
        streamBridge.send("cancelMenuCreate", menuResponse);
        logger.info("[ 메뉴생성 취소 이벤트 발행 ] : {}", menuResponse);
    }

}

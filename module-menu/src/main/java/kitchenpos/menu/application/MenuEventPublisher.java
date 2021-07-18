package kitchenpos.menu.application;

import kitchenpos.menu.dto.MenuResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class MenuEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(MenuEventPublisher.class);

    private final StreamBridge streamBridge;

    public MenuEventPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void publishCreateMenuEvent(MenuResponse menuResponse) {
        streamBridge.send("menuCreate", menuResponse);
        logger.info("[ 메뉴생성 이벤트 발행 ] : {}", menuResponse);
    }
}

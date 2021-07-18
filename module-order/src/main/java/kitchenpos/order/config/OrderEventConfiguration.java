package kitchenpos.order.config;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import kitchenpos.order.application.OrderEventPublisher;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import kitchenpos.order.dto.TableGroupResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderEventConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(OrderEventConfiguration.class);

    private final OrderService orderService;
    private final OrderEventPublisher orderEventPublisher;

    public OrderEventConfiguration(OrderService orderService, OrderEventPublisher orderEventPublisher) {
        this.orderService = orderService;
        this.orderEventPublisher = orderEventPublisher;
    }

    @Bean
    Consumer<List<OrderResponse>> cancelOrderCreate() {
        return list -> {
            logger.info("[ 주문생성 취소 이벤트 수신 ]");
            list.forEach(this::deleteOrder);
        };
    }

    @Bean
    Consumer<List<TableGroupResponse>> ungroupTable() {
        return list -> {
            logger.info("[ 단체지정 해제 이벤트 수신 ]");
            list.forEach(this::ungroupTable);
        };
    }

    @Bean
    Consumer<List<OrderTableRequest>> changeTableEmpty() {
        return list -> {
            logger.info("[ 주문테이블 비우기 이벤트 수신 ]");
            list.forEach(this::changeTableEmpty);
        };
    }

    private void deleteOrder(OrderResponse orderResponse) {
        orderService.deleteOrderById(orderResponse.getId());
    }

    private void ungroupTable(TableGroupResponse tableGroupResponse) {
        logger.info("[ 단체지정 해제 검증 ] 단체지정ID : {} ", tableGroupResponse.getId());
        List<Long> orderTableIds = tableGroupResponse.getOrderTables().stream()
            .map(OrderTableResponse::getId)
            .collect(Collectors.toList());

        boolean isNotCompleted = orderService.isNotCompletedByOrderTableIds(orderTableIds);
        logger.info("[ 단체지정 해제 검증 ] 결과 : {} ", isNotCompleted);
        if (isNotCompleted) {
            orderEventPublisher.publishCancelUngroupTableEvent(tableGroupResponse);
        }
    }

    private void changeTableEmpty(OrderTableRequest orderTableRequest) {
        logger.info("[ 주문테이블 비우기 검증 ] 주문테이블ID : {} ", orderTableRequest.getId());

        boolean isNotCompleted = orderService.isNotCompletedByOrderTableId(orderTableRequest.getId());
        logger.info("[ 주문테이블 비우기 검증 ] 결과 : {} ", isNotCompleted);
        if (isNotCompleted) {
            orderEventPublisher.publishCancelChangeTableEmptyEvent(orderTableRequest);
        }
    }

}

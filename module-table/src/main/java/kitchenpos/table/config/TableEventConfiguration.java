package kitchenpos.table.config;

import java.util.List;
import java.util.function.Consumer;
import kitchenpos.table.application.OrderTableService;
import kitchenpos.table.application.TableGroupEventPublisher;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.dto.OrderResponse;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TableEventConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(TableEventConfiguration.class);

    private final OrderTableService orderTableService;
    private final TableGroupService tableGroupService;
    private final TableGroupEventPublisher tableGroupEventPublisher;

    public TableEventConfiguration(OrderTableService orderTableService, TableGroupService tableGroupService, TableGroupEventPublisher tableGroupEventPublisher) {
        this.orderTableService = orderTableService;
        this.tableGroupService = tableGroupService;
        this.tableGroupEventPublisher = tableGroupEventPublisher;
    }

    @Bean
    Consumer<List<OrderResponse>> orderCreate() {
        return list -> {
            logger.info("[ 주문생성 유효성 검증 이벤트 수신 ]");
            list.forEach(this::orderCreate);
        };
    }

    @Bean
    Consumer<List<TableGroupResponse>> cancelUngroupTable() {
        return list -> {
            logger.info("[ 단체지정 해제 취소 이벤트 수신 ]");
            list.forEach(this::cancelUngroupTable);
        };
    }

    @Bean
    Consumer<List<OrderTableRequest>> cancelChangeTableEmpty() {
        return list -> {
            logger.info("[ 주문테이블 비우기 취소 이벤트 수신 ]");
            list.forEach(this::cancelChangeTableEmpty);
        };
    }

    private void cancelChangeTableEmpty(OrderTableRequest orderTableRequest) {
        logger.info("[ 주문테이블 비우기 ] : {}", orderTableRequest);
        orderTableService.cancelChangeEmpty(orderTableRequest.getId(), orderTableRequest.isEmpty());
    }

    private void cancelUngroupTable(TableGroupResponse tableGroupResponse) {
        tableGroupService.cancelUngroup(tableGroupResponse);
    }

    private void orderCreate(OrderResponse orderResponse) {
        boolean isEmptyTable = orderTableService.isEmpty(orderResponse.getOrderTableId());
        logger.info("주문테이블 비어있음 : {}", isEmptyTable);
        if (isEmptyTable) {
            logger.info("[ 주문생성 오류 ] 빈 테이블은 주문할 수 없습니다. 주문ID : {} ", orderResponse.getId());
            tableGroupEventPublisher.publishCancelOrderCreateEvent(orderResponse);
        }
    }
}

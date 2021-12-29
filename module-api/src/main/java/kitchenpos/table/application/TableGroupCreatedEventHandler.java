package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroupCreatedEvent;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class TableGroupCreatedEventHandler {

    private final OrderTableRepository orderTableRepository;

    public TableGroupCreatedEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @EventListener
    public void nonEmptyOrderTables(TableGroupCreatedEvent event) {

        Long tableGroupId = event.getTableGroupCreatedEventRequest().getTableGroupId();
        List<Long> orderTableIds = event.getTableGroupCreatedEventRequest().getOrderTableIds();

        orderTableIds.forEach( orderTableId -> {
                                                OrderTable orderTable = orderTableRepository.findById(orderTableId)
                                                                                            .orElseThrow(() -> new IllegalArgumentException("주문 테이블이 존재하지 않습니다."));

                                                orderTable.checkAvailable();
                                                orderTable.changeNonEmptyOrderTable();
                                                orderTable.addTableGroup(tableGroupId);
                                                });


    }


}

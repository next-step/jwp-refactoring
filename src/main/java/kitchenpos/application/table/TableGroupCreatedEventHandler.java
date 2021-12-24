package kitchenpos.application.table;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.tablegroup.TableGroupCreatedEvent;
import kitchenpos.repository.table.OrderTableRepository;
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

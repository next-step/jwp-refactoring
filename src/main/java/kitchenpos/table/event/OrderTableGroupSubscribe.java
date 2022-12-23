package kitchenpos.table.event;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.port.OrderTablePort;
import kitchenpos.tablegroup.event.TableGroupEvent;
import kitchenpos.tablegroup.event.TableUnGroupEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Service
@Transactional
public class OrderTableGroupSubscribe {

    private final OrderTablePort orderTablePort;

    public OrderTableGroupSubscribe(OrderTablePort orderTablePort) {
        this.orderTablePort = orderTablePort;
    }

    @TransactionalEventListener
    public void ungroup(TableUnGroupEvent publisher) {
        Long tableGroupId = publisher.getTableGroupId();
        List<OrderTable> orderTables = orderTablePort.findAllByTableGroupId(tableGroupId);

        orderTables.forEach(OrderTable::ungroup);
    }

    @TransactionalEventListener
    public void group(TableGroupEvent publisher) {
        Long tableGroupId = publisher.getTableGroupId();
        publisher.getOrderTables()
                .forEach(orderTable -> orderTable.setTableGroupId(tableGroupId));
    }
}

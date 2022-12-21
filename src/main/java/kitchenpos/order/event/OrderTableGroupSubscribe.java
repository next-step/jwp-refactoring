package kitchenpos.order.event;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.port.OrderTablePort;
import kitchenpos.tablegroup.event.TableGroupPublisher;
import kitchenpos.tablegroup.event.TableUnGroupPublisher;
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
    public void ungroup(TableUnGroupPublisher publisher) {
        Long tableGroupId = publisher.getTableGroupId();
        List<OrderTable> orderTables = orderTablePort.findAllByTableGroupId(tableGroupId);

        orderTables.forEach(OrderTable::ungroup);
    }

    @TransactionalEventListener
    public void group(TableGroupPublisher publisher) {
        Long tableGroupId = publisher.getTableGroupId();
        publisher.getOrderTables()
                .forEach(orderTable -> orderTable.setTableGroupId(tableGroupId));
    }
}

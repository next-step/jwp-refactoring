package kitchenpos.order.event;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.port.OrderPort;
import kitchenpos.order.port.OrderTablePort;
import kitchenpos.tablegroup.event.TableGroupPublisher;
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
    public void ungroup(TableGroupPublisher tableGroupPublisher) {
        Long tableGroupId = tableGroupPublisher.getTableGroupId();
        List<OrderTable> orderTables = orderTablePort.findAllByTableGroupId(tableGroupId);

        orderTables.forEach(OrderTable::ungroup);
    }
}

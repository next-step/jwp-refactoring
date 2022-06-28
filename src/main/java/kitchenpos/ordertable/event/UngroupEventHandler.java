package kitchenpos.ordertable.event;

import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Transactional(readOnly = true)
@Component
public class UngroupEventHandler {
    private final OrderTableRepository orderTableRepository;

    public UngroupEventHandler(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    @TransactionalEventListener
    @Transactional
    public void ungroupEvent(UngroupEvent event) {
        final List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(event.getTableGroupId());
        orderTables.forEach(OrderTable::unGroup);
    }
}

package kitchenpos.ordertable.domain;

import java.util.List;
import kitchenpos.tablegroup.domain.TableUnGroupedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class TableUnGroupedEventHandler {
    private final OrderTableRepository orderTableRepository;
    private final OrderTableValidator orderTableValidator;

    public TableUnGroupedEventHandler(OrderTableRepository orderTableRepository, OrderTableValidator orderTableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableValidator = orderTableValidator;
    }

    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(TableUnGroupedEvent event) {
        List<OrderTable> tables = orderTableRepository.findAllByTableGroupId(event.getTableGroupId());
        tables.forEach(table -> orderTableValidator.validateOrderStatus(table.getId()));
        tables.forEach(OrderTable::ungroup);
    }
}

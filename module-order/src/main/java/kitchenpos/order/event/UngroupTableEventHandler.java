package kitchenpos.order.event;

import java.util.List;
import kitchenpos.common.exception.NotCompletionStatusException;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.event.UngroupTableEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UngroupTableEventHandler {
    private final OrderRepository orderRepository;

    public UngroupTableEventHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    @Transactional
    public void handle(UngroupTableEventPublisher event) {
        final TableGroup tableGroup = event.tableGroup();
        validateNotCompletionOrderTables(tableGroup.getOrderTableIds());
    }

    private void validateNotCompletionOrderTables(List<Long> orderTableIds) {
        if (orderRepository.existNotCompletionOrderTables(orderTableIds)) {
            throw new NotCompletionStatusException();
        }
    }
}

package kitchenpos.table.application;

import kitchenpos.menu.infra.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTableValidationEvent;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.infra.OrderTableRepository;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidationHandler {
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderTableValidationHandler(final MenuRepository menuRepository,
                                       final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Async
    @EventListener
    public void validateOrder(final OrderTableValidationEvent orderTableValidationEvent) {
        final Order order = orderTableValidationEvent.getOrder();
        order.checkOrderLineIsEmpty();
        final Long menuCount = menuRepository.countByIdIn(order.getMenuIds());
        order.checkOrderLineItemsExists(menuCount);
        final OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        orderTable.checkOrderTableIsEmpty();
    }
}

package kitchenpos.order.application;

import static kitchenpos.Exception.OrderTableAlreadyEmptyException.ORDER_TABLE_ALREADY_EMPTY_EXCEPTION;

import java.util.List;
import kitchenpos.Exception.NotFoundMenuException;
import kitchenpos.Exception.NotFoundOrderTableException;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(final MenuRepository menuRepository, final OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(Order order) {
        validateOrderTableEmpty(order.getOrderTableId());
        validateNotFoundMenu(order.getOrderLineItems());
    }

    private void validateOrderTableEmpty(Long orderTableId) {
        OrderTable orderTable = orderTableRepository.findById(orderTableId)
                .orElseThrow(NotFoundOrderTableException::new);
        if (orderTable.isEmpty()) {
            throw ORDER_TABLE_ALREADY_EMPTY_EXCEPTION;
        }
    }

    private void validateNotFoundMenu(OrderLineItems orderLineItems) {
        final List<Long> menuIds = orderLineItems.getMenuIds();

        if (menuIds.size() != menuRepository.countByIdIn(menuIds)) {
            throw new NotFoundMenuException("존재하지 않는 메뉴가 포함되어 있습니다.");
        }
    }
}

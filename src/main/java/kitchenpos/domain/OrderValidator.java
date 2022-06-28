package kitchenpos.domain;

import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderValidator {
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(MenuRepository menuRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void checkOrderLineItems(final Order order) {
        if (order.isEmptyItem()) {
            throw new IllegalArgumentException();
        }
        if (!isItemMenuExistsAll(order)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isItemMenuExistsAll(final Order order) {
        return order.getOrderLineItems().getOrderLineItems().stream()
                .map(OrderLineItem::getMenuId)
                .allMatch(this::isMenuExists);
    }

    private boolean isMenuExists(Long menuId) {
        return menuRepository.existsById(menuId);
    }

    public void checkOrderTable(final Order order) {
        final OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}

package kitchenpos.domain;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.repository.MenuRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderValidator {
    private final MenuRepository menuRepository;
    private final OrderTableDao orderTableDao;

    public OrderValidator(MenuRepository menuRepository, OrderTableDao orderTableDao) {
        this.menuRepository = menuRepository;
        this.orderTableDao = orderTableDao;
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
        return order.getOrderLineItems().stream()
                .map(OrderLineItem::getMenuId)
                .allMatch(this::isMenuExists);
    }

    private boolean isMenuExists(Long menuId) {
        return menuRepository.existsById(menuId);
    }

    public void checkOrderTable(final Order order) {
        final OrderTable orderTable = orderTableDao.findById(order.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
}

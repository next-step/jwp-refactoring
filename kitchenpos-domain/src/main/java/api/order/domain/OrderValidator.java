package api.order.domain;

import api.exception.menu.MenuNotFoundException;
import api.exception.order.OrderLineItemNotFoundException;
import api.exception.table.InvalidTableException;
import api.exception.table.OrderTableNotFoundException;
import api.menu.domain.Menu;
import api.menu.domain.MenuRepository;
import api.table.domain.OrderTable;
import api.table.domain.OrderTableRepository;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * packageName : kitchenpos.order.domain
 * fileName : OrderValidator
 * author : haedoang
 * date : 2021/12/26
 * description :
 */
@Component
public class OrderValidator {
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(MenuRepository menuRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void validate(Order order) {
        validate(order, getOrderTable(order), getMenus(order));
    }

    private void validate(Order order, OrderTable orderTable, List<Menu> menus) {
        validateOrder(order);
        validateOrderTable(orderTable);
        validateOrderMenu(order, menus);
    }

    private void validateOrder(Order order) {
        if (order.getOrderLineItems().isEmpty()) {
            throw new OrderLineItemNotFoundException();
        }
    }

    private void validateOrderMenu(Order order, List<Menu> menus) {
        if (order.getOrderLineItems().size() > menus.size()) {
            throw new MenuNotFoundException();
        }
    }

    private void validateOrderTable(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new InvalidTableException();
        }
    }

    private OrderTable getOrderTable(Order order) {
        return orderTableRepository.findById(order.getOrderTableId())
                .orElseThrow(OrderTableNotFoundException::new);
    }

    private List<Menu> getMenus(Order order) {
        return menuRepository.findAllById(order
                .getOrderLineItems()
                .stream()
                .map(OrderLineItem::getMenuId)
                .collect(toList())
        );
    }
}

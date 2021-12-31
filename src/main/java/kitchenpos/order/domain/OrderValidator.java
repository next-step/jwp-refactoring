package kitchenpos.order.domain;

import kitchenpos.global.exception.EntityNotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.exception.CannotChangeOrderStatusException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderValidator {

    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderValidator(MenuRepository menuRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public void orderCreateValidator(Order order) {
        existMenuValidator(order);
        existTableValidator(order);
    }

    public void existMenuValidator(Order order) {
        final List<Menu> menus = getMenus(order.getOrderLineItems());
        for (OrderLineItem orderLineItem : order.getOrderLineItems()) {
            existMenu(menus, orderLineItem);
        }
    }

    public void existTableValidator(Order order) {
        final OrderTable orderTable = orderTableRepository.findById(order.getOrderTable())
                .orElseThrow(() -> new EntityNotFoundException(String.format("order not found. find order id is %d", order.getOrderTable())));
        orderTable.checkAvailability();
    }

    private List<Menu> getMenus(final List<OrderLineItem> orderLineItems) {
        List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenu)
                .collect(Collectors.toList());

        return menuRepository.findByIdIn(menuIds);
    }

    private void existMenu(final List<Menu> menus, final OrderLineItem orderLineItem) {
        menus.stream()
                .filter(menu -> menu.getId().equals(orderLineItem.getMenu()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(String.format("menu not found. find menu id is %d", orderLineItem.getMenu())));
    }

    public void changeOrderStatusValidator(final Order order) {
        if (OrderStatus.COMPLETION.equals(order.getOrderStatus())) {
            throw new CannotChangeOrderStatusException(String.format("order status is %s", order.getOrderStatus().name()));
        }
    }
}

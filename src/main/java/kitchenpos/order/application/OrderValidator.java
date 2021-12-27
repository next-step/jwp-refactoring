package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderValidator extends AbstractAggregateRoot<OrderValidator> {

    private final MenuRepository menuRepository;

    public OrderValidator(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public void validateCreate(Order order) {
        validateOrderTable(order.getOrderTableId());
        validateOrderLineItems(order.getOrderLineItems());
    }

    public void validateChangeOrderStatus(Order order) {
        if (order.getOrderStatus().isCompletion()) {
            throw new IllegalArgumentException("주문 상태가 완료인 경우 주문 상태를 수정할 수 없습니다.");
        }
    }

    private void validateOrderTable(Long orderTableId) {
        registerEvent(new OrderTableValidateEvent(orderTableId));
    }

    private void validateOrderLineItems(OrderLineItems orderLineItems) {
        validateOrderLineItemsSize(orderLineItems);
        validateOrderLineItemsMenuSize(orderLineItems);
    }

    private void validateOrderLineItemsSize(OrderLineItems orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems.getOrderLineItems())) {
            throw new IllegalArgumentException("주문 항목은 1개 이상이어야 합니다.");
        }
    }

    private void validateOrderLineItemsMenuSize(OrderLineItems orderLineItems) {
        List<Long> menuIds = orderLineItems.getOrderLineItems().stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        List<Menu> menus = menuRepository.findByIdIn(menuIds);
        if (menus.size() != menuIds.size()) {
            throw new IllegalArgumentException("메뉴 수가 일치하지 않습니다.");
        }
    }
}

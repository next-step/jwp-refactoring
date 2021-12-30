package kitchenpos.order.application;

import kitchenpos.order.domain.MenuAdapter;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderMenuResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Component
public class OrderValidator {

    private OrderMenuServiceImpl orderMenuService;

    public OrderValidator(final OrderMenuServiceImpl orderMenuService) {
        this.orderMenuService = orderMenuService;
    }

    public void validate(Order order) {
        validate(order, getMenus(order));
    }

    private void validate(Order order, Map<Long, MenuAdapter> menus) {

        List<OrderLineItem> orderLineItems = order.getOrderLineItems();

        for(OrderLineItem item : orderLineItems) {
            validateOrderLineItem(item, menus.get(item.getMenuId()));
        }
    }

    private void validateOrderLineItem(OrderLineItem item, MenuAdapter menu) {
        if(item.notMatchOrderItemName(menu.getId(), menu.getName())) {
            throw new IllegalArgumentException("주문항목과 메뉴항목의 이름이 일치하지 않습니다.");
        }

        if(item.notMatchOrderItemPrice(menu.getId(), menu.getPrice())) {
            throw new IllegalArgumentException("주문항목과 메뉴항목의 가격이 일치하지 않습니다.");
        }
    }

    private Map<Long, MenuAdapter> getMenus(Order order) {
        return orderMenuService.findAllByIds(order.getMenuIds()).stream()
                .collect(toMap(MenuAdapter::getId, Function.identity()));
    }
}

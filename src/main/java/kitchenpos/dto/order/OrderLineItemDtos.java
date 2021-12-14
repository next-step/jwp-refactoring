package kitchenpos.dto.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.Orders;
import kitchenpos.exception.menu.NotFoundMenuException;

public class OrderLineItemDtos {
    List<OrderLineItemDto> orderLineItemDtos;

    protected OrderLineItemDtos() {
    }

    private OrderLineItemDtos(List<OrderLineItemDto> orderLineItemDtos) {
        this.orderLineItemDtos = orderLineItemDtos;
    }

    public static OrderLineItemDtos of(List<OrderLineItemDto> orderLineItemDtos) {
        return new OrderLineItemDtos(orderLineItemDtos);
    }

    public List<OrderLineItemDto> getOrderLineItemDtos() {
        return Collections.unmodifiableList(this.orderLineItemDtos);
    }

    public List<Long> getMenuIds() {
        return this.orderLineItemDtos.stream()
                                        .map(orderLIneItemDto -> orderLIneItemDto.getMenuId())
                                        .collect(Collectors.toList());
    }

    public int size() {
        return this.orderLineItemDtos.size();
    }

    public List<OrderLineItem> createOrderLineItem(Orders order, List<Menu> menus) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();

        for (OrderLineItemDto orderLineItemDto : this.orderLineItemDtos) {
            Menu matchingMenu = menus.stream()
                                        .filter(menu -> menu.isEqualMenuId(orderLineItemDto.getMenuId()))
                                        .findFirst()
                                        .orElseThrow(NotFoundMenuException::new);

            orderLineItems.add(OrderLineItem.of(order, matchingMenu, orderLineItemDto.getQuantity()));
        }

        return orderLineItems;
    }

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(this.orderLineItemDtos);
    }
    
    public Long getOrderId(int index) {
        return this.orderLineItemDtos.get(index).getOrderId();
    }
    
    public Long getMenuId(int index) {
        return this.orderLineItemDtos.get(index).getMenuId();
    }
    
    public Long getQuantity(int index) {
        return this.orderLineItemDtos.get(index).getQuantity();
    }

}

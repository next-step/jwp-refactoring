package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.Menus;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemRequest;

@Component
public class OrderLineItemGenerator {
    private final MenuRepository menuRepository;

    public OrderLineItemGenerator(MenuRepository menuRepository) {this.menuRepository = menuRepository;}

    public List<OrderLineItem> generate(List<OrderLineItemRequest> orderLineItemRequests) {
        Menus menus = Menus.from(menuRepository.findAllById(toMenuIds(orderLineItemRequests)));
        return orderLineItemRequests.stream()
            .map(orderLineItemRequest -> generateOrderLineItem(menus, orderLineItemRequest))
            .collect(Collectors.toList());
    }

    private List<Long> toMenuIds(List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
            .map(OrderLineItemRequest::getMenuId)
            .collect(Collectors.toList());
    }

    private OrderLineItem generateOrderLineItem(Menus menus, OrderLineItemRequest orderLineItemRequest) {
        Menu menu = menus.findById(orderLineItemRequest.getMenuId())
            .orElseThrow(() -> new IllegalArgumentException("주문 항목에 등록되어 있지 않은 메뉴가 존재합니다."));
        return OrderLineItem.generate(menu.getId(), menu.getName(), menu.getPrice(),
            orderLineItemRequest.getQuantity());
    }
}

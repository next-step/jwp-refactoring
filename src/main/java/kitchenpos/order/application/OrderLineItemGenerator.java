package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.Menus;
import kitchenpos.menu.domain.OrderMenu;
import kitchenpos.menu.domain.OrderMenuRepository;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenus;
import kitchenpos.order.dto.OrderLineItemRequest;

@Component
public class OrderLineItemGenerator {
    private final MenuRepository menuRepository;
    private final OrderMenuRepository orderMenuRepository;

    public OrderLineItemGenerator(MenuRepository menuRepository, OrderMenuRepository orderMenuRepository) {
        this.menuRepository = menuRepository;
        this.orderMenuRepository = orderMenuRepository;
    }

    public List<OrderLineItem> generate(List<OrderLineItemRequest> orderLineItemRequests) {
        Menus menus = findMenus(toMenuIds(orderLineItemRequests));
        OrderMenus orderMenus = OrderMenus.from(orderMenuRepository.saveAll(menus.generateOrderMenus()));

        return orderLineItemRequests.stream()
            .map(orderLineItemRequest -> generateOrderLineItem(orderMenus, orderLineItemRequest))
            .collect(Collectors.toList());
    }

    private Menus findMenus(List<Long> menuIds) {
        return Menus.from(menuRepository.findAllById(menuIds));
    }

    private List<Long> toMenuIds(List<OrderLineItemRequest> orderLineItemRequests) {
        return orderLineItemRequests.stream()
            .map(OrderLineItemRequest::getMenuId)
            .collect(Collectors.toList());
    }

    private OrderLineItem generateOrderLineItem(OrderMenus orderMenus, OrderLineItemRequest orderLineItemRequest) {
        OrderMenu orderMenu = orderMenus.findByMenuId(orderLineItemRequest.getMenuId())
            .orElseThrow(() -> new IllegalArgumentException("주문 항목에 등록되어 있지 않은 메뉴가 존재합니다."));

        return OrderLineItem.generate(orderMenu.getId(), orderLineItemRequest.getQuantity());
    }
}

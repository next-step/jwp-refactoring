package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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

    public List<OrderLineItem> toOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests, Order order) {
        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new IllegalArgumentException();
        }
        return orderLineItemRequests.stream()
                .map(orderLineItemRequest -> {
                    Menu menu = findMenu(orderLineItemRequest.getMenuId());
                    return new OrderLineItem(menu.getId(), orderLineItemRequest.getQuantity());
                })
                .collect(Collectors.toList());
    }

    public OrderTable findOrderTable(Long orderTableId) {
        OrderTable result = orderTableRepository.findById(orderTableId).orElseThrow(IllegalArgumentException::new);

        return result;
    }

    private Menu findMenu(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(IllegalArgumentException::new);
    }
}

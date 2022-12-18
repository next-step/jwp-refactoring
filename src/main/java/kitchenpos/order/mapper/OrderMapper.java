package kitchenpos.order.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderMapper {

    private final MenuRepository menuRepository;

    public OrderMapper(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Transactional
    public Order mapFrom(final OrderRequest orderRequest) {
        Map<Long, Long> quantityMap = new HashMap<>();
        List<Menu> menus = menuRepository.findAllById(makeMenuIds(orderRequest, quantityMap));

        List<OrderLineItem> orderLineItems = menus.stream()
                .map(menu -> new OrderLineItem(menu.getId(), quantityMap.get(menu.getId()), menu.getName(),
                        menu.getPrice()))
                .collect(Collectors.toList());

        return new Order(orderRequest.getOrderTableId(), orderLineItems);
    }

    private List<Long> makeMenuIds(OrderRequest orderRequest, Map<Long, Long> quantityMap) {
        return orderRequest.getOrderLineItems().stream().map(orderLineItemRequest -> {
            Long menuId = orderLineItemRequest.getMenuId();
            quantityMap.put(menuId, orderLineItemRequest.getQuantity());
            return menuId;
        }).collect(Collectors.toList());
    }
}

package kitchenpos.order.application;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.repository.OrderTableRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Component
public class OrderMapper {

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderMapper(
        final MenuRepository menuRepository,
        final OrderRepository orderRepository,
        final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public Order mapFrom(final Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다. orderId =" + orderId));

    }

    public Order mapFrom(final OrderRequest orderRequest) {
        if (CollectionUtils.isEmpty(orderRequest.getOrderLineItems())) {
            throw new IllegalArgumentException("주문 항목이 비어 있습니다.");
        }

        if (validateMenuIds(orderRequest.getMenuIds())) {
            throw new IllegalArgumentException("메뉴 ID 목록이 유효하지 않습니다. "
                + "존재하지 않는 메뉴가 있거나, 목록이 unique 하지 않습니다.");
        }

        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
            .orElseThrow(() -> new IllegalArgumentException("테이블이 존재하지 않습니다. tableId =" + orderRequest.getOrderTableId()));


        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블은 주문을 등록할 수 없습니다.");
        }

        final OrderLineItems orderLineItems = orderLineItemsFromRequest((orderRequest.getOrderLineItems()));
        return new Order(orderTable, orderRequest.getOrderStatus(), orderLineItems);
    }

    private boolean validateMenuIds(List<Long> menuIds) {
        return menuIds.size() != menuRepository.countByIdIn(menuIds);
    }

    private OrderLineItems orderLineItemsFromRequest(List<OrderLineItemRequest> requests) {
        final OrderLineItems orderLineItems = new OrderLineItems();
        for (final OrderLineItemRequest orderLineItem : requests) {
            Menu menu = menuRepository.findById(orderLineItem.getMenuId())
                .orElseThrow(() -> new IllegalArgumentException("메뉴가 존재하지 않습니다. menuId =" + orderLineItem.getMenuId()));
            orderLineItems.add(orderLineItem.toEntity(menu));
        }
        return orderLineItems;
    }

}

package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.persistence.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.persistence.OrderRepository;
import kitchenpos.order.persistence.OrderLineItemRepository;
import kitchenpos.table.persistence.OrderTableRepository;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderLineItemRepository orderLineItemRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        List<Menu> menus = findAllMenuByIds(orderRequest.findMenuIds());
        Order order = orderRequest.toOrder(orderTable, OrderStatus.COOKING.name(), menus);
        return OrderResponse.of(orderRepository.save(order));
    }

    private List<Menu> findAllMenuByIds(List<Long> menuIds){
        List<Menu> menus = menuRepository.findAllById(menuIds);
        if(menuIds.size() != menus.size()) {
            throw new IllegalArgumentException();
        }
        return menus;
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        order.changeOrderStatus(status);
        return OrderResponse.of(orderRepository.save(order));
    }
}

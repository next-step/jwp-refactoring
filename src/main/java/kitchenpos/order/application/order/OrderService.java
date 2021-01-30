package kitchenpos.order.application.order;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.menu.Menu;
import kitchenpos.menu.domain.menu.MenuRepository;
import kitchenpos.order.domain.order.Order;
import kitchenpos.order.domain.order.OrderLineItemRepository;
import kitchenpos.order.domain.order.OrderRepository;
import kitchenpos.order.domain.ordertable.OrderTable;
import kitchenpos.order.domain.ordertable.OrderTableRepository;
import kitchenpos.order.dto.order.OrderRequest;
import kitchenpos.order.dto.order.OrderResponse;

@Service
@Transactional
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderLineItemRepository orderLineItemRepository;

    public OrderService(final MenuRepository menuRepository, final OrderRepository orderRepository,
        final OrderTableRepository orderTableRepository,
        final OrderLineItemRepository orderLineItemRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderLineItemRepository = orderLineItemRepository;
    }

    public OrderResponse create(final OrderRequest orderRequest) {
        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
            .orElseThrow(IllegalArgumentException::new);
        final List<Menu> menus = menuRepository.findAllByIdIn(orderRequest.getMenuIds());
        final Order order = Order.createToCook(orderTable, orderRequest.toOrderLineItems(menus));

        orderRepository.save(order);

        return OrderResponse.of(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll() {
        return OrderResponse.ofList(orderRepository.findAll());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        order.changeOrderStatus(orderRequest.getOrderStatus());
        return OrderResponse.of(order);
    }
}

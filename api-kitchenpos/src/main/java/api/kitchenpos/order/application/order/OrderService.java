package api.kitchenpos.order.application.order;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import api.kitchenpos.order.dto.order.OrderRequest;
import api.kitchenpos.order.dto.order.OrderResponse;
import domain.kitchenpos.menu.menu.Menu;
import domain.kitchenpos.menu.menu.MenuRepository;
import domain.kitchenpos.order.order.Order;
import domain.kitchenpos.order.order.OrderLineItemRepository;
import domain.kitchenpos.order.order.OrderRepository;
import domain.kitchenpos.order.ordertable.OrderTable;
import domain.kitchenpos.order.ordertable.OrderTableRepository;

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

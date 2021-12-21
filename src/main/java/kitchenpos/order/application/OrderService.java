package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.exception.NotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(final OrderRepository orderRepository,
        final MenuRepository menuRepository,
        final OrderTableRepository orderTableRepository) {
        this.orderRepository = orderRepository;
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
    }


    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {

        final List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItems()
            .stream()
            .map(it -> it.toEntity(findMenu(it.getMenuId())))
            .collect(Collectors.toList());

        final OrderTable orderTable = findOrderTable(orderRequest.getOrderTableId());

        final Order persist = orderRepository
            .save(orderRequest.toEntity(orderTable, orderLineItems));

        return OrderResponse.of(persist);
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return OrderResponse.ofList(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId,
        final OrderStatusRequest orderStatus) {

        final Order savedOrder = findOrder(orderId);

        savedOrder.updateOrderStatus(orderStatus.toStatus());

        return OrderResponse.of(savedOrder);
    }

    private Menu findMenu(Long menuId) {
        return menuRepository.findById(menuId)
            .orElseThrow(() -> new NotFoundException("해당하는 메뉴가 없습니다."));
    }

    private OrderTable findOrderTable(Long orderTableId) {
        return orderTableRepository.findById(orderTableId)
            .orElseThrow(() -> new NotFoundException("해당하는 테이블이 없습니다."));
    }

    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new NotFoundException("해당하는 주문이 없습니다."));
    }
}

package kitchenpos.application;

import kitchenpos.domain.OrderEntity;
import kitchenpos.domain.OrderLineItemEntity;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final MenuService menuService;
    private final TableService tableService;

    public OrderService(
            OrderRepository orderRepository,
            MenuService menuService,
            TableService tableService) {
        this.orderRepository = orderRepository;
        this.menuService = menuService;
        this.tableService = tableService;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        OrderEntity order = OrderEntity.createOrder(
                tableService.findOrderTableById(request.getOrderTableId()),
                request.getOrderLineItems()
                        .stream()
                        .map(itemRequest -> new OrderLineItemEntity(
                                menuService.findMenuById(itemRequest.getMenuId()),
                                itemRequest.getQuantity()))
                        .collect(Collectors.toList())
        );

        orderRepository.save(order);

        return OrderResponse.of(order);
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(order -> OrderResponse.of(order))
                .collect(Collectors.toList());
    }

    @Transactional
    public void changeOrderStatus(final Long orderId, final OrderStatus status) {
        OrderEntity order = findOrderById(orderId);
        order.changeOrderStatus(status);
    }

    public OrderEntity findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("주문을 찾을 수 없습니다: " + id));
    }
}

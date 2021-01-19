package kitchenpos.order.service;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderLineRequest;
import kitchenpos.order.dto.OrderLineResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.service.OrderTableService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrderService {
    private final OrderTableService orderTableService;
    private final OrderRepository orderRepository;
    private final OrderLineItemService orderLineItemService;

    public OrderService(OrderTableService orderTableService, OrderRepository orderRepository, OrderLineItemService orderLineItemService) {
        this.orderTableService = orderTableService;
        this.orderRepository = orderRepository;
        this.orderLineItemService = orderLineItemService;
    }

    public OrderResponse create(final OrderRequest request) {
        checkOrderLineItemEmpty(request.getOrderLineItems());
        final OrderTable tableById = orderTableService.findById(request.getOrderTableId());
        checkOrderTableEmpty(tableById);
        final Order savedOrder = orderRepository.save(new Order(tableById));
        final List<OrderLineResponse> orderLineResponses = orderLineItemService.saveLineItems(savedOrder, request.getOrderLineItems());
        return OrderResponse.of(savedOrder, orderLineResponses);
    }

    private void checkOrderTableEmpty(final OrderTable tableById) {
        if (!tableById.isEmpty()) {
            throw new IllegalArgumentException("테이블이 비어있지 않습니다.");
        }
    }

    private void checkOrderLineItemEmpty(final List<OrderLineRequest> orderLineItems) {
        if (orderLineItems.isEmpty()) {
            throw new IllegalArgumentException("주문 메뉴는 1개 이상이어야 합니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(order -> OrderResponse.of(order, orderLineItemService.list(order)))
                .collect(Collectors.toList());
    }

    public OrderResponse changeOrderStatus(long orderId, String orderStatus) {
        Order orderById = findById(orderId);
        if (orderById.isComplete()) {
            throw new IllegalArgumentException("이미 완료된 주문입니다.");
        }
        orderById.changeOrderStatusByName(orderStatus);

        return OrderResponse.of(orderById);
    }

    @Transactional(readOnly = true)
    public Order findById(long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을수 없습니다."));
    }
}

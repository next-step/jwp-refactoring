package kitchenpos.order.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableService orderTableService;
    private final MenuService menuService;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderTableService orderTableService,
            final MenuService menuService) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableService = orderTableService;
        this.menuService = menuService;
    }

    @Transactional
    public OrderResponse create(final OrderRequest request) {
        validate(request);
        Order saved = orderRepository.save(createOrder(request));
        return OrderResponse.of(saved);
    }

    private void validate(OrderRequest request) {
        List<OrderLineRequest> orderLineItems = request.getOrderLineItems();
        emptyOrderLineItem(orderLineItems);

        final List<Long> menuIds = getMenuIds(orderLineItems);
        if(!menuService.exists(menuIds)) {
            throw new IllegalArgumentException("등록되지 않은 메뉴가 포함되어 있습니다.");
        }

        final OrderTable orderTable = orderTableService.findById(request.getOrderTableId());
        if (!orderTable.isEmpty()) {
            throw new IllegalArgumentException("빈 테이블에만 주문이 가능합니다.");
        }
    }

    private Order createOrder(OrderRequest request) {
        final OrderTable orderTable = orderTableService.findById(request.getOrderTableId());
        final Order order = new Order(orderTable.getId());
        List<OrderLineItem> items = request.getOrderLineItems().stream()
                .map(item -> new OrderLineItem(order, menuRepository.findById(item.getMenuId()).get(), item.getQuantity()))
                .collect(Collectors.toList());
        order.addItems(items);
        return order;
    }

    private List<Long> getMenuIds(List<OrderLineRequest> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineRequest::getMenuId)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream().map(OrderResponse::of)
                .collect(Collectors.toList());

    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest request) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }

        savedOrder.changeOrderStatus(request.getOrderStatus());
        orderRepository.save(savedOrder);
        return OrderResponse.of(savedOrder);
    }

    private void emptyOrderLineItem(final List<OrderLineRequest> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException("메뉴가 1개 이상 입력되어야 합니다.");
        }
    }
}

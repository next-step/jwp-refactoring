package kitchenpos.order.application;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class OrderService {
    private final MenuService menuService;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuService menuService,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuService = menuService;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        validateExistsOrderLineItems(orderRequest);

        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        final Order savedOrder = orderRepository.save(Order.from(orderTable));

        List<OrderLineItem> orderLineItems = findOrderLineItems(orderRequest.getOrderLineItems());

        for (final OrderLineItem orderLineItem : orderLineItems) {
            savedOrder.addOrderLineItem(orderLineItem);
        }

        return OrderResponse.from(savedOrder);
    }

    public List<OrderResponse> list() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrder.changeOrderStatus(orderRequest.getOrderStatus());
        return OrderResponse.from(savedOrder);
    }

    private void validateExistsOrderLineItems(OrderRequest orderRequest) {
        List<Long> menuIds = orderRequest.getMenuIds();
        if (orderRequest.getOrderLineItems().size() != menuService.countByIdIn(menuIds)) {
            throw new IllegalArgumentException("등록하려는 주문 항목의 매뉴가 등록되어있지 않습니다.");
        }
    }

    private List<OrderLineItem> findOrderLineItems(List<OrderLineItemRequest> OrderLineItemRequests) {
        if (CollectionUtils.isEmpty(OrderLineItemRequests)) {
            throw new IllegalArgumentException("주문 항목이 존재하지 않습니다.");
        }

        return OrderLineItemRequests.stream()
                .map(orderLineItemRequest -> {
                    Menu menu = menuService.findMenuById(orderLineItemRequest.getMenuId());
                    return OrderLineItem.of(menu, orderLineItemRequest.getQuantity());
                })
                .collect(Collectors.toList());
    }
}

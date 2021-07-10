package kitchenpos.order.application;

import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.common.exception.NotExistMenuException;
import kitchenpos.common.exception.NotExistOrderException;
import kitchenpos.common.exception.UnableCreateOrderException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.common.exception.NotExistOrderTableException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        List<OrderLineItem> newOrderLineItems = getOrderLineItems(orderRequest);

        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new NotExistOrderTableException("주문테이블이 존재하지 않습니다."));

        Order newOrder = orderTable.newOrder(LocalDateTime.now(), newOrderLineItems);
        final Order savedOrder = orderRepository.save(newOrder);

        OrderResponse orderResponse = OrderResponse.of(savedOrder);
        return orderResponse;
    }

    private List<OrderLineItem> getOrderLineItems(OrderRequest orderRequest) {
        List<OrderLineItemRequest> orderLineItemRequests = orderRequest.getOrderLineItems();

        if (CollectionUtils.isEmpty(orderLineItemRequests)) {
            throw new UnableCreateOrderException("메뉴가 없이 주문을 할수 없습니다.");
        }

        List<OrderLineItem> orderLineItems = orderLineItemRequests
                .stream()
                .map(orderLineItemRequest -> findByOrderLineItem(orderLineItemRequest))
                .collect(Collectors.toList());

        return orderLineItems;
    }

    private OrderLineItem findByOrderLineItem(OrderLineItemRequest orderLineItemRequest) {
        Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
                .orElseThrow(() -> new NotExistMenuException("존재하지 않는 메뉴입니다."));
        return new OrderLineItem(menu.getId(), orderLineItemRequest.getQuantity());
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        List<OrderResponse> orderResponses = orders.stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());

        return orderResponses;
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(NotExistOrderException::new);

        savedOrder.changeOrderStatus(orderRequest.getOrderStatus());

        return OrderResponse.of(savedOrder);
    }
}

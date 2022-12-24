package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domin.*;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.exception.NoSuchDataException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private static final String TABLE_IS_NOT_EMPTY_EXCEPTION = "해당 테이블은 비어 있습니다.";

    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderLineItemRepository orderLineItemRepository;


    public OrderService(
            final MenuRepository menuRepository,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository,
            final OrderLineItemRepository orderLineItemRepository
    ) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderLineItemRepository = orderLineItemRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {

        final OrderTable orderTable = findOrderTableById(orderRequest.getOrderTableId());

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException(TABLE_IS_NOT_EMPTY_EXCEPTION);
        }

        Order order = new Order(OrderStatus.COOKING.name(), LocalDateTime.now(), orderTable.getId());
        final Order savedOrder = orderRepository.save(order);

        List<OrderLineItem> orderLineItemList = orderLineItemRepository.saveAll(generateOrderLineItems(orderRequest.getOrderLineItems(), order));

        return OrderResponse.of(savedOrder, new OrderLineItems(orderLineItemList));
    }

    public List<OrderResponse> list() {

        return orderRepository.findAll().stream()
                .map(order ->
                        OrderResponse.of(order, findOrderLineItemsByOrderId(order.getId())))
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order persistOrder = findOrderById(orderId);
        persistOrder.updateOrderStatus(orderRequest.getOrderStatus());
        return OrderResponse.of(persistOrder, findOrderLineItemsByOrderId(persistOrder.getId()));
    }

    private List<OrderLineItem> generateOrderLineItems(List<OrderLineItemRequest> orderLineItemRequests, Order order) {
        List<OrderLineItem> orderLineItemList = new ArrayList<>();
        orderLineItemRequests.stream()
                .forEach(orderLineItemRequest -> {
                    Menu menu = findMenuById(orderLineItemRequest.getMenuId());
                    orderLineItemList.add(new OrderLineItem(order, menu.getId(), orderLineItemRequest.getQuantity()));
                });

        return orderLineItemList;
    }

    private Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(NoSuchDataException::new);
    }

    private OrderTable findOrderTableById(Long orderTableId) {
        return orderTableRepository.findById(orderTableId).orElseThrow(NoSuchDataException::new);
    }

    private Menu findMenuById(Long menuId) {
        return menuRepository.findById(menuId).orElseThrow(NoSuchDataException::new);
    }

    private OrderLineItems findOrderLineItemsByOrderId(Long orderId) {
        List<OrderLineItem> orderLineItemList = orderLineItemRepository.findOrderLineItemsByOrderId(orderId);
        return new OrderLineItems(orderLineItemList);
    }
}
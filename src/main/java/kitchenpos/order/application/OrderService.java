package kitchenpos.order.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository
            , OrderRepository orderRepository
            , OrderLineItemRepository orderLineItemRepository
            , OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderResponse create(final OrderRequest orderRequest) {
        Order order = this.toOrder(orderRequest);
        order.changeOrderTable(findOrderTable(order));
        order.changeOrderStatus(OrderStatus.COOKING);
        order.changeOrderedTime(LocalDateTime.now());

        final Order savedOrder = this.orderRepository.save(order);
        this.addPersistOrderLineItems(order, savedOrder);

        return OrderResponse.of(savedOrder);
    }


    /**
     * 주문 요청을 Order로 변환합니다.
     * @param orderRequest
     * @return
     */
    private Order toOrder(OrderRequest orderRequest) {
        Order order = orderRequest.toOrder();
        order.changeOrderTable(this.orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문 시 테이블이 반드시 존재해야합니다.")));
        this.addOrderLineItems(orderRequest, order);

        return order;
    }

    /**
     * 주문항목을 주문에 추가합니다.
     * @param orderRequest
     * @param order
     */
    private void addOrderLineItems(OrderRequest orderRequest, Order order) {
        List<OrderLineItemRequest> orderLineItems = orderRequest.getOrderLineItems();

        for (OrderLineItemRequest orderLineItemRequest : orderLineItems) {
            OrderLineItem orderLineItem = orderLineItemRequest.toOrderLineItem();
            orderLineItem.changeMenu(this.menuRepository.findById(orderLineItemRequest.getMenuId())
                    .orElseThrow(() -> new IllegalArgumentException("주문에 포함된 메뉴가 없습니다.")));
            order.addOrderLineItem(orderLineItem);
        }

        order.validateOrderLineItems(this.findMenuIdCount(order.getOrderLineItems()));
    }

    /**
     * 주문항목에 있는 메뉴의 ID 값들을 가져옵니다.
     * @param orderLineItems
     * @return
     */
    private long findMenuIdCount(List<OrderLineItem> orderLineItems) {
        final List<Long> menuIds = orderLineItems.stream()
                .map(orderLineItem -> orderLineItem.getMenu().getId())
                .collect(Collectors.toList());

        return this.menuRepository.countByIdIn(menuIds);
    }

    /**
     * 주문 항목을 저장하고, 주문에 추가합니다.
     * @param order
     * @param savedOrder
     */
    private void addPersistOrderLineItems(Order order, Order savedOrder) {
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();

        for (final OrderLineItem orderLineItem : order.getOrderLineItems()) {
            orderLineItem.changeOrderId(savedOrder.getId());
            savedOrderLineItems.add(this.orderLineItemRepository.save(orderLineItem));
        }
        savedOrder.changeOrderLineItems(new OrderLineItems(savedOrderLineItems));
    }

    /**
     * 해당 주문의 테이블을 찾습니다.
     * @param order 
     * @return
     */
    private OrderTable findOrderTable(Order order) {
        final OrderTable orderTable = this.orderTableRepository.findById(order.getOrderTable().getId())
                .orElseThrow(IllegalArgumentException::new);

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException();
        }
        return orderTable;
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> list() {
        final List<Order> orders = this.orderRepository.findAll();

        for (final Order order : orders) {
            order.changeOrderLineItems(new OrderLineItems(this.orderLineItemRepository.findAllByOrderId(order.getId())));
        }

        return orders.stream().map(OrderResponse::of).collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = this.orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        savedOrder.changeOrderStatus(OrderStatus.valueOf(orderRequest.getOrderStatus()));
        this.orderRepository.save(savedOrder);

        savedOrder.changeOrderLineItems(new OrderLineItems(this.orderLineItemRepository.findAllByOrderId(orderId)));

        return OrderResponse.of(savedOrder);
    }

}

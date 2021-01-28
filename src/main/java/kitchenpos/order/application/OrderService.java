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
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
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

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        Order order = this.toOrder(orderRequest);
        this.validateOrderLineItems(order.getOrderLineItems());

        order.setOrderTable(findOrderTable(order));
        order.setOrderStatus(OrderStatus.COOKING.name());
        order.setOrderedTime(LocalDateTime.now());

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
        order.setOrderTable(this.orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("주문 시 테이블이 반드시 존재해야합니다.")));

        List<OrderLineItemRequest> orderLineItems = orderRequest.getOrderLineItems();

        for (OrderLineItemRequest orderLineItemRequest : orderLineItems) {
            OrderLineItem orderLineItem = orderLineItemRequest.toOrderLineItem();
            orderLineItem.setMenu(this.menuRepository.findById(orderLineItemRequest.getMenuId())
                    .orElseThrow(() -> new IllegalArgumentException("주문에 포함된 메뉴가 없습니다.")));
            order.addOrderLineItem(orderLineItem);
        }

        return order;
    }

    /**
     * 주문 항목을 저장하고, 주문에 추가합니다.
     * @param order
     * @param savedOrder
     */
    private void addPersistOrderLineItems(Order order, Order savedOrder) {
        final List<OrderLineItem> savedOrderLineItems = new ArrayList<>();

        for (final OrderLineItem orderLineItem : order.getOrderLineItems()) {
            orderLineItem.setOrder(savedOrder);
            savedOrderLineItems.add(this.orderLineItemRepository.save(orderLineItem));
        }
        savedOrder.setOrderLineItems(savedOrderLineItems);
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

    /**
     * 주문항목이 적합한지 체크합니다.
     * @param orderLineItems
     */
    private void validateOrderLineItems(List<OrderLineItem> orderLineItems) {
        this.checkEmptyOrderLineItems(orderLineItems);
        this.checkOrderLineItemsCount(orderLineItems);
    }

    /**
     * 주문항목의 개수가 메뉴의 수와 같은지 확인합니다.
     * @param orderLineItems
     */
    private void checkOrderLineItemsCount(List<OrderLineItem> orderLineItems) {
        final List<Long> menuIds = orderLineItems.stream()
                .map(orderLineItem -> orderLineItem.getMenu().getId())
                .collect(Collectors.toList());

        if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 주문항목이 비었는지 확인합니다.
     * @param orderLineItems
     */
    private void checkEmptyOrderLineItems(List<OrderLineItem> orderLineItems) {
        if (CollectionUtils.isEmpty(orderLineItems)) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderResponse> list() {
        final List<Order> orders = this.orderRepository.findAll();

        for (final Order order : orders) {
            order.setOrderLineItems(this.orderLineItemRepository.findAllByOrderId(order.getId()));
        }

        return orders.stream().map(OrderResponse::of).collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = this.orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        this.checkCompletionOrder(savedOrder);
        savedOrder.setOrderStatus(OrderStatus.valueOf(orderRequest.getOrderStatus()).name());
        this.orderRepository.save(savedOrder);

        savedOrder.setOrderLineItems(this.orderLineItemRepository.findAllByOrderId(orderId));

        return OrderResponse.of(savedOrder);
    }

    /**
     * 주문이 완료된 상태인지 확인합니다.
     * @param savedOrder
     */
    private void checkCompletionOrder(Order savedOrder) {
        if (Objects.equals(OrderStatus.COMPLETION.name(), savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException();
        }
    }
}

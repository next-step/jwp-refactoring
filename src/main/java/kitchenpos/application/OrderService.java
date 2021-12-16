package kitchenpos.application;


import kitchenpos.domain.*;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.List;
import java.util.Objects;

@Service
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(MenuRepository menuRepository, OrderRepository orderRepository, OrderLineItemRepository orderLineItemRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public Order create(final OrderRequest orderRequest) {

        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new IllegalArgumentException("등록된 주문테이블이 아닙니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비어있습니다.");
        }
        if (CollectionUtils.isEmpty(orderRequest.getOrderLineItemRequests())) {
            throw new IllegalArgumentException("주문 항목 리스트가 비어있습니다.");
        }
        Order order = new Order(orderTable, OrderStatus.COOKING);
        Order savedOrder = orderRepository.save(order);

        for (OrderLineItemRequest orderLineItemRequest : orderRequest.getOrderLineItemRequests()) {
            addOrderLineItemToOrder(savedOrder, orderLineItemRequest);
        }
        return savedOrder;
    }


    void addOrderLineItemToOrder(Order order, OrderLineItemRequest orderLineItemRequest) {
        Menu menu =  menuRepository.findById(orderLineItemRequest.getMenuId()).orElseThrow(() -> new IllegalArgumentException("등록된 메뉴가 아닙니다."));
        OrderLineItem orderLineItem = new OrderLineItem(order, menu, orderLineItemRequest.getQuantity());
        orderLineItemRepository.save(orderLineItem);
        order.addOrderLineItems(orderLineItem);
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하는 주문 ID가 아닙니다."));

        if (Objects.equals(OrderStatus.COMPLETION, savedOrder.getOrderStatus())) {
            throw new IllegalArgumentException("주문이 완료 상태입니다.");
        }
        savedOrder.changeStatus(orderRequest.getOrderStatus());
        return savedOrder;
    }
}

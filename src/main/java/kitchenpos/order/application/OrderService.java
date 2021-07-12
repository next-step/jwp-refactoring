package kitchenpos.order.application;

import kitchenpos.menu.domain.entity.Menu;
import kitchenpos.menu.domain.entity.MenuRepository;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    private final MenuRepository menuRepository;
    private final OrderRepository orderRepository;
    private final OrderLineItemRepository orderLineItemRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            MenuRepository menuRepository, OrderRepository orderRepository, OrderLineItemRepository orderLineItemRepository, OrderTableRepository orderTableRepository) {
        this.menuRepository = menuRepository;
        this.orderRepository = orderRepository;
        this.orderLineItemRepository = orderLineItemRepository;
        this.orderTableRepository = orderTableRepository;
    }

    public OrderResponse create(final OrderRequest orderRequest) {
        OrderTable orderTable = findOrderTable(orderRequest);
        List<OrderLineItem> orderLineItems = findOrderLineItems(orderRequest);
        Order order = new Order(orderTable, orderLineItems);
        return OrderResponse.of(orderRepository.save(order));
    }

    private List<OrderLineItem> findOrderLineItems(OrderRequest orderRequest) {
        return orderRequest.getOrderLineItems()
                .stream()
                .map(orderLineItemRequest -> {
                    Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId())
                            .orElseThrow(IllegalArgumentException::new);
                    return new OrderLineItem(menu, orderLineItemRequest.getQuantity());
                }).collect(Collectors.toList());
    }

    private OrderTable findOrderTable(OrderRequest orderRequest) {
        OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);
        if(orderTable.isEmpty()){
            throw new IllegalArgumentException();
        }
        return orderTable;
    }

    public List<OrderResponse> list() {
        return OrderResponse.ofList(orderRepository.findAll());
    }

    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrder.changeOrderStatus(orderRequest.getOrderStatus());
        return OrderResponse.of(savedOrder);
    }
}

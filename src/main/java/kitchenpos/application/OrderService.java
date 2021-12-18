package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.domain.*;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {
    private final MenuDao menuDao;
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;

    public OrderService(
            final MenuDao menuDao,
            final OrderRepository orderRepository,
            final OrderTableRepository orderTableRepository
    ) {
        this.menuDao = menuDao;
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @Transactional
    public OrderResponse create(final OrderRequest orderRequest) {
        validateOrderLineItems(orderRequest);

        final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId())
                .orElseThrow(IllegalArgumentException::new);

        Order order = orderRepository.save(orderTable.createOrder());
        final List<OrderLineItemRequest> orderLineItems = orderRequest.getOrderLineItems();
        for (final OrderLineItemRequest orderLineItem : orderLineItems) {
            order.addOrderLineItem(orderLineItem.getMenuId(), orderLineItem.getQuantity());
        }

        return OrderResponse.of(order);
    }

    private void validateOrderLineItems(OrderRequest orderRequest) {
        if (orderRequest.isEmptyOrderLineItems()) {
            throw new IllegalArgumentException();
        }

        if (orderRequest.getOrderLineItemsSize() != menuDao.countByIdIn(orderRequest.getMenuIds())) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();
        return OrderResponse.ofList(orders);
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order order = orderRepository.findById(orderId)
                .orElseThrow(IllegalArgumentException::new);

        final OrderStatus orderStatus = OrderStatus.valueOf(orderRequest.getOrderStatus());
        order.changeOrderStatus(orderStatus);

        return OrderResponse.of(order);
    }
}

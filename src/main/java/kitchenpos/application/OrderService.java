package kitchenpos.application;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.exception.InvalidEntityException;
import kitchenpos.exception.InvalidOrderLineItemsException;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    public Order create(final OrderRequest orderRequest) {

        OrderTable findOrderTable = orderTableRepository
                .findById(orderRequest.getOrderTableId())
                .orElseThrow(() -> new InvalidEntityException("Not found OrderTable " + orderRequest.getOrderTableId()));

        List<OrderLineItem> findOrderLineItems = orderRequest.getOrderLineItemRequests()
                .stream()
                .map(orderLineItemRequest -> {
                    Long quantity = orderLineItemRequest.getQuantity();
                    return OrderLineItem.of(null, orderLineItemRequest.getMenuId(), quantity);
                })
                .collect(Collectors.toList());

        Order order = Order.of(findOrderTable, OrderStatus.COOKING, findOrderLineItems);

        // TODO orderLineItems 이벤트 로 의존성 분리
        List<OrderLineItem> orderLineItems = order.getOrderLineItems();

        final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItem::getMenuId)
                .collect(Collectors.toList());

        int size = orderLineItems.size();
        long savedOrderLineItemsCount = menuRepository.countByIdIn(menuIds);
        if (size != savedOrderLineItemsCount) {
            throw new InvalidOrderLineItemsException("orderLineItems size: " + size +
                    "saved orderLineItems size: " + savedOrderLineItemsCount);
        }

        return orderRepository.save(order);
    }

    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order changeOrderStatus(final Long orderId, final OrderStatus orderStatus) {
        final Order savedOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new InvalidEntityException("Not found orderId - " + orderId));

        savedOrder.changeStatus(orderStatus);

        return savedOrder;
    }
}

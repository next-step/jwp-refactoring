package kitchenpos.order.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.table.dto.OrderLineItemRequest;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

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
        final List<OrderLineItemRequest> orderLineItems = orderRequest.getOrderLineItems();
		validateOrderLineItemsRequestEmpty(orderLineItems);
		validateOrderLineItemsAllExists(orderLineItems);
		final OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId()).orElseThrow(IllegalArgumentException::new);
		validateOrderTableEmpty(orderTable);
		Order savedOrder = orderRepository.save(new Order(orderTable, OrderStatus.COOKING.name(), LocalDateTime.now()));
        savedOrder.addOrderLineItems(orderLineItems.stream()
			.map(orderLineItemRequest -> new OrderLineItem(savedOrder, menuRepository.findById(orderLineItemRequest.getMenuId()).get(), orderLineItemRequest.getQuantity()))
			.collect(Collectors.toList()));

        return OrderResponse.of(savedOrder);
    }

	private void validateOrderTableEmpty(OrderTable orderTable) {
		if (orderTable.isEmpty()) {
			throw new IllegalArgumentException();
		}
	}

	private void validateOrderLineItemsAllExists(List<OrderLineItemRequest> orderLineItems) {
		final List<Long> menuIds = orderLineItems.stream()
                .map(OrderLineItemRequest::getMenuId)
                .collect(Collectors.toList());

		if (orderLineItems.size() != menuRepository.countByIdIn(menuIds)) {
			throw new IllegalArgumentException();
		}
	}

	private void validateOrderLineItemsRequestEmpty(List<OrderLineItemRequest> orderLineItems) {
		if (CollectionUtils.isEmpty(orderLineItems)) {
			throw new IllegalArgumentException();
		}
	}

	public List<OrderResponse> list() {
        final List<Order> orders = orderRepository.findAll();

        return orders.stream().map(order -> OrderResponse.of(order)).collect(Collectors.toList());
    }

    @Transactional
    public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
        final Order savedOrder = orderRepository.findById(orderId).orElseThrow(IllegalArgumentException::new);
		validateOrderStatusChangeable(savedOrder);
		final OrderStatus orderStatus = OrderStatus.valueOf(orderRequest.getOrderStatus());
		savedOrder.updateStatus(orderStatus.name());
        Order order = orderRepository.save(savedOrder);

        return OrderResponse.of(order);
    }

	private void validateOrderStatusChangeable(Order savedOrder) {
		if (savedOrder.isStatusChangeable()) {
			throw new IllegalArgumentException();
		}
	}
}

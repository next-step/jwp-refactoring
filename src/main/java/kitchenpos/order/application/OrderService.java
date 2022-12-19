package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.exception.ErrorMessage;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;

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
		OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId()).orElseThrow(
			() -> new EntityNotFoundException(ErrorMessage.notFoundEntity("주문테이블", orderRequest.getOrderTableId()))
		);
		List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItemRequests()
			.stream()
			.map(this::createOrderLineItem)
			.collect(Collectors.toList());
		Order saved = orderRepository.save(Order.of(orderTable, OrderLineItems.of(orderLineItems)));
		return OrderResponse.of(saved);
	}

	public List<OrderResponse> list() {
		return orderRepository.findAll()
			.stream()
			.map(OrderResponse::of)
			.collect(Collectors.toList());
	}

	@Transactional
	public OrderResponse changeOrderStatus(final Long orderId, final String orderStatus) {
		Order order = findOrderById(orderId);
		order.updateOrderStatus(OrderStatus.valueOf(orderStatus));
		return OrderResponse.of(order);
	}

	private OrderLineItem createOrderLineItem(OrderLineItemRequest orderLineItemRequest) {
		Menu menu = menuRepository.findById(orderLineItemRequest.getMenuId()).orElseThrow(
			() -> new EntityNotFoundException(ErrorMessage.notFoundEntity("메뉴", orderLineItemRequest.getMenuId()))
		);
		return OrderLineItem.of(menu, orderLineItemRequest.getQuantity());
	}

	public boolean isAllOrderFinished(final Long orderTableId) {
		List<Order> orders = orderRepository.findOrdersByOrderTableId(orderTableId);
		return Orders.of(orders).isAllFinished();
	}

	private Order findOrderById(Long orderId) {
		return orderRepository.findById(orderId).orElseThrow(
			() -> new EntityNotFoundException(ErrorMessage.notFoundEntity("주문", orderId))
		);
	}

}

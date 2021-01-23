package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menus;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.domain.OrdersRepository;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.domain.OrderTable;

@Service
@Transactional(readOnly = true)
public class OrderService {
	private final MenuService menuService;
	private final OrderTableService orderTableService;
	private final OrdersRepository orderRepository;
	private final OrderLineItemRepository orderLineItemRepository;

	public OrderService(
		final MenuService menuService,
		final OrderTableService orderTableService,
		final OrdersRepository orderRepository,
		final OrderLineItemRepository orderLineItemRepository

	) {
		this.menuService = menuService;
		this.orderTableService = orderTableService;
		this.orderRepository = orderRepository;
		this.orderLineItemRepository = orderLineItemRepository;
	}

	@Transactional
	public OrderResponse create(final OrderRequest request) {
		final Menus menus = Menus.of(menuService.findAllByIds(request.getMenuIds()));
		final OrderTable orderTable = orderTableService.findById(request.getOrderTableId());
		final Orders order = orderRepository.save(Orders.of(orderTable));
		final OrderLineItems orderLineItems = OrderLineItems.of(order, menus, request.getOrderLineItems());

		return OrderResponse.of(orderLineItemRepository.saveAll(orderLineItems.getOrderLineItems()));
	}

	public List<OrderResponse> list() {
		final List<Orders> orders = orderRepository.findAll();
		return orders.stream()
			.map(order -> OrderResponse.of(orderLineItemRepository.findAllByOrderId(order.getId())))
			.collect(Collectors.toList());
	}

	@Transactional
	public OrderResponse changeOrderStatus(final Long orderId, final OrderStatus order) {
		final Orders savedOrder = findById(orderId);
		savedOrder.changeOrderStatus(order);
		return OrderResponse.of(orderLineItemRepository.findAllByOrderId(savedOrder.getId()));
	}

	private Orders findById(final Long orderId) {
		return orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
	}

	public Orders findByOrderTable(OrderTable orderTable) {
		return orderRepository.findByOrderTable(orderTable);
	}
}


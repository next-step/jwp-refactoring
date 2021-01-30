package kitchenpos.order.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;

@Service
public class OrderService {
	private final OrderRepository orderRepository;

	private final MenuService menuService;
	private final TableService tableService;

	public OrderService(
		final OrderRepository orderRepository,
		MenuService menuService, TableService tableService) {
		this.orderRepository = orderRepository;
		this.menuService = menuService;
		this.tableService = tableService;
	}

	@Transactional
	public OrderResponse create(final OrderRequest orderRequest) {
		List<OrderLineItem> orderLineItems = createOrderLineItems(orderRequest);
		OrderTable orderTable = tableService.findOrderTableById(orderRequest.getOrderTableId());

		if (orderTable.isEmpty()) {
			throw new IllegalArgumentException();
		}

		Order order = Order.builder()
			.orderTable(orderTable)
			.orderLineItems(orderLineItems)
			.build();

		return OrderResponse.of(orderRepository.save(order));
	}

	private List<OrderLineItem> createOrderLineItems(OrderRequest orderRequest) {
		Map<Long, Menu> menus = menuService.findAllMenuByIds(orderRequest.getMenuIds());

		return orderRequest.getOrderLineItems()
			.stream()
			.map(request -> {
				Menu menu = menus.get(request.getMenuId());
				return OrderLineItem.builder().menu(menu).quantity(request.getQuantity()).build();
			})
			.collect(Collectors.toList());
	}

	public List<OrderResponse> list() {
		List<Order> orders = orderRepository.findAll();
		return OrderResponse.of(orders);
	}

	@Transactional
	public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
		final Order order = findOrderById(orderId);

		if (order.isOrderStatus(OrderStatus.COMPLETION)) {
			throw new IllegalArgumentException("완료된 주문 입니다.");
		}

		order.changeOrderStatus(orderRequest.getOrderStatus());

		return OrderResponse.of(orderRepository.save(order));
	}

	public Order findOrderById(Long id) {
		return orderRepository.findById(id).orElseThrow(IllegalArgumentException::new);
	}

	public List<Order> findAllOrderByOrderTableIds(List<Long> ids) {
		return orderRepository.findAllByOrderTableIdIn(ids);
	}
}

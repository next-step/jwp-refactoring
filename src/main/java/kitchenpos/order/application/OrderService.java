package kitchenpos.order.application;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.exception.NotFoundMenuException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderAddRequest;
import kitchenpos.order.dto.OrderLineItemAddRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.exception.NotFoundOrderException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.exception.NotFoundOrderTableException;

@Service
public class OrderService {

	private static final List<OrderStatus> COOKING_OR_MEAL = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);

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
	public OrderResponse create(final OrderAddRequest request) {
		final OrderTable orderTable = findOrderTable(request.getTableId());
		final List<OrderLineItem> orderLineItems = createOrderLineItems(request.getOrderLineItemAddRequests());

		final Order order = orderRepository.save(
			request.toEntity(orderTable, orderLineItems)
		);
		return OrderResponse.of(order);
	}

	private OrderTable findOrderTable(Long id) {
		return orderTableRepository.findById(id)
			.orElseThrow(NotFoundOrderTableException::new);
	}

	private Map<Long, Menu> findMenus(List<OrderLineItemAddRequest> requests) {
		final List<Long> menuIds = requests.stream()
			.map(OrderLineItemAddRequest::getMenuId)
			.collect(Collectors.toList());
		return menuRepository.findAllById(menuIds)
			.stream()
			.collect(Collectors.toMap(Menu::getId, Function.identity()));
	}

	private List<OrderLineItem> createOrderLineItems(List<OrderLineItemAddRequest> requests) {
		final Map<Long, Menu> menus = findMenus(requests);
		if (menus.size() != requests.size()) {
			throw new NotFoundMenuException();
		}
		return requests.stream()
			.map(orderLineItemAddRequest -> OrderLineItem.of(
				menus.get(orderLineItemAddRequest.getMenuId()),
				orderLineItemAddRequest.getQuantity()
			)).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<OrderResponse> list() {
		final List<Order> orders = orderRepository.findAll();
		return orders.stream()
			.map(OrderResponse::of)
			.collect(Collectors.toList());
	}

	@Transactional
	public OrderResponse changeOrderStatus(final Long orderId, final OrderStatusRequest request) {
		final Order order = findOrder(orderId);
		order.changeOrderStatus(request.getOrderStatus());
		return OrderResponse.of(order);
	}

	private Order findOrder(Long id) {
		return orderRepository.findById(id)
			.orElseThrow(NotFoundOrderException::new);
	}

	@Transactional(readOnly = true)
	public boolean existsOrderStatusCookingOrMeal(Long orderTableId) {
		return orderRepository.existsByOrderTable_IdAndOrderStatusIn(orderTableId, COOKING_OR_MEAL);
	}

	@Transactional(readOnly = true)
	public boolean existsOrderStatusCookingOrMeal(List<Long> orderTableIds) {
		return orderRepository.existsByOrderTable_IdInAndOrderStatusIn(orderTableIds, COOKING_OR_MEAL);
	}
}

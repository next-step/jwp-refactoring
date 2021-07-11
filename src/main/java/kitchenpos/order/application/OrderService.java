package kitchenpos.order.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.repository.OrderTableRepository;

@Service
@Transactional(readOnly = true)
public class OrderService {
	private final MenuRepository menuRepository;
	private final OrderRepository orderRepository;
	private final OrderTableRepository orderTableRepository;

	public OrderService(MenuRepository menuRepository, OrderRepository orderRepository,
		OrderTableRepository orderTableRepository) {
		this.menuRepository = menuRepository;
		this.orderRepository = orderRepository;
		this.orderTableRepository = orderTableRepository;
	}

	@Transactional
	public OrderResponse create(final OrderRequest orderRequest) {
		List<OrderLineItem> orderLineItems = createOrderLineItems(orderRequest);
		final OrderTable orderTable = findOrderTable(orderRequest);
		Order order = Order.ofCooking(orderTable, orderLineItems);
		return OrderResponse.of(orderRepository.save(order));
	}

	public List<OrderResponse> list() {
		return orderRepository.findAll().stream().map(OrderResponse::of).collect(Collectors.toList());
	}

	@Transactional
	public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
		Order order = orderRepository.findById(orderId)
			.orElseThrow(() -> new IllegalArgumentException("orderId에 해당하는 주문정보를 찾을 수 없습니다."));
		order.changeState(orderRequest.getOrderStatus());
		return OrderResponse.of(order);
	}

	private List<OrderLineItem> createOrderLineItems(OrderRequest orderRequest) {
		return orderRequest.getOrderLineItems().stream()
			.map(orderLineItemRequest -> {
				Menu menu = findMenu(orderLineItemRequest.getMenuId());
				return new OrderLineItem(menu, orderLineItemRequest.getQuantity());
			}).collect(Collectors.toList());
	}

	private Menu findMenu(Long menuId) {
		return menuRepository.findById(menuId)
			.orElseThrow(() -> new IllegalArgumentException("id에 해당하는 메뉴가 없습니다"));
	}

	private OrderTable findOrderTable(OrderRequest orderRequest) {
		return orderTableRepository.findById(orderRequest.getOrderTableId())
			.orElseThrow(() -> new IllegalArgumentException("id에 해당하는 주문 테이블을 찾을 수 없습니다."));
	}
}

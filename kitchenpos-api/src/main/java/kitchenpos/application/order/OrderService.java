package kitchenpos.application.order;


import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.Orders;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {
	private final MenuRepository menuRepository;
	private final OrderRepository orderRepository;
	private final OrderTableRepository orderTableRepository;
	private final OrderLineItemRepository orderLineItemRepository;

	public OrderService(
			final MenuRepository menuRepository,
			final OrderRepository orderRepository,
			final OrderTableRepository orderTableRepository,
			OrderLineItemRepository orderLineItemRepository
	) {
		this.menuRepository = menuRepository;
		this.orderRepository = orderRepository;
		this.orderTableRepository = orderTableRepository;
		this.orderLineItemRepository = orderLineItemRepository;
	}

	@Transactional
	public OrderResponse create(final OrderRequest orderRequest) {
		OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId()).orElseThrow(() -> new IllegalArgumentException());

		Set<Long> menuIds = orderRequest.menuIds();
		List<Menu> menus = menuRepository.findAllByIdIn(new ArrayList<>(menuIds));
		Orders savedOrder = orderRepository.save(orderRequest.toEntity(orderTable));
		orderLineItemRepository.saveAll(orderRequest.toOrderLineItems(savedOrder, menus));

		return OrderResponse.of(savedOrder);
	}

	public List<OrderResponse> listOrders() {
		return OrderResponse.of(orderRepository.findAll());
	}

	@Transactional
	public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest request) {
		Orders order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException());
		order.changeStatus(request.getOrderStatus());
		return OrderResponse.of(order);
	}
}

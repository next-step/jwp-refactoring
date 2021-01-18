package kitchenpos.order.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.repository.OrderTableRepository;
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
		OrderTable orderTable = orderTableRepository.findById(orderRequest.getOrderTableId()).orElseThrow(() -> new IllegalArgumentException());

		Set<Long> menuIds = orderRequest.menuIds();
		List<Menu> menus = menuRepository.findAllByIdIn(new ArrayList<>(menuIds));
		Orders savedOrder = orderRepository.save(orderRequest.toEntity(orderTable, menus));

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

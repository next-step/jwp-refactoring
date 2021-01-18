package kitchenpos.order.application;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.Orders;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.ordertable.application.OrderTableQueryService;
import kitchenpos.ordertable.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

	private final OrderQueryService orderQueryService;
	private final OrderTableQueryService orderTableQueryService;
	private final MenuService menuService;
	private final OrderRepository orderRepository;

	public OrderService(
		  OrderQueryService orderQueryService,
		  OrderTableQueryService orderTableQueryService,
		  MenuService menuService,
		  OrderRepository orderRepository) {
		this.orderQueryService = orderQueryService;
		this.orderTableQueryService = orderTableQueryService;
		this.menuService = menuService;
		this.orderRepository = orderRepository;
	}

	@Transactional
	public OrderResponse create(final OrderRequest request) {
		Set<Long> menuIds = request.menuIds();
		List<Menu> menus = menuService.findAllByIds(menuIds);
		OrderTable orderTable = orderTableQueryService.findById(request.getOrderTableId());

		Orders savedOrder = orderRepository.save(request.toEntity(orderTable, menus));
		return OrderResponse.of(savedOrder);
	}

	public List<OrderResponse> list() {
		List<Orders> orders = orderQueryService.findAll();
		return orders.stream()
			  .map(OrderResponse::of)
			  .collect(Collectors.toList());
	}

	@Transactional
	public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest request) {
		Orders order = orderQueryService.findById(orderId);
		order.changeStatus(request.getOrderStatus());
		return OrderResponse.of(order);
	}
}

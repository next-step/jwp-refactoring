package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.menu.Menu;
import kitchenpos.ui.dto.OrderRequest;
import kitchenpos.ui.dto.OrderResponse;
import kitchenpos.ui.dto.OrderStatusRequest;

@Service
@Transactional(readOnly = true)
public class OrderService {
	private final TableService tableService;
	private final MenuService menuService;
	private final OrderRepository orderRepository;

	public OrderService(TableService tableService,
						MenuService menuService,
						OrderRepository orderRepository) {
		this.tableService = tableService;
		this.menuService = menuService;
		this.orderRepository = orderRepository;
	}

	@Transactional
	public Order save(Order order) {
		order.startOrder();

		return orderRepository.save(order);
	}

	@Transactional
	public OrderResponse create(OrderRequest request) {
		OrderTable orderTable = tableService.findById(request.getOrderTableId());
		List<Menu> menus = getMenus(request);
		Order order = request.toOrder(orderTable, menus);

		return new OrderResponse(create(order));
	}

	@Transactional
	public Order create( Order order) {
		order.startOrder();

		return orderRepository.save(order);
	}

	private List<Menu> getMenus(OrderRequest request) {
		return menuService.findAllById(request.menuIdList());
	}

	public List<Order> findAll() {
		return orderRepository.findAll();
	}

	public List<OrderResponse> list() {
		return OrderResponse.of(orderRepository.findAll());
	}

	@Transactional
	public OrderResponse changeOrderStatus(Long orderId, OrderStatusRequest statusRequest) {
		return new OrderResponse(changeOrderStatus(orderId, statusRequest.toOrderStatus()));
	}

	@Transactional
	public Order changeOrderStatus(Long orderId, OrderStatus toStatus) {
		Order savedOrder = findById(orderId);

		savedOrder.changeOrderStatus(toStatus);

		return savedOrder;
	}

	private Order findById(Long orderId) {
		return orderRepository.findById(orderId)
			.orElseThrow(IllegalArgumentException::new);
	}
}

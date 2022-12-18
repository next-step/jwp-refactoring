package kitchenpos.order.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.ui.dto.OrderRequest;
import kitchenpos.order.ui.dto.OrderResponse;
import kitchenpos.order.ui.dto.OrderStatusRequest;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;

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
	public Order create(Order order) {
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

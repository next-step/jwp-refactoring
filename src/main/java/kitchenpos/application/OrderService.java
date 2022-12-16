package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order2;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable2;
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
	public Order2 save(Order2 order) {
		order.startOrder();

		return orderRepository.save(order);
	}

	@Transactional
	public OrderResponse create(OrderRequest request) {
		OrderTable2 orderTable = tableService.findById(request.getOrderTableId());
		List<Menu> menus = getMenus(request);
		Order2 order = request.toOrder(orderTable, menus);

		return new OrderResponse(create(order));
	}

	@Transactional
	public Order2 create(final Order2 order) {
		order.startOrder();

		return orderRepository.save(order);
	}

	private List<Menu> getMenus(OrderRequest request) {
		return menuService.findAllById(request.menuIdList());
	}

	public List<Order2> findAll() {
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
	public Order2 changeOrderStatus(Long orderId, OrderStatus toStatus) {
		Order2 savedOrder = findById(orderId);

		savedOrder.changeOrderStatus(toStatus);

		return savedOrder;
	}

	private Order2 findById(Long orderId) {
		return orderRepository.findById(orderId)
			.orElseThrow(IllegalArgumentException::new);
	}
}

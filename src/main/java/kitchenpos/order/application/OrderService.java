package kitchenpos.order.application;

import kitchenpos.common.application.NotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderRequest_ChangeStatus;
import kitchenpos.order.dto.OrderRequest_Create;
import kitchenpos.order.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {
	static final String MSG_CANNOT_FIND_ORDER = "Cannot find Order by orderId";
	static final String MSG_CANNOT_FIND_MENU = "Cannot find Menu by menuId";
	static final String MSG_CANNOT_FIND_ORDER_TABLE = "Cannot find OrderTable by orderTableId";

	private final MenuRepository menuRepository;
	private final OrderRepository orderRepository;
	private final OrderTableRepository orderTableRepository;

	public OrderService(final MenuRepository menuRepository,
	                    final OrderRepository orderRepository,
	                    final OrderTableRepository orderTableRepository) {
		this.menuRepository = menuRepository;
		this.orderRepository = orderRepository;
		this.orderTableRepository = orderTableRepository;
	}

	@Transactional
	public OrderResponse create(OrderRequest_Create request) {
		OrderTable orderTable = orderTableRepository.findById(request.getOrderTableId())
				.orElseThrow(() -> new NotFoundException(MSG_CANNOT_FIND_ORDER_TABLE));
		List<Menu> menus = findMenus(request);
		List<OrderItem> items = request.toOrderItems(menus);
		Order order = orderTable.order(items);
		order = orderRepository.save(order);
		return OrderResponse.of(order);
	}

	private List<Menu> findMenus(OrderRequest_Create request) {
		final List<Long> menuIds = request.getMenuIds();
		List<Menu> menus = menuRepository.findAllById(menuIds);
		if (menus.size() != menuIds.size()) {
			throw new NotFoundException(MSG_CANNOT_FIND_MENU);
		}
		return menus;
	}

	public List<OrderResponse> list() {
		final List<Order> orders = orderRepository.findAll();

		return orders.stream()
				.map(OrderResponse::of)
				.collect(Collectors.toList());
	}

	@Transactional
	public OrderResponse changeOrderStatus(long orderId, OrderRequest_ChangeStatus request) {
		final Order savedOrder = orderRepository.findById(orderId)
				.orElseThrow(() -> new NotFoundException(MSG_CANNOT_FIND_ORDER));

		savedOrder.changeOrderStatus(request.getOrderStatus());
		return OrderResponse.of(savedOrder);
	}
}

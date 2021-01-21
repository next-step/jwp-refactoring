package kitchenpos.application;

import kitchenpos.common.NotFoundException;
import kitchenpos.common.Quantity;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderRequest_ChangeStatus;
import kitchenpos.dto.OrderRequest_Create;
import kitchenpos.dto.OrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OrderService {
	static final String MSG_CANNOT_FIND_ORDER = "Cannot find Order by orderId";
	static final String MSG_CANNOT_FIND_MENU = "Cannot find Menu by menuId";
	static final String MSG_CANNOT_FIND_ORDER_TABLE = "Cannot find OrderTable by orderTableId";


	private final MenuDao menuDao;
	private final OrderDao orderDao;
	private final OrderTableDao orderTableDao;

	public OrderService(final MenuDao menuDao,
	                    final OrderDao orderDao,
	                    final OrderTableDao orderTableDao) {
		this.menuDao = menuDao;
		this.orderDao = orderDao;
		this.orderTableDao = orderTableDao;
	}

	@Transactional
	public OrderResponse create(OrderRequest_Create request) {
		OrderTable orderTable = orderTableDao.findById(request.getOrderTableId())
				.orElseThrow(() -> new NotFoundException(MSG_CANNOT_FIND_ORDER_TABLE));
		List<Menu> menus = findMenu(request);
		List<OrderItem> items = getOrderItems(request, menus);
		Order order = orderTable.order(items);
		order = orderDao.save(order);
		return OrderResponse.of(order);
	}

	private List<Menu> findMenu(OrderRequest_Create request) {
		final List<Long> menuIds = request.getOrderLineItems().stream()
				.map(OrderLineItemRequest::getMenuId)
				.collect(Collectors.toList());

		List<Menu> menus = menuDao.findAllById(menuIds);
		if (menus.size() != menuIds.size()) {
			throw new NotFoundException(MSG_CANNOT_FIND_MENU);
		}
		return menus;
	}

	private List<OrderItem> getOrderItems(OrderRequest_Create request, List<Menu> menus) {
		return request.getOrderLineItems().stream()
				.map(iter -> toOrderItem(menus, iter))
				.collect(Collectors.toList());
	}

	private OrderItem toOrderItem(List<Menu> menus, OrderLineItemRequest request) {
		Menu menu = menus.stream()
				.filter(iter -> Objects.equals(iter.getId(), request.getMenuId()))
				.findFirst()
				.orElseThrow(() -> new NotFoundException(MSG_CANNOT_FIND_MENU));
		return OrderItem.of(menu, new Quantity(request.getQuantity()));
	}

	public List<OrderResponse> list() {
		final List<Order> orders = orderDao.findAll();

		return orders.stream()
				.map(OrderResponse::of)
				.collect(Collectors.toList());
	}

	@Transactional
	public OrderResponse changeOrderStatus(long orderId, OrderRequest_ChangeStatus request) {
		final Order savedOrder = orderDao.findById(orderId)
				.orElseThrow(() -> new NotFoundException(MSG_CANNOT_FIND_ORDER));

		savedOrder.changeOrderStatus(request.getOrderStatus());
		return OrderResponse.of(savedOrder);
	}
}

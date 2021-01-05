package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.exception.NotFoundException;

@Service
public class OrderService {
	private final MenuDao menuDao;
	private final OrderDao orderDao;
	private final OrderLineItemDao orderLineItemDao;
	private final OrderTableDao orderTableDao;

	public OrderService(
		final MenuDao menuDao,
		final OrderDao orderDao,
		final OrderLineItemDao orderLineItemDao,
		final OrderTableDao orderTableDao
	) {
		this.menuDao = menuDao;
		this.orderDao = orderDao;
		this.orderLineItemDao = orderLineItemDao;
		this.orderTableDao = orderTableDao;
	}

	@Transactional
	public OrderResponse create(final OrderRequest orderRequest) {
		final List<Long> menuIds = orderRequest.getMenuIds();

		if (CollectionUtils.isEmpty(menuIds)) {
			throw new NotFoundException("요청된 메뉴 정보가 없습니다.");
		}

		if (menuIds.size() != menuDao.countByIdIn(menuIds)) {
			throw new NotFoundException("요청된 메뉴 정보 중 데이터베이스에 없는 정보가 존재합니다.");
		}

		final OrderTable orderTable = orderTableDao.findById(orderRequest.getOrderTableId())
			.orElseThrow(() -> new NotFoundException("주문테이블 정보를 찾을 수 없습니다."));

		final List<Menu> menus = menuDao.findAllById(menuIds);
		final Order savedOrder = orderDao.save(Order.create(orderTable, menus, orderRequest.getQuantities()));
		return OrderResponse.of(savedOrder);
	}

	public List<OrderResponse> list() {
		final List<Order> orders = orderDao.findAll();
		return orders.stream()
			.map(OrderResponse::of)
			.collect(Collectors.toList());
	}

	@Transactional
	public OrderResponse changeOrderStatus(final Long orderId, final OrderRequest orderRequest) {
		final Order savedOrder = orderDao.findById(orderId)
			.orElseThrow(() -> new NotFoundException("주문 정보를 찾을 수 없습니다."));

		final OrderStatus orderStatus = OrderStatus.valueOf(orderRequest.getOrderStatus());
		savedOrder.changeOrderStatus(orderStatus.name());
		return OrderResponse.of(savedOrder);
	}
}

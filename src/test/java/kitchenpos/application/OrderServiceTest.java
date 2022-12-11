package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	@Mock MenuDao menuDao;
	@Mock OrderDao orderDao;
	@Mock OrderLineItemDao orderLineItemDao;
	@Mock OrderTableDao orderTableDao;
	OrderService orderService;

	static final List<Long> menus = Lists.newArrayList(1L, 2L, 3L);


	@BeforeEach
	void setUp() {
		orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
	}

	@Test
	void testCreateOrder() {
		OrderTable orderTable = createOrderTable();
		Order order = createOrder(orderTable);
		when(menuDao.countByIdIn(anyList())).thenReturn((long) menus.size());
		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));
		when(orderDao.save(order)).thenAnswer(returnsFirstArg());

		order = orderService.create(order);

		verify(orderLineItemDao, times(order.getOrderLineItems().size())).save(any());
		verify(orderDao, times(1)).save(order);
		assertThat(order)
			.extracting(Order::getOrderStatus).isEqualTo(OrderStatus.COOKING.name());
	}

	@Test
	void testListOrder() {
		List<Order> orders = createOrders(1L, 2L, 3L);
		when(orderDao.findAll()).thenReturn(orders);

		orderService.list();

		verify(orderDao, times(1)).findAll();
		verify(orderLineItemDao, times(orders.size())).findAllByOrderId(anyLong());
	}

	@Test
	void changeOrderStatus() {
		Order order = createOrder(createOrderTable());
		Long orderId = order.getId();
		order.setOrderStatus(OrderStatus.MEAL.name());

		when(orderDao.findById(anyLong())).thenReturn(Optional.of(order));
		when(orderLineItemDao.findAllByOrderId(orderId)).thenReturn(order.getOrderLineItems());

		orderService.changeOrderStatus(orderId, order);

		verify(orderDao, times(1)).save(any());
		verify(orderLineItemDao, times(1)).findAllByOrderId(orderId);
	}

	private List<Order> createOrders(long ...orderId) {
		return Arrays.stream(orderId)
			.mapToObj(id -> {
				OrderTable orderTable = createOrderTable();
				Order order = createOrder(orderTable);
				order.setId(id);
				return order;
			}).collect(Collectors.toList());
	}

	private static Order createOrder(OrderTable orderTable) {
		Order order = new Order();
		order.setOrderTableId(orderTable.getId());
		order.setOrderLineItems(createOrderLineItems());
		order.setId(1L);
		return order;
	}

	private static List<OrderLineItem> createOrderLineItems() {
		return menus.stream()
			.map(OrderServiceTest::createOrderLineItem)
			.collect(Collectors.toList());
	}

	private static OrderLineItem createOrderLineItem(long menuId) {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setMenuId(menuId);
		orderLineItem.setQuantity(1);

		return orderLineItem;
	}

	private static OrderTable createOrderTable() {
		OrderTable orderTable = new OrderTable();
		orderTable.setId(1L);
		orderTable.setEmpty(false);
		orderTable.setNumberOfGuests(5);
		return orderTable;
	}
}

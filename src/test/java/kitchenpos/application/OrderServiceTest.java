package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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

	@Mock
	private MenuDao menuDao;

	@Mock
	private OrderTableDao orderTableDao;

	@Mock
	private OrderLineItemDao orderLineItemDao;

	@Mock
	private OrderDao orderDao;

	@InjectMocks
	private OrderService orderService;

	@DisplayName("주문을 등록할 수 있다.")
	@Test
	void create() {
		//given
		OrderTable orderTable = new OrderTable(1L, 6, false);
		List<OrderLineItem> orderLineItems = Arrays.asList(
			new OrderLineItem(1L, 1),
			new OrderLineItem(2L, 3));
		Order order = new Order(orderTable.getId(), LocalDateTime.now(), orderLineItems);

		given(menuDao.countByIdIn(any()))
			.willReturn((long)orderLineItems.size());
		given(orderTableDao.findById(any()))
			.willReturn(Optional.of(orderTable));
		given(orderDao.save(any()))
			.willReturn(order);
		orderLineItems.stream()
			.forEach(orderLineItem -> given(orderLineItemDao.save(orderLineItem))
				.willReturn(orderLineItem));

		// when
		Order savedOrder = orderService.create(order);

		// then
		assertThat(savedOrder.getOrderTableId()).isEqualTo(order.getOrderTableId());
		assertThat(savedOrder.getOrderedTime()).isEqualTo(order.getOrderedTime());
		assertThat(savedOrder.getOrderLineItems()).containsAll(orderLineItems);
	}

	@DisplayName("주문 항목은 1개 이상이어야 한다.")
	@Test
	void create_exception1() {
		//given
		OrderTable orderTable = new OrderTable(1L, 6, false);
		List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(1L, 1));
		Order order = new Order(orderTable.getId(), LocalDateTime.now(), orderLineItems);

		// when, then
		assertThatThrownBy(() -> orderService.create(order))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("중복된 메뉴가 존재하면 안된다..")
	@Test
	void create_exception2() {
		//given
		OrderTable orderTable = new OrderTable(1L, 6, true);
		List<OrderLineItem> orderLineItems = Arrays.asList(
			new OrderLineItem(1L, 1),
			new OrderLineItem(1L, 3));
		Order order = new Order(orderTable.getId(), LocalDateTime.now(), orderLineItems);

		given(menuDao.countByIdIn(any()))
			.willReturn((long)orderLineItems.size());

		// when, then
		assertThatThrownBy(() -> orderService.create(order))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("빈 테이블은 주문할 수 없다.")
	@Test
	void create_exception3() {
		//given
		OrderTable orderTable = new OrderTable(1L, 6, true);
		List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(1L, 1));
		Order order = new Order(orderTable.getId(), LocalDateTime.now(), orderLineItems);

		given(menuDao.countByIdIn(any()))
			.willReturn((long)orderLineItems.size());
		given(orderTableDao.findById(any()))
			.willReturn(Optional.of(orderTable));

		// when, then
		assertThatThrownBy(() -> orderService.create(order))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 목록을 조회할 수 있다.")
	@Test
	void list() {
		// given
		OrderTable orderTable = new OrderTable(1L, 6, true);
		List<OrderLineItem> orderLineItems1 = Arrays.asList(
			new OrderLineItem(1L, 1),
			new OrderLineItem(2L, 3));
		List<OrderLineItem> orderLineItems2 = Arrays.asList(
			new OrderLineItem(3L, 2),
			new OrderLineItem(4L, 5));
		List<Order> orders = Arrays.asList(
			new Order(1L, orderTable.getId(), OrderStatus.MEAL.name(), LocalDateTime.now(), orderLineItems1),
			new Order(2L, orderTable.getId(), OrderStatus.COMPLETION.name(), LocalDateTime.now(), orderLineItems2));

		given(orderDao.findAll())
			.willReturn(orders);
		orders.stream().forEach(order -> given(orderLineItemDao.findAllByOrderId(order.getId()))
			.willReturn(order.getOrderLineItems()));

		//when
		List<Order> findOrders = orderService.list();

		//then
		assertThat(findOrders.size()).isEqualTo(orders.size());
		assertThat(findOrders).containsAll(orders);
	}

	@DisplayName("주문 상태를 변경할 수 있다.")
	@Test
	void changeOrderStatus() {
		// given
		OrderTable orderTable = new OrderTable(1L, 6, true);
		List<OrderLineItem> orderLineItems = Arrays.asList(
			new OrderLineItem(1L, 1),
			new OrderLineItem(2L, 3));

		Order order = new Order(1L, orderTable.getId(), OrderStatus.MEAL.name(),
			LocalDateTime.now(), orderLineItems);

		given(orderDao.findById(any()))
			.willReturn(Optional.of(order));

		//when
		Order changeOrder = new Order(OrderStatus.COMPLETION.name());
		Order savedOrder = orderService.changeOrderStatus(order.getId(), changeOrder);

		//then
		assertThat(savedOrder.getOrderStatus()).isEqualTo(changeOrder.getOrderStatus());
	}

	@DisplayName("계산 완료된 주문 상태는 변경할 수 없다.")
	@Test
	void changeOrderStatus_exception() {
		// given
		OrderTable orderTable = new OrderTable(1L, 6, true);
		List<OrderLineItem> orderLineItems = Arrays.asList(
			new OrderLineItem(1L, 1));

		Order order = new Order(1L, orderTable.getId(), OrderStatus.COMPLETION.name(),
			LocalDateTime.now(), orderLineItems);

		given(orderDao.findById(any()))
			.willReturn(Optional.of(order));

		//when
		Order changeOrder = new Order(OrderStatus.MEAL.name());
		assertThatThrownBy(() -> orderService.changeOrderStatus(order.getId(), changeOrder))
			.isInstanceOf(IllegalArgumentException.class);
	}
}

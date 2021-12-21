package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
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
	@Mock
	private MenuDao menuDao;

	@Mock
	private OrderDao orderDao;

	@Mock
	private OrderLineItemDao orderLineItemDao;

	@Mock
	private OrderTableDao orderTableDao;

	@Test
	@DisplayName("주문 생성 테스트")
	public void createOrderSuccessTest() {
		//given
		OrderLineItem orderLineItem = new OrderLineItem(null, null, 1L, 1L);
		Order order = new Order(null, 1L, null, null, Lists.newArrayList(orderLineItem));
		OrderTable orderTable = new OrderTable(1L, 1L, 0, true);
		when(menuDao.countByIdIn(Lists.newArrayList(1L))).thenReturn(1L);
		when(orderTableDao.findById(1L)).thenReturn(Optional.of(orderTable));
		when(orderDao.save(order)).thenReturn(new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), null));
		when(orderLineItemDao.save(orderLineItem)).thenReturn(new OrderLineItem(1L, 1L, 1L, 1L));
		OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

		//when
		Order save = orderService.create(order);

		//then
		assertThat(save).isNotNull();
		assertThat(save.getId()).isEqualTo(1L);
		assertThat(save.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
	}

	@Test
	@DisplayName("메뉴를 고르지 않아서 생성 실패")
	public void createOrderFailNotSelectMenuTest() {
		//given
		Order order = new Order(null, 1L, null, null, Lists.emptyList());
		OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

		//when
		//then
		assertThatThrownBy(() -> orderService.create(order))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("주문할 메뉴를 골라주세요");
	}

	@Test
	@DisplayName("식당에 없는 메뉴를 주문해서 실패")
	public void createOrderFailNoneMenuTest() {
		//given
		OrderLineItem orderLineItem = new OrderLineItem(null, null, 1L, 1L);
		Order order = new Order(null, 1L, null, null, Lists.newArrayList(orderLineItem));
		when(menuDao.countByIdIn(Lists.newArrayList(1L))).thenReturn(2L);
		OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

		//when
		//then
		assertThatThrownBy(() -> orderService.create(order))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("없는 메뉴는 주문할 수 없습니다");
	}

	@Test
	@DisplayName("주문을 받을 테이블이 없어서 실패")
	public void createOrderFailNotExistedTableTest() {
		//given
		OrderLineItem orderLineItem = new OrderLineItem(null, null, 1L, 1L);
		Order order = new Order(null, 1L, null, null, Lists.newArrayList(orderLineItem));
		when(menuDao.countByIdIn(Lists.newArrayList(1L))).thenReturn(1L);
		when(orderTableDao.findById(1L)).thenReturn(Optional.empty());
		OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

		//when
		//then
		assertThatThrownBy(() -> orderService.create(order))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("주문을 받을 테이블이 존재하지 않습니다");
	}

	@Test
	@DisplayName("주문이 할당 된 테이블이 비어있지 않아서 실패")
	public void createOrderFailNotEmptyTableTest() {
		//given
		OrderLineItem orderLineItem = new OrderLineItem(null, null, 1L, 1L);
		Order order = new Order(null, 1L, null, null, Lists.newArrayList(orderLineItem));
		OrderTable orderTable = new OrderTable(1L, 1L, 0, false);
		when(menuDao.countByIdIn(Lists.newArrayList(1L))).thenReturn(1L);
		when(orderTableDao.findById(1L)).thenReturn(Optional.of(orderTable));
		OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

		//when
		//then
		assertThatThrownBy(() -> orderService.create(order))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("테이블에 사람이 있습니다");
	}

	@Test
	@DisplayName("주문 목록 조회 테스트")
	public void findAllOrderList() {
		//given
		OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1L);
		Order order = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), null);
		when(orderDao.findAll()).thenReturn(Lists.newArrayList(order));
		when(orderLineItemDao.findAllByOrderId(1L)).thenReturn(Lists.newArrayList(orderLineItem));
		OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

		//when
		List<Order> orders = orderService.list();

		//then
		assertThat(orders).hasSizeGreaterThanOrEqualTo(1);
		assertThat(orders.get(0).getOrderLineItems()).hasSize(1);
	}

	@Test
	@DisplayName("주문 상태 변경 테스트")
	public void changeOrderStatusTest() {
		//given
		OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1L);
		Order order = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), null);
		when(orderDao.findById(1L)).thenReturn(Optional.of(order));
		when(orderLineItemDao.findAllByOrderId(1L)).thenReturn(Lists.newArrayList(orderLineItem));
		OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

		//when
		Order updateParamOrder = new Order(null, null, OrderStatus.MEAL.name(), null, null);
		Order change = orderService.changeOrderStatus(1L, updateParamOrder);

		//then
		assertThat(change.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
	}

	@Test
	@DisplayName("존재하지 않는 주문 상태 변경 실패 테스트")
	public void changeOrderStatusFailNotExistedTest() {
		//given
		when(orderDao.findById(1L)).thenReturn(Optional.empty());
		OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

		//when
		Order updateParamOrder = new Order(null, null, OrderStatus.MEAL.name(), null, null);

		//then
		assertThatThrownBy(() -> orderService.changeOrderStatus(1L, updateParamOrder))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("주문이 존재하지 않습니다");
	}

	@Test
	@DisplayName("계산완료 된 주문 상태변경 실패")
	public void changeOrderStatusFailAlreadyCompletionTest() {
		//given
		OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1L);
		Order order = new Order(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), null);
		when(orderDao.findById(1L)).thenReturn(Optional.of(order));
		OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

		//when
		Order updateParamOrder = new Order(null, null, OrderStatus.MEAL.name(), null, null);

		//then
		assertThatThrownBy(() -> orderService.changeOrderStatus(1L, updateParamOrder))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("계산완료 된 주문은 상태를 변경 할 수 없습니다");

	}
}

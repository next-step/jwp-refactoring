package kitchenpos.application;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
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

@DisplayName("주문 요구사항 테스트")
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

	@InjectMocks
	private OrderService orderService;

	@DisplayName("주문 생성시 주문 목록이 존재해야 한다.")
	@Test
	void createOrderWithoutOrderLineItems() {
		// given
		Order order = mock(Order.class);
		when(order.getOrderLineItems()).thenReturn(new ArrayList<>());

		// when
		// then
		assertThatThrownBy(() -> orderService.create(order))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("주문 항목이 없습니다.");
	}

	@DisplayName("주문 생성시 등록이 안된 메뉴는 주문할 수 없다.")
	@Test
	void createOrderWithUnknownMenusTest() {
		// given
		OrderLineItem orderLineItem = mock(OrderLineItem.class);
		when(orderLineItem.getMenuId()).thenReturn(1L);
		
		Order order = mock(Order.class);
		when(order.getOrderLineItems()).thenReturn(asList(orderLineItem));

		when(menuDao.countByIdIn(asList(1L))).thenReturn(0L);

		// when
		// then
		assertThatThrownBy(() -> orderService.create(order))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("등록이 안된 메뉴는 주문할 수 없습니다.");
	}

	@DisplayName("등록이 안된 주문테이블에서 주문할 수 없다.")
	@Test
	void createOrderWithUnknownOrderTableTest() {
		// given
		OrderLineItem orderLineItem = mock(OrderLineItem.class);
		when(orderLineItem.getMenuId()).thenReturn(1L);

		Order order = mock(Order.class);
		when(order.getOrderLineItems()).thenReturn(asList(orderLineItem));

		when(menuDao.countByIdIn(asList(1L))).thenReturn(1L);
		when(orderTableDao.findById(anyLong())).thenReturn(Optional.empty());

		// when
		// then
		assertThatThrownBy(() -> orderService.create(order))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("등록이 안된 주문 테이블에서는 주문할 수 없습니다.");
	}

	@DisplayName("빈 주문테이블에서 주문할 수 없다.")
	@Test
	void createOrderWithEmptyOrderTableTest() {
		// given
		OrderLineItem orderLineItem = mock(OrderLineItem.class);
		when(orderLineItem.getMenuId()).thenReturn(1L);

		Order order = mock(Order.class);
		when(order.getOrderLineItems()).thenReturn(asList(orderLineItem));

		OrderTable emptyTable = mock(OrderTable.class);
		when(emptyTable.isEmpty()).thenReturn(true);

		when(menuDao.countByIdIn(asList(1L))).thenReturn(1L);
		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(emptyTable));

		// when
		// then
		assertThatThrownBy(() -> orderService.create(order))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("빈테이블에서 주문할 수 없습니다.");
	}

	@DisplayName("주문 생성시 주문과, 주문목록이 저장된다.")
	@Test
	void createOrderVerifiedMethodCallTest() {
		// given
		OrderLineItem orderLineItem = mock(OrderLineItem.class);
		when(orderLineItem.getMenuId()).thenReturn(1L);

		Order order = mock(Order.class);
		when(order.getOrderLineItems()).thenReturn(asList(orderLineItem));

		OrderTable orderTable = mock(OrderTable.class);
		when(orderTable.getId()).thenReturn(1L);

		Order savedOrder = mock(Order.class);
		when(savedOrder.getId()).thenReturn(1L);

		when(menuDao.countByIdIn(asList(1L))).thenReturn(1L);
		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));
		when(orderDao.save(order)).thenReturn(savedOrder);

		// when
		orderService.create(order);

		// then
		verify(orderDao).save(order);
		verify(orderLineItemDao).save(orderLineItem);
	}

	@DisplayName("등록된 주문의 상태만 변경 가능하다.")
	@Test
	void changeUnknownOrderTest() {
		// given
		when(orderDao.findById(anyLong())).thenReturn(Optional.empty());

		// when
		// then
		assertThatThrownBy(() -> orderService.changeOrderStatus(1L, new Order()))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("등록이 안된 주문은 상태를 변경할 수 없습니다.");
	}

	@DisplayName("계산완료상태의 주문은 상태변경이 불가능하다.")
	@Test
	void changeCompletedOrderTest() {
		// given
		Order completed = mock(Order.class);
		when(completed.getOrderStatus()).thenReturn(OrderStatus.COMPLETION.name());

		when(orderDao.findById(anyLong())).thenReturn(Optional.of(completed));

		// when
		// then
		assertThatThrownBy(() -> orderService.changeOrderStatus(1L, new Order()))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("계산 완료 주문은 상태를 변경할 수 없습니다.");
	}

	@DisplayName("주문의 상태를 변경 가능하다.")
	@Test
	void changeOrderTest() {
		// given
		Order cookingOrder = new Order();

		OrderLineItem orderLineItem = mock(OrderLineItem.class);

		Order mealOrder = mock(Order.class);
		when(mealOrder.getOrderStatus()).thenReturn(OrderStatus.MEAL.name());

		when(orderDao.findById(anyLong())).thenReturn(Optional.of(cookingOrder));
		when(orderLineItemDao.findAllByOrderId(anyLong())).thenReturn(asList(orderLineItem));

		// when
		orderService.changeOrderStatus(1L, mealOrder);

		// then
		assertThat(cookingOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
		assertThat(cookingOrder.getOrderLineItems()).isNotEmpty();
	}

	@DisplayName("주문 목록을 조회할 수 있다.")
	@Test
	void orderListTest() {
		// given
		List<Order> orders = asList(mock(Order.class));
		when(orderDao.findAll()).thenReturn(orders);

		// when
		List<Order> findOrders = orderService.list();

		// then
		assertThat(findOrders).isSameAs(orders);
		verify(orderLineItemDao).findAllByOrderId(anyLong());
	}

}

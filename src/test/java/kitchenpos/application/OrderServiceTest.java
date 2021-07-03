package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
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
	private OrderDao orderDao;
	@Mock
	private OrderLineItemDao orderLineItemDao;
	@Mock
	private OrderTableDao orderTableDao;
	@InjectMocks
	private OrderService orderService;

	private OrderLineItem orderLineItem1;
	private Order savedOrder;

	@BeforeEach
	void setUp() {
		List<OrderLineItem> orderLineItems = new ArrayList<>();
		orderLineItem1 = new OrderLineItem(1L, 1L, 1L, 3);
		orderLineItems.add(orderLineItem1);
		savedOrder = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
	}

	@DisplayName("빈 테이블에서 주문이 생성된경우 오류 발생")
	@Test
	void testOrderTableEmpty() {
		Order order = mock(Order.class);
		OrderLineItem orderLineItem = mock(OrderLineItem.class);
		OrderTable orderTable = new OrderTable(1L, 1L, 0, true);

		when(order.getOrderLineItems()).thenReturn(Arrays.asList(orderLineItem));
		when(menuDao.countByIdIn(any())).thenReturn(1L);
		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));

		assertThatThrownBy(() -> {
			orderService.create(order);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("비어있는 테이블은 주문할 수 없습니다.");
	}

	@DisplayName("주문 항목 수와 메뉴에 등록된 주문항목의 수가 다르면 오류 발생")
	@Test
	void testNotContainsOrderLineItemInMenu() {
		Order order = mock(Order.class);
		OrderLineItem orderLineItem = mock(OrderLineItem.class);

		when(order.getOrderLineItems()).thenReturn(Arrays.asList(orderLineItem));
		when(menuDao.countByIdIn(any())).thenReturn(2L);
		assertThatThrownBy(() -> {
			orderService.create(order);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("주문 항목과 메뉴에서 찾은 주문항목의 수가 일치하지 않습니다.");
	}

	@DisplayName("주문에 주문 항목이 비어있는 경우 오류 발생")
	@Test
	void testEmptyOrderLineItem() {
		Order order = mock(Order.class);

		when(order.getOrderLineItems()).thenReturn(Collections.emptyList());
		assertThatThrownBy(() -> {
			orderService.create(order);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("주문 항목을 구성해야 주문이 가능합니다.");
	}

	@DisplayName("주문 생성 - happy path")
	@Test
	void testCreateOrder() {
		Order order = mock(Order.class);
		OrderLineItem orderLineItem = mock(OrderLineItem.class);
		OrderTable orderTable = mock(OrderTable.class);

		when(order.getOrderLineItems()).thenReturn(Arrays.asList(orderLineItem));
		when(menuDao.countByIdIn(any())).thenReturn(1L);
		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));
		when(orderTable.isEmpty()).thenReturn(false);
		when(orderDao.save(eq(order))).thenReturn(savedOrder);

		Order actual = orderService.create(order);
		//then
		verify(orderLineItemDao, times(1)).save(orderLineItem);
		assertThat(actual.getId()).isEqualTo(savedOrder.getId());
	}

	@DisplayName("주문 상태 변경 오류 - 주문이 이미 완료상태인 경우")
	@Test
	void testAlreadyOrderStatusCompletion() {
		Order paramOrder = mock(Order.class);
		Order findOrder = mock(Order.class);

		when(orderDao.findById(eq(1L))).thenReturn(Optional.of(findOrder));
		when(findOrder.getOrderStatus()).thenReturn(OrderStatus.COMPLETION.name());

		assertThatThrownBy(() -> {
			orderService.changeOrderStatus(1L, paramOrder);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("이미 완료된 주문입니다.");

	}

	@DisplayName("주문 상태 변경 테스트")
	@Test
	void testChangeOrderStatus() {
		Order paramOrder = mock(Order.class);

		when(orderDao.findById(eq(1L))).thenReturn(Optional.of(savedOrder));
		when(paramOrder.getOrderStatus()).thenReturn(OrderStatus.MEAL.name());

		Order order = orderService.changeOrderStatus(1L, paramOrder);
		assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
	}

	@DisplayName("주문 상태 변경 오류 - 변경하려는 주문을 찾을 수 없음")
	@Test
	void testOrderNotFound() {
		Order paramOrder = mock(Order.class);
		when(orderDao.findById(eq(1L))).thenReturn(Optional.empty());

		assertThatThrownBy(() -> {
			orderService.changeOrderStatus(1L, paramOrder);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("orderId에 해당하는 주문정보를 찾을 수 없습니다.");
	}
}
package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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

	private OrderLineItem A세트3개;
	private Order 주문;
	private List<OrderLineItem> 주문항목;

	@BeforeEach
	void setUp() {
		주문항목 = new ArrayList<>();
		A세트3개 = new OrderLineItem(1L, 1L, 1L, 3);
		주문항목.add(A세트3개);
		주문 = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), 주문항목);
	}

	@DisplayName("빈 테이블에서 주문이 생성된경우 오류 발생")
	@Test
	void testOrderTableEmpty() {
		Order order = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), 주문항목);
		OrderTable orderTable = new OrderTable(1L, 1L, 0, true);

		when(menuDao.countByIdIn(any())).thenReturn(1L);
		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));

		assertThatThrownBy(() -> {
			orderService.create(order);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("비어있는 테이블은 주문할 수 없습니다.");
	}

	@DisplayName("주문하려는 테이블을 찾을 수 없는 경우 오류 발생")
	@Test
	void testNotFoundOrderTable() {
		Order order = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), 주문항목);
		when(menuDao.countByIdIn(any())).thenReturn(1L);
		when(orderTableDao.findById(anyLong())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> {
			orderService.create(order);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("id에 해당하는 주문 테이블을 찾을 수 없습니다.");
	}

	@DisplayName("주문 항목 수와 메뉴에 등록된 주문항목의 수가 다르면 오류 발생")
	@Test
	void testNotContainsOrderLineItemInMenu() {
		Order order = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), 주문항목);

		List<Long> menuIds = Arrays.asList(1L);
		when(menuDao.countByIdIn(eq(menuIds))).thenReturn(2L);
		assertThatThrownBy(() -> {
			orderService.create(order);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("주문 항목과 메뉴에서 찾은 주문항목의 수가 일치하지 않습니다.");
	}

	@DisplayName("주문에 주문 항목이 비어있는 경우 오류 발생")
	@Test
	void testEmptyOrderLineItem() {
		Order order = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), new ArrayList<>());

		assertThatThrownBy(() -> {
			orderService.create(order);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("주문 항목을 구성해야 주문이 가능합니다.");
	}

	@DisplayName("주문 생성 - happy path")
	@Test
	void testCreateOrder() {
		Order order = new Order(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), 주문항목);
		OrderTable orderTable = new OrderTable(1L, 1L, 0, false);
		Long orderTableId = order.getOrderTableId();

		when(menuDao.countByIdIn(any())).thenReturn(1L);
		when(orderTableDao.findById(eq(orderTableId))).thenReturn(Optional.of(orderTable));
		when(orderDao.save(eq(order))).thenReturn(order);
		when(orderLineItemDao.save(A세트3개)).thenReturn(A세트3개);

		Order actual = orderService.create(order);
		//then
		verify(orderLineItemDao, times(1)).save(A세트3개);
		assertThat(actual.getId()).isEqualTo(this.주문.getId());
	}

	@DisplayName("주문 상태 변경 오류 - 주문이 이미 완료상태인 경우")
	@Test
	void testAlreadyOrderStatusCompletion() {
		Order completionOrder = new Order(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), 주문항목);

		when(orderDao.findById(eq(completionOrder.getId()))).thenReturn(Optional.of(completionOrder));

		assertThatThrownBy(() -> {
			orderService.changeOrderStatus(1L, completionOrder);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("이미 완료된 주문입니다.");

	}

	@DisplayName("주문 상태 변경 테스트")
	@Test
	void testChangeOrderStatus() {
		Order order = new Order(1L, 1L, OrderStatus.MEAL.name(), LocalDateTime.now(), 주문항목);
		Long orderId = 1L;

		when(orderDao.findById(eq(orderId))).thenReturn(Optional.of(주문));

		Order actual = orderService.changeOrderStatus(orderId, order);
		assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
	}

	@DisplayName("주문 상태 변경 오류 - 변경하려는 주문을 찾을 수 없음")
	@Test
	void testOrderNotFound() {
		Order order = new Order(1L, 1L, OrderStatus.MEAL.name(), LocalDateTime.now(), 주문항목);
		Long orderId = 1L;

		when(orderDao.findById(eq(orderId))).thenReturn(Optional.empty());

		assertThatThrownBy(() -> {
			orderService.changeOrderStatus(orderId, order);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("orderId에 해당하는 주문정보를 찾을 수 없습니다.");
	}
}
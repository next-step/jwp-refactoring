package kitchenpos.application.mock;

import static kitchenpos.domain.DomainFactory.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.OrderService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
	@InjectMocks
	private OrderService orderService;
	@Mock
	private MenuDao menuDao;
	@Mock
	private OrderDao orderDao;
	@Mock
	private OrderLineItemDao orderLineItemDao;
	@Mock
	private OrderTableDao orderTableDao;

	private Order order;
	private OrderLineItem orderLineItem;

	@BeforeEach
	void setUp() {
		orderLineItem = createOrderLineItem(1L, 2);
		order = createOrder(1L, orderLineItem);
	}

	@DisplayName("주문 항목이 없으면 IllegalArgumentException 발생")
	@Test
	void create_ThrowIllegalArgumentException1() {
		assertThatIllegalArgumentException()
			.isThrownBy(() -> orderService.create(createOrder(1L)));
	}

	@DisplayName("주문 항목에 없는 메뉴가 있으면 IllegalArgumentException 발생")
	@Test
	void create_ThrowIllegalArgumentException2() {
		when(menuDao.countByIdIn(anyList())).thenReturn(0L);

		assertThatIllegalArgumentException()
			.isThrownBy(() -> orderService.create(order));
	}

	@DisplayName("등록되지 않은 테이블이면 IllegalArgumentException 발생")
	@Test
	void create_ThrowIllegalArgumentException3() {
		when(menuDao.countByIdIn(anyList())).thenReturn(1L);
		when(orderTableDao.findById(anyLong())).thenReturn(Optional.empty());

		assertThatIllegalArgumentException()
			.isThrownBy(() -> orderService.create(order));
	}

	@DisplayName("빈 테이블이면 IllegalArgumentException 발생")
	@Test
	void create_ThrowIllegalArgumentException4() {
		when(menuDao.countByIdIn(anyList())).thenReturn(1L);
		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(createOrderTable(1L, 0, true, null)));

		assertThatIllegalArgumentException()
			.isThrownBy(() -> orderService.create(order));
	}

	@DisplayName("주문 성공")
	@Test
	void create() {
		when(menuDao.countByIdIn(anyList())).thenReturn(1L);
		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(createOrderTable(1L, 0, false, null)));
		when(orderDao.save(any(Order.class))).thenAnswer(invocation -> {
			Order order = invocation.getArgument(0, Order.class);
			order.setId(1L);
			return order;
		});
		when(orderLineItemDao.save(any(OrderLineItem.class))).thenAnswer(invocation -> {
			OrderLineItem orderLineItem = invocation.getArgument(0, OrderLineItem.class);
			orderLineItem.setSeq(1L);
			return orderLineItem;
		});

		Order resultOrder = orderService.create(order);

		assertThat(resultOrder.getId()).isNotNull();
		resultOrder.getOrderLineItems().forEach(it -> assertThat(it.getOrderId()).isNotNull());
	}

	@DisplayName("주문 목록 조회")
	@Test
	void list() {
		when(orderDao.findAll()).thenReturn(Collections.singletonList(
			createOrder(1L, 1L, OrderStatus.COOKING.name(), null)
		));
		when(orderLineItemDao.findAllByOrderId(anyLong()))
			.thenReturn(Collections.singletonList(createOrderLineItem(1L, 1L, 1L, 2)));

		List<Order> orders = orderService.list();

		assertThat(orders).hasSize(1);
		assertThat(orders.stream().map(Order::getOrderLineItems).collect(Collectors.toList())).hasSize(1);
	}

	@DisplayName("주문 번호가 없으면 IllegalArgumentException 발생")
	@Test
	void changeOrderStatus_ThrowIllegalArgumentException1() {
		when(orderDao.findById(anyLong())).thenReturn(Optional.empty());

		assertThatIllegalArgumentException()
			.isThrownBy(() -> orderService.changeOrderStatus(1L, createOrder(OrderStatus.MEAL.name())));
	}

	@DisplayName("주문 상태가 완료면 IllegalArgumentException 발생")
	@Test
	void changeOrderStatus_ThrowIllegalArgumentException2() {
		when(orderDao.findById(anyLong()))
			.thenReturn(Optional.of(
				createOrder(1L, 1L, OrderStatus.COMPLETION.name(), Collections.singletonList(orderLineItem))));

		assertThatIllegalArgumentException()
			.isThrownBy(() -> orderService.changeOrderStatus(1L, createOrder(OrderStatus.MEAL.name())));
	}

	@DisplayName("주문 상태 변경")
	@Test
	void changeOrderStatus() {
		when(orderDao.findById(anyLong()))
			.thenReturn(Optional.of(
				createOrder(1L, 1L, OrderStatus.COOKING.name(), Collections.singletonList(orderLineItem))));
		when(orderLineItemDao.findAllByOrderId(anyLong()))
			.thenReturn(Collections.singletonList(createOrderLineItem(1L, 1L, 1L, 2)));

		Order resultOrder = orderService.changeOrderStatus(1L, createOrder(OrderStatus.MEAL.name()));

		verify(orderDao, times(1)).save(any(Order.class));
		assertThat(resultOrder.getId()).isNotNull();
		assertThat(resultOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
	}
}

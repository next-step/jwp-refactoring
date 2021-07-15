package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

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

	private Order order;

	@BeforeEach
	void setUp() {
		OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1L, 1L);
		OrderLineItem orderLineItem2 = new OrderLineItem(2L, 1L, 2L, 2L);
		order = new Order(1L, 1L, OrderStatus.MEAL.name(), LocalDateTime.now(), Arrays.asList(orderLineItem, orderLineItem2));
	}

	@Test
	void orderCreateTest() {
		OrderTable orderTable = new OrderTable(1L, 1L, 1, false);

		when(menuDao.countByIdIn(any())).thenReturn(2L);
		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));
		when(orderDao.save(any())).thenReturn(order);
		when(orderLineItemDao.save(any())).thenReturn(new OrderLineItem());

		assertThat(orderService.create(order)).isNotNull();
	}

	@Test
	@DisplayName("주문을 생성 시 Order line item 이 없을 시 익셉션 발생")
	void orderCreateFailTest() {
		Order order = new Order(1L, 1L, OrderStatus.MEAL.name(), LocalDateTime.now(), Collections.emptyList());
		assertThatThrownBy(() -> orderService.create(order))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("주문 생성 시 order line item 의 개수와 메뉴의 숫자가 일치하지 않으면 익셉션 발생")
	void orderCreateFailTest2() {
		when(menuDao.countByIdIn(any())).thenReturn(1L);

		assertThatThrownBy(() -> orderService.create(order))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("주문 생성 시 주문 테이블이 빈 테이블이면 익셉션 발생")
	void orderCreateFailTest3() {
		OrderTable orderTable = new OrderTable(1L, 1L, 1, true);

		when(menuDao.countByIdIn(any())).thenReturn(2L);
		when(orderTableDao.findById(anyLong())).thenReturn(Optional.of(orderTable));

		assertThatThrownBy(() -> orderService.create(order))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void getOrderListTest() {
		when(orderDao.findAll()).thenReturn(Lists.list(new Order(), new Order()));
		assertThat(orderService.list()).hasSize(2);
	}

	@Test
	void changeOrderStatusTest() {
		Order order = new Order(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), Collections.emptyList());

		when(orderDao.findById(anyLong())).thenReturn(Optional.of(this.order));
		assertThat(orderService.changeOrderStatus(1L, order).getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
	}

	@Test
	@DisplayName("주문상태 변경 시 저장 된 주문이 COMPLETION 상태가 아닌 경우 익셉션 발생생")
	void changeOrderStatusFailTest() {
		Order order = new Order(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), Collections.emptyList());

		when(orderDao.findById(anyLong())).thenReturn(Optional.of(order));
		assertThatThrownBy(() -> orderService.changeOrderStatus(1L, order))
				.isInstanceOf(IllegalArgumentException.class);
	}
}

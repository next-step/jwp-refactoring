package kitchenpos.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderLineItemRepository;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
	@Mock
	private MenuRepository menuRepository;
	@Mock
	private OrderRepository orderRepository;
	@Mock
	private OrderLineItemRepository orderLineItemRepository;
	@Mock
	private OrderTableRepository orderTableRepository;

	@InjectMocks
	private OrderService orderService;

	private Order order;
	private OrderLineItem orderLineItem;

	@BeforeEach
	void setUp() {
		orderLineItem = new OrderLineItem(1L, 1L, 1L, 1L);
		OrderLineItem orderLineItem2 = new OrderLineItem(2L, 1L, 2L, 2L);
		order = new Order(1L, 1L, OrderStatus.MEAL.name(), LocalDateTime.now(), Arrays.asList(orderLineItem, orderLineItem2));
	}

	@Test
	void orderCreateTest() {
		OrderTable orderTable = new OrderTable(1L, 1L, 1, false);

		when(menuRepository.countByIdIn(Lists.list(1L, 2L))).thenReturn(2L);
		when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));
		when(orderRepository.save(order)).thenReturn(order);
		when(orderLineItemRepository.save(orderLineItem)).thenReturn(orderLineItem);

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
		when(menuRepository.countByIdIn(Lists.list(1L, 2L))).thenReturn(1L);

		assertThatThrownBy(() -> orderService.create(order))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("주문 생성 시 주문 테이블이 빈 테이블이면 익셉션 발생")
	void orderCreateFailTest3() {
		OrderTable orderTable = new OrderTable(1L, 1L, 1, true);

		when(menuRepository.countByIdIn(Lists.list(1L, 2L))).thenReturn(2L);
		when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));

		assertThatThrownBy(() -> orderService.create(order))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void getOrderListTest() {
		when(orderRepository.findAll()).thenReturn(Lists.list(new Order(), new Order()));
		assertThat(orderService.list()).hasSize(2);
	}

	@Test
	void changeOrderStatusTest() {
		Order order = new Order(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), Collections.emptyList());

		when(orderRepository.findById(1L)).thenReturn(Optional.of(this.order));
		assertThat(orderService.changeOrderStatus(1L, order).getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
	}

	@Test
	@DisplayName("주문상태 변경 시 저장 된 주문이 COMPLETION 상태가 아닌 경우 익셉션 발생생")
	void changeOrderStatusFailTest() {
		Order order = new Order(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), Collections.emptyList());

		when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
		assertThatThrownBy(() -> orderService.changeOrderStatus(1L, order))
				.isInstanceOf(IllegalArgumentException.class);
	}
}

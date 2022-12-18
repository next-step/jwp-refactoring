package kitchenpos.order.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.exception.CannotChangeOrderStatusException;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	Long ORDER_TABLE_ID = 1L;

	@Mock
	OrderRepository orderRepository;
	@InjectMocks
	OrderService orderService;

	private Order createOrder(OrderStatus orderStatus) {
		return new Order(1L,
						 createMenus(3),
						 ORDER_TABLE_ID,
						 orderStatus,
						 LocalDateTime.now()
						 );
	}

	private Map<Long, Integer> createMenus(int count) {
		return LongStream.range(0, count)
						 .boxed()
						 .collect(Collectors.toMap(Function.identity(), it -> 1, Integer::sum));
	}

	@Test
	@DisplayName("주문 생성")
	void testCreateOrder() {
		// given
		Order expectedOrder = createOrder(OrderStatus.COOKING);

		when(orderRepository.save(any(Order.class))).thenReturn(expectedOrder);
		// when
		Order actualOrder = orderService.create(expectedOrder);

		// then
		verify(orderRepository, times(1)).save(expectedOrder);
		assertThat(actualOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
	}

	@Test
	@DisplayName("주문 목록 조회 성공")
	void testListOrder() {
		// given
		Order expectedOrder1 = createOrder(OrderStatus.COOKING);
		Order expectedOrder2 = createOrder(OrderStatus.MEAL);
		when(orderRepository.findAll()).thenReturn(Lists.newArrayList(expectedOrder1, expectedOrder2));

		// when
		List<Order> actualOrders = orderService.findAll();

		// then
		verify(orderRepository, times(1)).findAll();
		assertThat(actualOrders).containsExactlyInAnyOrder(expectedOrder1, expectedOrder2);
	}

	@Test
	@DisplayName("주문 상태 변경 성공")
	void changeOrderStatus() {
		// given
		Order beforeOrder = createOrder(OrderStatus.COOKING);
		OrderStatus expectedStatus = OrderStatus.MEAL;
		when(orderRepository.findById(anyLong())).thenReturn(Optional.of(beforeOrder));

		// when
		Order actualOrder = orderService.changeOrderStatus(1L, expectedStatus);

		// then
		assertThat(actualOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
	}

	@Test
	@DisplayName("주문 상태 변경 실패")
	void failToChangeOrderStatusWhenOrderStatusIsCompletion() {
		// given
		Order beforeOrder = createOrder(OrderStatus.COMPLETION);
		OrderStatus expectedStatus = OrderStatus.COOKING;
		when(orderRepository.findById(anyLong())).thenReturn(Optional.of(beforeOrder));

		// when
		assertThatThrownBy(() -> orderService.changeOrderStatus(1L, expectedStatus))
			.isInstanceOf(CannotChangeOrderStatusException.class);
	}

}

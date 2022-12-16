package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.exception.CannotChangeOrderStatusException;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	@Mock
	OrderRepository orderRepository;

	@InjectMocks
	OrderService orderService;

	@Deprecated
	static final List<Long> menusId = Lists.newArrayList(1L, 2L, 3L);

	@Test
	@DisplayName("주문 생성")
	void testCreateOrder() {
		// given
		Order expectedOrder = createOrder();

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
		Order expectedOrder1 = createOrder();
		Order expectedOrder = createOrder();
		when(orderRepository.findAll()).thenReturn(Lists.newArrayList(expectedOrder1, expectedOrder));

		// when
		List<Order> actualOrders = orderService.findAll();

		// then
		verify(orderRepository, times(1)).findAll();
		assertThat(actualOrders).containsExactlyInAnyOrder(expectedOrder1, expectedOrder);
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

	private static Order createOrder(OrderStatus orderStatus) {
		return new Order(orderStatus, createOrderTable2(), createMenus(3));
	}

	private static Order createOrder() {
		return new Order(createOrderTable2(), createMenus(3));
	}

	private static Map<Menu, Integer> createMenus(int count) {
		return LongStream.range(0, count)
			.mapToObj(i -> createMenu())
			.collect(Collectors.toMap(Function.identity(), menu -> 1, Integer::sum));
	}

	private static Menu createMenu() {
		return new Menu("menu",
						10_000L,
						new MenuGroup("menu-group"),
						Lists.newArrayList(new Product("product", 1_000L)));
	}

	private static OrderTable createOrderTable2() {
		return new OrderTable(10, true);
	}

}

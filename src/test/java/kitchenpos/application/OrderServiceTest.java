package kitchenpos.application;

import static java.util.Arrays.*;
import static kitchenpos.domain.TextFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;

@DisplayName("주문 요구사항 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	@Mock
	private MenuRepository menuRepository;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private OrderTableRepository orderTableRepository;

	@InjectMocks
	private OrderService orderService;

	@DisplayName("주문 생성시 등록이 안된 메뉴는 주문할 수 없다.")
	@Test
	void createOrderWithUnknownMenusTest() {
		// given
		OrderRequest orderRequest = mock(OrderRequest.class);
		when(orderRequest.getOrderLineItemSize()).thenReturn(1);
		when(menuRepository.findAllById(anyList())).thenReturn(Collections.emptyList());

		// when
		// then
		assertThatThrownBy(() -> orderService.create(orderRequest))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("등록이 안된 메뉴는 주문할 수 없습니다.");
	}

	@DisplayName("등록이 안된 주문테이블에서 주문할 수 없다.")
	@Test
	void createOrderWithUnknownOrderTableTest() {
		// given
		OrderRequest orderRequest = mock(OrderRequest.class);
		when(orderRequest.getOrderLineItemSize()).thenReturn(1);
		when(menuRepository.findAllById(anyList())).thenReturn(asList(mock(Menu.class)));
		when(orderTableRepository.findById(anyLong())).thenReturn(Optional.empty());

		// when
		// then
		assertThatThrownBy(() -> orderService.create(orderRequest))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("등록이 안된 주문 테이블에서는 주문할 수 없습니다.");
	}

	@DisplayName("주문 목록을 조회할 수 있다.")
	@Test
	void orderListTest() {
		// given
		Order order = mock(Order.class);
		when(order.getId()).thenReturn(1L);
		when(order.getOrderStatus()).thenReturn(OrderStatus.MEAL);
		when(orderRepository.findAll()).thenReturn(asList(order));

		// when
		List<OrderResponse> findOrders = orderService.findAll();

		// then
		assertThat(findOrders.size()).isEqualTo(1);
		assertThat(findOrders.get(0).getId()).isEqualTo(1L);
	}

	@DisplayName("주문 상태가 완료인 경우 변경할 수 없다.")
	@Test
	void changeStatusOfCompletedOrderTest() {
		Order completedOrder = Order.create(asList(new OrderLineItem(주문_메뉴_후라이드, 2)),
			new OrderTable(1, false), LocalDateTime.now());
		completedOrder.complete();

		when(orderRepository.findById(anyLong())).thenReturn(Optional.of(completedOrder));

		assertThatThrownBy(() -> orderService.changeOrderStatus(1L, OrderStatus.MEAL))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("계산 완료 주문은 상태를 변경할 수 없습니다.");
	}

	@DisplayName("등록이 안된 주문은 상태를 변경할 수 없다.")
	@Test
	void changeStatusOfUnknownOrderTest() {
		when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> orderService.changeOrderStatus(1L, OrderStatus.MEAL))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("등록이 안된 주문은 상태를 변경할 수 없습니다.");
	}

	@DisplayName("주문의 상태를 변경할 수 있다.")
	@Test
	void changeOrderStatusTest() {
		Order order = mock(Order.class);

		when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

		verifyToChangeOrderStatus(order);
	}

	private void verifyToChangeOrderStatus(Order order) {
		for (OrderStatus orderStatus : OrderStatus.values()) {
			when(order.getOrderStatus()).thenReturn(orderStatus);

			orderService.changeOrderStatus(1L, orderStatus);

			verify(order).changeStatus(orderStatus);
		}
	}
}

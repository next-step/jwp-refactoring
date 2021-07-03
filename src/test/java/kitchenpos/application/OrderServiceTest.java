package kitchenpos.application;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.OrderRepository;
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
		when(menuRepository.countByIdIn(anyList())).thenReturn(0L);

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
		when(menuRepository.countByIdIn(anyList())).thenReturn(1L);
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
		List<OrderResponse> findOrders = orderService.list();

		// then
		assertThat(findOrders.size()).isEqualTo(1);
		assertThat(findOrders.get(0).getId()).isEqualTo(1L);
	}

}

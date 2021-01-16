package kitchenpos.mockito;

import kitchenpos.order.application.OrderService;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.*;
import kitchenpos.order.domain.Orders;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@Disabled
public class OrdersServiceTest {
	@Mock
	private MenuRepository menuRepository;

	@Mock
	private OrderRepository orderRepository;

	@Mock
	private OrderLineItemRepository orderLineItemRepository;

	@Mock
	private OrderTableRepository orderTableRepository;

	private OrderService orderService;

	@Mock
	private Orders orders;

	@BeforeEach
	void setUp() {
		orderService = new OrderService(menuRepository, orderRepository, orderLineItemRepository, orderTableRepository);
		assertThat(orderService).isNotNull();
		orders = mock(Orders.class);
	}

	@Test
	@DisplayName("주문을 등록한다")
	void create() {
		given(orders.getId()).willReturn(1L);
//		given(orders.getOrderTableId()).willReturn(1L);
		given(menuRepository.countByIdIn(anyList())).willReturn(2);
		given(orderTableRepository.findById(any())).willReturn(Optional.of(mock(OrderTable.class)));
		given(orders.getOrderLineItems()).willReturn(new ArrayList<>(Arrays.asList(mock(OrderLineItem.class), mock(OrderLineItem.class))));

		given(orderRepository.save(any())).willReturn(orders);
//		assertThat(orderService.create(orders).getOrderTableId()).isEqualTo(1L);
	}

	@Test
	@DisplayName("주문을 조회한다")
	void list() {
		given(orderRepository.findAll()).willReturn(new ArrayList<>(Arrays.asList(mock(Orders.class), mock(Orders.class))));
		given(orders.getOrderLineItems()).willReturn(new ArrayList<>(Arrays.asList(mock(OrderLineItem.class), mock(OrderLineItem.class))));

		assertNotNull(orderService.list());
		assertThat(orderService.list().size()).isEqualTo(2);
	}

	@Test
	@DisplayName("계산 완료 상태의 주문이 조회될 순 없다")
	void givenOrderStatusCompletionWhenFindOrderThenError() {
		given(orders.getOrderStatus()).willReturn(OrderStatus.COMPLETION.name());
		given(orderRepository.findById(any())).willReturn(Optional.ofNullable(orders));

		assertThat(orderRepository.findById(1L).get().getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
		assertThrows(IllegalArgumentException.class, () -> orderService.changeOrderStatus(1L, any()));
	}

}
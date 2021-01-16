package kitchenpos.domain;

import kitchenpos.application.OrderService;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class OrderServiceTest {
	@Mock
	private MenuDao menuDao;

	@Mock
	private OrderDao orderDao;

	@Mock
	private OrderLineItemDao orderLineItemDao;

	@Mock
	private OrderTableDao orderTableDao;

	private OrderService orderService;

	@Mock
	private Order order;

	@BeforeEach
	void setUp() {
		orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
		assertThat(orderService).isNotNull();
		order = mock(Order.class);
	}

	@Test
	@DisplayName("주문을 등록한다")
	void create() {
		given(order.getId()).willReturn(1L);
		given(order.getOrderTableId()).willReturn(1L);
		given(menuDao.countByIdIn(anyList())).willReturn(2L);
		given(orderTableDao.findById(any())).willReturn(Optional.of(mock(OrderTable.class)));
		given(order.getOrderLineItems()).willReturn(new ArrayList<>(Arrays.asList(mock(OrderLineItem.class), mock(OrderLineItem.class))));

		given(orderDao.save(any())).willReturn(order);
		assertThat(orderService.create(order).getOrderTableId()).isEqualTo(1L);
	}

	@Test
	@DisplayName("주문을 조회한다")
	void list() {
		given(orderDao.findAll()).willReturn(new ArrayList<>(Arrays.asList(mock(Order.class), mock(Order.class))));
		given(order.getOrderLineItems()).willReturn(new ArrayList<>(Arrays.asList(mock(OrderLineItem.class), mock(OrderLineItem.class))));

		assertNotNull(orderService.list());
		assertThat(orderService.list().size()).isEqualTo(2);
	}

	@Test
	@DisplayName("계산 완료 상태의 주문이 조회될 순 없다")
	void givenOrderStatusCompletionWhenFindOrderThenError() {
		given(order.getOrderStatus()).willReturn(OrderStatus.COMPLETION.name());
		given(orderDao.findById(any())).willReturn(Optional.of(order));

		assertThat(orderDao.findById(1L).get().getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
		assertThrows(IllegalArgumentException.class, () -> orderService.changeOrderStatus(1L, any()));
	}

}
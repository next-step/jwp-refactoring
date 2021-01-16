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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
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

	@BeforeEach
	void setUp() {
		orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);
		assertThat(orderService).isNotNull();
	}

	@Test
	@DisplayName("주문을 등록한다")
	void create() {
//		- 주문 항목은 필수다. (orderLineItems)
//		- 주문 항목과 메뉴는 서로 포함되어야한다.
		when(menuDao.countByIdIn(anyList())).thenReturn(2L);
		OrderTable orderTable = new OrderTable();
		orderTable.setId(1L);
		when(orderTableDao.findById(any())).thenReturn(Optional.of(orderTable));

		Order order = new Order();
		order.setId(1L);
		List<OrderLineItem> orderLineItems = new ArrayList<>(Arrays.asList(new OrderLineItem(), new OrderLineItem()));
		order.setOrderLineItems(orderLineItems);
		when(orderDao.save(any())).thenReturn(order);

		assertThat(orderService.create(order).getOrderTableId()).isEqualTo(1L);
	}

	@Test
	@DisplayName("주문을 조회한다")
	void list() {
		when(orderDao.findAll()).thenReturn(new ArrayList<>(Arrays.asList(new Order(), new Order())));

		List<OrderLineItem> orderLineItems = new ArrayList<>(Arrays.asList(new OrderLineItem(), new OrderLineItem(), new OrderLineItem()));
		when(orderLineItemDao.findAllByOrderId(any())).thenReturn(orderLineItems);

		assertNotNull(orderService.list());
		assertThat(orderService.list().size()).isEqualTo(2);
	}


	@Test
	@DisplayName("계산 완료 상태의 주문이 조회될 순 없다")
	void givenOrderStatusCompletionWhenFindOrderThenError() {
		Order order = new Order();
		order.setOrderStatus(OrderStatus.COMPLETION.name());
		when(orderDao.findById(any())).thenReturn(Optional.of(order));

		assertThat(orderDao.findById(1L).get().getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name());
		assertThrows(IllegalArgumentException.class, () -> orderService.changeOrderStatus(1L, any()));
	}

}
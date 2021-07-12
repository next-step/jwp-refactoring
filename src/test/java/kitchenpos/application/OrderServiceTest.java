package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
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

	@Test
	void createTestInHappyCase() {
		// given
		when(menuDao.countByIdIn(any())).thenReturn(1L);
		when(orderTableDao.findById(any())).thenReturn(Optional.of(new OrderTable()));
		when(orderDao.save(any())).thenReturn(new Order());
		when(orderLineItemDao.save(any())).thenReturn(new OrderLineItem());
		// when
		Order order = orderService.create(new Order(1L, "COOKING", LocalDateTime.now(), Arrays.asList(new OrderLineItem())));
		// then
		assertThat(order.getOrderLineItems().size()).isEqualTo(1);
	}

	@Test
	void listTestInHappyCase() {
		// given
		when(orderDao.findAll()).thenReturn(Arrays.asList(new Order()));
		when(orderLineItemDao.findAllByOrderId(any())).thenReturn(Arrays.asList(new OrderLineItem()));
		// when
		List<Order> orders = orderService.list();
		// then
		assertThat(orders.size()).isEqualTo(1);
	}

	@Test
	void changeOrderStatusTestInHappyCase() {
		// given
		when(orderDao.findById(any())).thenReturn(
			Optional.of(new Order(1L, "COOKING", LocalDateTime.now(), Arrays.asList(new OrderLineItem()))));
		// when
		Order order = orderService.changeOrderStatus(1L, new Order(1L, "COMPLETION", LocalDateTime.now(), Arrays.asList(new OrderLineItem())));
		// then
		assertThat(order.getOrderStatus()).isEqualTo("COMPLETION");
	}
}

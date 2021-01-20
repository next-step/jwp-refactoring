package kitchenpos.application;

import static java.util.Optional.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;

/**
 * @author : byungkyu
 * @date : 2021/01/20
 * @description :
 **/
@DisplayName("주문")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
	@Mock
	MenuDao menuDao;
	@Mock
	OrderDao orderDao;
	@Mock
	OrderLineItemDao orderLineItemDao;
	@Mock
	OrderTableDao orderTableDao;

	@DisplayName("주문을 등록할 수 있다.")
	@Test
	void create(){
		// given
		Order order = mock(Order.class);

		OrderLineItem orderLineItem = mock(OrderLineItem.class);
		when(orderLineItem.getMenuId()).thenReturn(1L);
		when(menuDao.countByIdIn(any())).thenReturn(1L);
		when(order.getOrderLineItems()).thenReturn(Arrays.asList(orderLineItem));

		OrderTable orderTable = mock(OrderTable.class);
		when(orderTable.getId()).thenReturn(1L);
		when(orderTableDao.findById(any())).thenReturn(ofNullable(orderTable));

		Order savedOrder = mock(Order.class);
		when(savedOrder.getId()).thenReturn(1L);
		when(orderDao.save(any())).thenReturn(savedOrder);
		OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

		// when
		Order finalSavedOrder = orderService.create(order);

		// then
		assertThat(finalSavedOrder.getId()).isNotNull();
	}


	@DisplayName("주문항목이 존재하지 않으면 등록할 수 없다.")
	@Test
	void whenOrderLineItemsIsEmpty(){
		// given
		Order order = mock(Order.class);
		OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

		// when - then
		assertThatThrownBy(() -> {
			orderService.create(order);
		}).isInstanceOf(IllegalArgumentException.class);

	}

	@DisplayName("주문항목과 메뉴의 항목들이 일치해야 주문할 수 있다.")
	@Test
	void orderLineItemsSizeMustExistInMenu(){
		// given
		Order order = mock(Order.class);

		OrderLineItem orderLineItem = mock(OrderLineItem.class);
		when(order.getOrderLineItems()).thenReturn(Arrays.asList(orderLineItem));
		when(menuDao.countByIdIn(any())).thenReturn(3L);
		OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

		// when - then
		assertThatThrownBy(() -> {
			orderService.create(order);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("빈 테이블은 주문을 할 수 없다.")
	@Test
	void empTyOrderTableCannotOrder(){
		// given
		// given
		Order order = mock(Order.class);

		OrderLineItem orderLineItem = mock(OrderLineItem.class);
		when(order.getOrderLineItems()).thenReturn(Arrays.asList(orderLineItem));
		when(menuDao.countByIdIn(any())).thenReturn(1L);

		when(orderTableDao.findById(any())).thenThrow(IllegalArgumentException.class);
		OrderService orderService = new OrderService(menuDao, orderDao, orderLineItemDao, orderTableDao);

		// when - then
		assertThatThrownBy(() -> {
			orderService.create(order);
		}).isInstanceOf(IllegalArgumentException.class);
	}

}
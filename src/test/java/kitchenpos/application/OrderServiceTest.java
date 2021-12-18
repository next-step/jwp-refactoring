package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
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
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

	@InjectMocks
	private OrderService orderService;

	@Mock
	private MenuDao menuDao;

	@Mock
	private OrderTableDao orderTableDao;

	@Mock
	private OrderDao orderDao;

	@Mock
	private OrderLineItemDao orderLineItemDao;

	@DisplayName("주문을 생성한다")
	@Test
	void createTest() {
		// given
		OrderLineItem item1 = new OrderLineItem();
		item1.setMenuId(1L);
		item1.setQuantity(1);
		List<OrderLineItem> orderLineItems = new ArrayList<>();
		orderLineItems.add(item1);

		Order persist = new Order();
		persist.setId(1L);
		persist.setOrderTableId(1L);
		persist.setOrderLineItems(orderLineItems);

		OrderTable orderTable = new OrderTable();
		orderTable.setId(1L);
		orderTable.setEmpty(false);

		given(menuDao.countByIdIn(any())).willReturn((long)orderLineItems.size());
		given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));
		given(orderDao.save(any())).willReturn(persist);
		given(orderLineItemDao.save(any())).willReturn(item1);

		Order request = new Order();
		request.setOrderTableId(1L);
		request.setOrderLineItems(orderLineItems);

		// when
		Order order = orderService.create(request);

		// then
		assertThat(order.getId()).isEqualTo(persist.getId());
	}

	@DisplayName("주문 시, 주문 항목이 1개 이상이어야 한다")
	@Test
	void createTest2() {
		// given
		Order request = new Order();
		request.setOrderTableId(1L);
		request.setOrderLineItems(new ArrayList<>());

		// when, then
		assertThatThrownBy(() -> orderService.create(request))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 시, 모든 주문 항목 메뉴가 존재해야 한다")
	@Test
	void createTest3() {
		// given
		OrderLineItem item1 = new OrderLineItem();
		item1.setMenuId(1L);
		item1.setQuantity(1);
		List<OrderLineItem> orderLineItems = new ArrayList<>();
		orderLineItems.add(item1);

		given(menuDao.countByIdIn(any())).willReturn(0L);

		Order request = new Order();
		request.setOrderTableId(1L);
		request.setOrderLineItems(orderLineItems);

		// when, then
		assertThatThrownBy(() -> orderService.create(request))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 시, 주문 테이블이 존재해야 한다")
	@Test
	void createTest4() {
		// given
		OrderLineItem item1 = new OrderLineItem();
		item1.setMenuId(1L);
		item1.setQuantity(1);
		List<OrderLineItem> orderLineItems = new ArrayList<>();
		orderLineItems.add(item1);

		given(menuDao.countByIdIn(any())).willReturn((long)orderLineItems.size());
		given(orderTableDao.findById(any())).willReturn(Optional.empty());

		Order request = new Order();
		request.setOrderTableId(1L);
		request.setOrderLineItems(orderLineItems);

		// when, then
		assertThatThrownBy(() -> orderService.create(request))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 시, 주문 테이블이 빈 테이블 상태가 아니어야 한다")
	@Test
	void createTest5() {
		// given
		OrderLineItem item1 = new OrderLineItem();
		item1.setMenuId(1L);
		item1.setQuantity(1);
		List<OrderLineItem> orderLineItems = new ArrayList<>();
		orderLineItems.add(item1);

		OrderTable orderTable = new OrderTable();
		orderTable.setId(1L);
		orderTable.setEmpty(true);

		given(menuDao.countByIdIn(any())).willReturn((long)orderLineItems.size());
		given(orderTableDao.findById(any())).willReturn(Optional.of(orderTable));

		Order request = new Order();
		request.setOrderTableId(1L);
		request.setOrderLineItems(orderLineItems);

		// when, then
		assertThatThrownBy(() -> orderService.create(request))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 목록을 조회한다")
	@Test
	void listTest() {
		// given
		OrderLineItem item1 = new OrderLineItem();
		item1.setMenuId(1L);
		item1.setQuantity(1);

		Order order = new Order();
		order.setId(1L);
		order.setOrderLineItems(Arrays.asList(item1));
		List<Order> persist = new ArrayList<>();
		persist.add(order);

		given(orderDao.findAll()).willReturn(persist);
		given(orderLineItemDao.findAllByOrderId(any())).willReturn(Arrays.asList(item1));

		// when
		List<Order> results = orderService.list();

		// then
		assertThat(results.size()).isEqualTo(1);
	}

	@DisplayName("주문 상태를 변경한다")
	@Test
	void changeOrderStatusTest() {
		// given
		Long requestOrderId = 1L;
		Order request = new Order();
		request.setOrderStatus(OrderStatus.MEAL.name());

		Order persist = new Order();
		persist.setId(1L);
		persist.setOrderStatus(OrderStatus.COOKING.name());

		given(orderDao.findById(requestOrderId)).willReturn(Optional.of(persist));
		given(orderLineItemDao.findAllByOrderId(any())).willReturn(null);

		// when
		Order result = orderService.changeOrderStatus(requestOrderId, request);

		// then
		assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
	}

	@DisplayName("주문 상태를 변경 시, 주문 상태가 완료가 아니어야 한다")
	@Test
	void changeOrderStatusTest2() {
		// given
		Long requestOrderId = 1L;
		Order request = new Order();
		request.setOrderStatus(OrderStatus.MEAL.name());

		Order persist = new Order();
		persist.setId(1L);
		persist.setOrderStatus(OrderStatus.COMPLETION.name());

		given(orderDao.findById(requestOrderId)).willReturn(Optional.of(persist));

		// when
		assertThatThrownBy(() ->
			orderService.changeOrderStatus(requestOrderId, request)
		).isInstanceOf(IllegalArgumentException.class);
	}
	
}

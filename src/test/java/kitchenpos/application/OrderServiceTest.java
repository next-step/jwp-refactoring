package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Collections;
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

@DisplayName("주문 : 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	@Mock
	MenuDao menuDao;

	@Mock
	OrderDao orderDao;

	@Mock
	OrderTableDao orderTableDao;

	@Mock
	OrderLineItemDao orderLineItemDao;

	@Mock
	Order order;

	@Mock
	OrderTable orderTable;

	@InjectMocks
	private OrderService orderService;

	@DisplayName("주문 생성시 주문 항목이 비어있는 경우 예외처리 테스트")
	@Test
	void createOrderEmptyOrderLineItems() {
		// when
		when(order.getOrderLineItems()).thenReturn(Collections.emptyList());

		// then
		assertThatThrownBy(() -> {
			orderService.create(order);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 생성시 주문 항목이 메뉴에 존재하지 않은 경우 예외처리 테스트")
	@Test
	void createOrderCompareMenuSize() {
		// when
		when(order.getOrderLineItems()).thenReturn(Collections.singletonList(new OrderLineItem()));
		when(menuDao.countByIdIn(anyList())).thenReturn(3L);

		// then
		assertThatThrownBy(() -> {
			orderService.create(order);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 생성시 주문 테이블이 존재하지 않은 경우 예외처리 테스트")
	@Test
	void createOrderUnknownOrderTable() {
		// given
		given(order.getOrderLineItems()).willReturn(Collections.singletonList(new OrderLineItem()));
		given(menuDao.countByIdIn(anyList())).willReturn(1L);

		// when
		when(orderTableDao.findById(order.getOrderTableId())).thenThrow(IllegalArgumentException.class);

		// then
		assertThatThrownBy(() -> {
			orderService.create(order);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 생성시 주문 테이블이 비어있는 경우 예외처리 테스트")
	@Test
	void createOrderEmptyOrderTable() {
		// given
		given(order.getOrderLineItems()).willReturn(Collections.singletonList(new OrderLineItem()));
		given(menuDao.countByIdIn(anyList())).willReturn(1L);
		given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(orderTable));

		// when
		when(orderTable.isEmpty()).thenReturn(true);

		// then
		assertThatThrownBy(() -> {
			orderService.create(order);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 생성 테스트")
	@Test
	void createOrder() {
		// given
		given(order.getOrderLineItems()).willReturn(Collections.singletonList(new OrderLineItem()));
		given(menuDao.countByIdIn(anyList())).willReturn(1L);
		given(orderTableDao.findById(order.getOrderTableId())).willReturn(Optional.of(orderTable));
		given(orderTable.isEmpty()).willReturn(false);

		// when
		when(orderDao.save(order)).thenReturn(order);

		// then
		assertThat(orderService.create(order)).isEqualTo(order);
	}

	@DisplayName("주문 목록 조회 테스트")
	@Test
	void getList() {
		// given
		given(orderLineItemDao.findAllByOrderId(anyLong())).willReturn(Collections.singletonList(new OrderLineItem()));

		// when
		when(orderDao.findAll()).thenReturn(Collections.singletonList(order));

		// then
		assertThat(orderService.list()).containsExactly(order);
	}

	@DisplayName("주문 상태 변경시 존재하지 않는 주문인 경우 예외처리 테스트")
	@Test
	void changeOrderStatusUnknownOrder() {
		// when
		when(orderDao.findById(anyLong())).thenThrow(IllegalArgumentException.class);

		// then
		assertThatThrownBy(() -> {
			orderService.changeOrderStatus(anyLong(), order);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 상태 변경시 이미 완료 상태인 경우 예외처리 테스트")
	@Test
	void changeOrderCompletionStatus() {
		// given
		given(orderDao.findById(anyLong())).willReturn(Optional.of(order));

		// when
		when(order.getOrderStatus()).thenReturn(OrderStatus.COMPLETION.name());

		// then
		assertThatThrownBy(() -> {
			orderService.changeOrderStatus(anyLong(), order);
		}).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 상태 변경 테스트")
	@Test
	void changeOrderStatus() {
		// given
		given(orderDao.findById(anyLong())).willReturn(Optional.of(order));
		given(order.getOrderStatus()).willReturn(OrderStatus.MEAL.name());

		// when
		when(orderDao.save(order)).thenReturn(order);

		// then
		assertThat(orderService.changeOrderStatus(anyLong(), order)).isEqualTo(order);
	}
}

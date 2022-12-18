package kitchenpos.application;

import static kitchenpos.generator.OrderGenerator.*;
import static kitchenpos.generator.OrderTableGenerator.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.generator.OrderLineItemGenerator;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

@DisplayName("주문 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

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

	@DisplayName("주문을 등록할 수 있다.")
	@Test
	void createOrderTest() {
		// given
		long orderTableId = 1L;
		Order orderRequest = 주문(1L, orderTableId, OrderStatus.COOKING,
			Collections.singletonList(OrderLineItemGenerator.주문_품목()));
		given(menuDao.countByIdIn(anyList())).willReturn(orderTableId);
		given(orderTableDao.findById(orderTableId)).willReturn(Optional.of(비어있지_않은_5명_테이블()));

		Order 조리중_주문 = 조리중_주문();
		given(orderDao.save(any())).willReturn(조리중_주문);

		// when
		orderService.create(orderRequest);

		// then
		주문_등록_됨(orderTableId);
	}

	@DisplayName("주문 항목이 없으면 주문을 등록할 수 없다.")
	@Test
	void createOrderWithoutOrderLineItemsTest() {
		// given
		long orderTableId = 1L;
		Order orderRequest = 주문(orderTableId, OrderStatus.COOKING,
			Collections.emptyList());

		// when
		Throwable throwable = catchThrowable(() -> orderService.create(orderRequest));

		// then
		assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("메뉴가 등록되어 있지 않으면 주문을 등록할 수 없다.")
	@Test
	void createOrderWithoutMenuTest() {
		// given
		long orderTableId = 1L;
		Order orderRequest = 주문(orderTableId, OrderStatus.COOKING,
			Collections.singletonList(OrderLineItemGenerator.주문_품목()));
		given(menuDao.countByIdIn(anyList())).willReturn(0L);

		// when
		Throwable throwable = catchThrowable(() -> orderService.create(orderRequest));

		// then
		assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 테이블이 등록되어 있지 않으면 주문을 등록할 수 없다.")
	@Test
	void createOrderWithNotSavedTableTest() {
		// given
		long orderTableId = 1L;
		Order orderRequest = 주문(orderTableId, OrderStatus.COOKING,
			Collections.singletonList(OrderLineItemGenerator.주문_품목()));
		given(menuDao.countByIdIn(anyList())).willReturn(orderTableId);
		given(orderTableDao.findById(orderTableId)).willReturn(Optional.empty());

		// when
		Throwable throwable = catchThrowable(() -> orderService.create(orderRequest));

		// then
		assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 테이블이 비어있으면 주문을 등록할 수 없다.")
	@Test
	void createOrderWithEmptyTableTest() {
		// given
		long orderTableId = 1L;
		Order orderRequest = 주문(orderTableId, OrderStatus.COOKING,
			Collections.singletonList(OrderLineItemGenerator.주문_품목()));
		given(menuDao.countByIdIn(anyList())).willReturn(orderTableId);
		given(orderTableDao.findById(orderTableId)).willReturn(Optional.of(비어있는_테이블()));

		// when
		Throwable throwable = catchThrowable(() -> orderService.create(orderRequest));

		// then
		assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문을 조회할 수 있다.")
	@Test
	void listOrderTest() {
		// given
		Order order = 조리중_주문();
		given(orderDao.findAll()).willReturn(Collections.singletonList(order));

		// when
		orderService.list();

		// then
		verify(orderDao, only()).findAll();
		verify(orderLineItemDao, only()).findAllByOrderId(order.getId());
	}

	@DisplayName("주문 상태를 변경할 수 있다.")
	@Test
	void changeOrderStatusTest() {
		// given
		OrderStatus orderStatus = OrderStatus.MEAL;
		Order orderUpdateRequest = orderUpdateRequest(orderStatus);
		Order order = 조리중_주문();
		given(orderDao.findById(anyLong())).willReturn(Optional.of(order));

		// when
		orderService.changeOrderStatus(order.getId(), orderUpdateRequest);

		// then
		주문_상태_변경됨(orderStatus);
	}

	@DisplayName("이미 등록된 주문이 아니면 주문 상태를 변경할 수 없다.")
	@Test
	void changeOrderStatusWithNotSavedOrderTest() {
		// given
		OrderStatus orderStatus = OrderStatus.MEAL;
		Order orderUpdateRequest = orderUpdateRequest(orderStatus);
		given(orderDao.findById(anyLong())).willReturn(Optional.empty());

		// when
		Throwable throwable = catchThrowable(() -> orderService.changeOrderStatus(1L, orderUpdateRequest));

		// then
		assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 상태가 계산 완료이면 주문 상태를 변경할 수 없다.")
	@Test
	void changeOrderStatusWithCompletedOrderTest() {
		// given
		OrderStatus orderStatus = OrderStatus.MEAL;
		Order orderUpdateRequest = orderUpdateRequest(orderStatus);
		Order order = 계산_완료_주문();
		given(orderDao.findById(anyLong())).willReturn(Optional.of(order));

		// when
		Throwable throwable = catchThrowable(() -> orderService.changeOrderStatus(1L, orderUpdateRequest));

		// then
		assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
	}

	private void 주문_상태_변경됨(OrderStatus orderStatus) {
		ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
		verify(orderDao, times(1)).save(captor.capture());
		Order savedOrder = captor.getValue();
		assertThat(savedOrder.getOrderStatus()).isEqualTo(orderStatus.name());
	}

	private Order orderUpdateRequest(OrderStatus orderStatus) {
		return 주문(1L, orderStatus, Collections.emptyList());
	}

	private void 주문_등록_됨(long orderTableId) {
		ArgumentCaptor<Order> captor = ArgumentCaptor.forClass(Order.class);
		verify(orderDao, only()).save(captor.capture());
		Order savedOrder = captor.getValue();
		assertAll(
			() -> assertThat(savedOrder.getId()).isEqualTo(orderTableId),
			() -> assertThat(savedOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name()),
			() -> assertThat(savedOrder.getOrderLineItems()).hasSize(1)
		);
	}

}

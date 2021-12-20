package kitchenpos.application;

import static kitchenpos.menu.MenuFixture.*;
import static kitchenpos.order.OrderFixture.*;
import static kitchenpos.ordertable.OrderTableFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import kitchenpos.domain.OrderStatus;

@DisplayName("주문 단위 테스트")
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

	@DisplayName("주문을 등록한다.")
	@Test
	void register() {
		// given
		given(menuDao.countByIdIn(any())).willReturn((long)후라이드후라이드_주문().getOrderLineItems().size());
		given(orderTableDao.findById(any())).willReturn(Optional.of(비어있지않은_주문_테이블()));
		given(orderDao.save(any())).willReturn(후라이드후라이드_주문());
		given(orderLineItemDao.save(any())).willReturn(후라이드후라이드_주문_항목());

		// when
		Order order = orderService.create(주문_요청(비어있지않은_주문_테이블().getId(), 후라이드후라이드_메뉴().getId(), 1).toOrder());

		// then
		assertAll(
			() -> assertThat(order).isNotNull(),
			() -> assertThat(order.getId()).isNotNull());
	}

	@DisplayName("주문 항목이 없는 경우 주문을 등록할 수 없다.")
	@Test
	void registerFailOnEmptyOrderLineItem() {
		// when
		ThrowingCallable throwingCallable = () ->
			orderService.create(주문_항목_없는_주문_요청(비어있지않은_주문_테이블().getId()).toOrder());

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("주문 항목이 등록되지 않은 메뉴라면 주문을 등록할 수 없다.")
	@Test
	void registerFailOnNotFoundMenu() {
		// given
		given(menuDao.countByIdIn(any())).willReturn(0L);
		Long unknownMenuId = 0L;

		// when
		ThrowingCallable throwingCallable = () ->
			orderService.create(주문_요청(비어있지않은_주문_테이블().getId(), unknownMenuId, 1).toOrder());

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("주문 테이블이 비어있다면 주문을 등록할 수 없다.")
	@Test
	void registerFailOnEmptyOrderTable() {
		// given
		given(menuDao.countByIdIn(any())).willReturn((long)후라이드후라이드_주문().getOrderLineItems().size());
		given(orderTableDao.findById(any())).willReturn(Optional.of(빈_주문_테이블()));

		// when
		ThrowingCallable throwingCallable = () ->
			orderService.create(주문_요청(빈_주문_테이블().getId(), 후라이드후라이드_메뉴().getId(), 1).toOrder());

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("주문 목록을 조회할 수 있다.")
	@Test
	void findAll() {
		// given
		given(orderDao.findAll()).willReturn(Collections.singletonList(후라이드후라이드_주문()));
		given(orderLineItemDao.findAllByOrderId(any())).willReturn(Collections.singletonList(후라이드후라이드_주문_항목()));

		// when
		List<Order> orders = orderService.list();

		// then
		List<Long> actualIds = orders.stream()
			.map(Order::getId)
			.collect(Collectors.toList());
		List<Long> expectedIds = Stream.of(후라이드후라이드_주문())
			.map(Order::getId)
			.collect(Collectors.toList());
		assertThat(actualIds).containsAll(expectedIds);
	}

	@DisplayName("주문 상태를 변경할 수 있다.")
	@Test
	void changeOrderStatus() {
		// given
		OrderStatus orderStatus = OrderStatus.MEAL;
		given(orderDao.findById(any())).willReturn(Optional.of(후라이드후라이드_주문()));
		given(orderDao.save(any())).willReturn(후라이드후라이드_주문_상태_변경됨(orderStatus));
		given(orderLineItemDao.findAllByOrderId(any())).willReturn(Collections.singletonList(후라이드후라이드_주문_항목()));

		// when
		Order order = orderService.changeOrderStatus(후라이드후라이드_주문().getId(), 주문_상태_변경_요청(orderStatus).toOrder());

		// then
		assertThat(order.getOrderStatus()).isEqualTo(orderStatus.name());
	}

	@DisplayName("주문 상태가 완료라면 변경할 수 없다.")
	@Test
	void changeOrderStatusFailOnCompleted() {
		// given
		given(orderDao.findById(any())).willReturn(Optional.of(후라이드후라이드_주문_상태_변경됨(OrderStatus.COMPLETION)));

		// when
		ThrowingCallable throwingCallable = () ->
			orderService.changeOrderStatus(후라이드후라이드_주문().getId(), 주문_상태_변경_요청(OrderStatus.MEAL).toOrder());

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}
}

package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
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

	private OrderTable orderTable;
	private OrderLineItem orderLineItem1;
	private OrderLineItem orderLineItem2;
	private List<OrderLineItem> orderLineItems;

	@BeforeEach
	void setUp() {
		orderTable = OrderTable.of(1L, 1L, 2, false);
		orderLineItem1 = OrderLineItem.of(1L, 1L, 1L, 1);
		orderLineItem2 = OrderLineItem.of(2L, 1L, 2L, 2);
		orderLineItems = Arrays.asList(orderLineItem1, orderLineItem2);
	}

	@DisplayName("주문: 주문 등록 테스트")
	@Test
	void createOrderTest() {
		// given
		Order order = Order.of(1L, orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
		given(menuDao.countByIdIn(any())).willReturn((long)orderLineItems.size());
		given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.of(orderTable));
		given(orderDao.save(order)).willReturn(order);
		given(orderLineItemDao.save(any())).willReturn(orderLineItem1, orderLineItem2);

		// when
		Order actual = orderService.create(order);

		// then
		assertAll(
			() -> assertThat(actual).isNotNull(),
			() -> assertThat(actual.getId()).isNotNull(),
			() -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name())
		);
	}

	@DisplayName("주문[예외]: 주문 등록 테스트(1. 주문 아이템이 있어야 한다.)")
	@Test
	void errorOrderListNullTest() {
		// given // when
		Order order = Order.of(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), null);

		// then
		assertThatIllegalArgumentException().isThrownBy(
			() -> orderService.create(order)
		);
	}

	@DisplayName("주문[예외]: 주문 등록 테스트(1. 주문 아이템이 있어야 한다.)")
	@Test
	void errorOrderListEmptyTest() {
		// given // when
		Order order = Order.of(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), new ArrayList<>());

		// then
		assertThatIllegalArgumentException().isThrownBy(
			() -> orderService.create(order)
		);
	}

	@DisplayName("주문[예외]: 주문 등록 테스트(2. 주문하려는 메뉴가 등록되어 있어야 한다.)")
	@Test
	void errorNotFoundMenuTest() {
		// given
		Order order = Order.of(1L, orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);

		// when
		when(menuDao.countByIdIn(any())).thenReturn(1L);

		// then
		assertThatIllegalArgumentException().isThrownBy(
			() -> orderService.create(order)
		);
	}

	@DisplayName("주문[예외]: 주문 등록 테스트(2. 주문하려는 메뉴가 등록되어 있어야 한다.)")
	@Test
	void errorEmptyMenuTest() {
		// given
		Order order = Order.of(1L, orderTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);

		// when
		when(menuDao.countByIdIn(any())).thenReturn((long)orderLineItems.size());
		when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.empty());

		// then
		assertThatIllegalArgumentException().isThrownBy(
			() -> orderService.create(order)
		);
	}

	@DisplayName("주문[예외]: 주문 등록 테스트()")
	@Test
	void errorOrderTableEmptyTest() {
		// given
		OrderTable emptyTable = OrderTable.of(2L, null, 0, true);
		Order order = Order.of(1L, emptyTable.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);

		// when
		when(menuDao.countByIdIn(any())).thenReturn((long)orderLineItems.size());
		when(orderTableDao.findById(emptyTable.getId())).thenReturn(Optional.of(emptyTable));

		// then
		assertThatIllegalArgumentException().isThrownBy(
			() -> orderService.create(order)
		);
	}

	@DisplayName("주문: 주문 목록을 조회 테스트")
	@Test
	void selectOrdersTest() {
		// given
		OrderLineItem orderLineItem = mock(OrderLineItem.class);
		Order order1 = Order.of(1L, 1L, null, null, Arrays.asList(orderLineItem, orderLineItem));
		Order order2 = Order.of(2L, 2L, null, null, Arrays.asList(orderLineItem, orderLineItem, orderLineItem));
		given(orderDao.findAll()).willReturn(Arrays.asList(order1, order2));
		given(orderLineItemDao.findAllByOrderId(anyLong())).willReturn(
			Arrays.asList(orderLineItem, orderLineItem, orderLineItem, orderLineItem, orderLineItem)
		);

		// when
		List<Order> results = orderService.list();

		// then
		assertAll(
			() -> assertThat(results.size()).isEqualTo(2),
			() -> assertThat(results.get(0).getOrderLineItems().size()).isEqualTo(5)
		);
	}

	@DisplayName("주문: 주문 상태를 변경 테스트")
	@Test
	void changeOrderStatusTest() {
		// given
		Order oldOrder = Order.of(1L, orderTable.getId(), OrderStatus.COOKING.name(), null, orderLineItems);
		Order newOrder = Order.of(1L, orderTable.getId(), OrderStatus.COMPLETION.name(), null, orderLineItems);
		given(orderDao.findById(any())).willReturn(Optional.of(oldOrder));
		given(orderDao.save(oldOrder)).willReturn(newOrder);
		given(orderLineItemDao.findAllByOrderId(anyLong())).willReturn(orderLineItems);

		// when
		Order actual = orderService.changeOrderStatus(oldOrder.getId(), newOrder);

		// then
		assertAll(
			() -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION.name()),
			() -> assertThat(actual.getOrderLineItems().size()).isEqualTo(orderLineItems.size())
		);
	}

	@DisplayName("주문[예외]: 주문 상태를 변경 테스트(1. 주문이 등록되어 있어야 한다.)")
	@Test
	void errorNotExistOrderTest() {
		// given
		Order changeStatusOrder = mock(Order.class);

		// when
		when(orderDao.findById(any())).thenReturn(Optional.empty());

		// then
		assertThatIllegalArgumentException().isThrownBy(
			() -> orderService.changeOrderStatus(5L, changeStatusOrder)
		);
	}

	@DisplayName("주문[예외]: 주문 상태를 변경 테스트(2. 주문 상태가 계산 완료인 경우 변경이 불가능하다.)")
	@Test
	void errorChangeCompleteStatusTest() {
		// given
		Order changeStatusOrder = Order.of(1L, orderTable.getId(), OrderStatus.MEAL.name(), null, orderLineItems);
		Order completeStatusOrder = Order.of(1L, orderTable.getId(), OrderStatus.COMPLETION.name(), null,
			orderLineItems);

		// when
		when(orderDao.findById(any())).thenReturn(Optional.of(completeStatusOrder));

		// then
		assertThatIllegalArgumentException().isThrownBy(
			() -> orderService.changeOrderStatus(1L, changeStatusOrder)
		);
	}
}

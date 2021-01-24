package kitchenpos.application;

import static kitchenpos.TestFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
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

@DisplayName("주문 BO 테스트")
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

	private OrderTable 새_주문_테이블;

	@BeforeEach
	void setUp() {
		새_주문_테이블 = new OrderTable.Builder().id(9L).empty(false).build();
	}

	@DisplayName("주문 생성")
	@Test
	void create_happyPath() {
		// given
		OrderLineItem 주문_항목1 = new OrderLineItem.Builder().menu(메뉴1).quantity(1L).build();
		OrderLineItem 주문_항목2 = new OrderLineItem.Builder().menu(메뉴2).quantity(2L).build();
		Order 주문 = new Order.Builder().orderTable(새_주문_테이블).orderLineItems(주문_항목1, 주문_항목2).build();

		given(menuDao.countByIdIn(Arrays.asList(메뉴1.getId(), 메뉴2.getId()))).willReturn(2L);
		given(orderTableDao.findById(새_주문_테이블.getId())).willReturn(Optional.of(새_주문_테이블));
		given(orderDao.save(주문)).willAnswer(invocation -> {
			주문.setId(1L);
			return 주문;
		});

		// when
		Order saveOrder = orderService.create(주문);

		// then
		assertThat(saveOrder.getId()).isEqualTo(1L);
	}

	@DisplayName("주문 생성 : 주문 항목의 메뉴는 중복")
	@Test
	void create_exceptionCase1() {
		// given
		OrderLineItem 주문_항목1 = new OrderLineItem.Builder().menu(메뉴1).quantity(1L).build();
		OrderLineItem 주문_항목2 = new OrderLineItem.Builder().menu(메뉴1).quantity(2L).build();
		Order 주문 = new Order.Builder().orderTable(새_주문_테이블).orderLineItems(주문_항목1, 주문_항목2).build();

		given(menuDao.countByIdIn(Arrays.asList(메뉴1.getId(), 메뉴1.getId()))).willReturn(1L);

		// when & then
		assertThatThrownBy(() -> orderService.create(주문)).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 생성 : 주문 항목이 비어있음")
	@Test
	void create_exceptionCase2() {
		// given
		Order 주문 = new Order.Builder().orderTable(새_주문_테이블).build();

		// when & then
		assertThatThrownBy(() -> orderService.create(주문)).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 생성 : 주문 테이블 번호가 비어있음")
	@Test
	void create_exceptionCase3() {
		// given
		OrderLineItem 주문_항목1 = new OrderLineItem.Builder().menu(메뉴1).quantity(1L).build();
		Order 주문 = new Order.Builder().orderLineItems(주문_항목1).build();

		given(menuDao.countByIdIn(Arrays.asList(메뉴1.getId()))).willReturn(1L);

		// when & then
		assertThatThrownBy(() -> orderService.create(주문)).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 목록 조회")
	@Test
	void list() {
		// given
		Order 주문 = new Order.Builder().orderTable(주문_테이블1).build();
		given(orderDao.findAll()).willReturn(Arrays.asList(주문));

		OrderLineItem 주문_항목1 = new OrderLineItem.Builder().menu(메뉴1).quantity(1L).build();
		OrderLineItem 주문_항목2 = new OrderLineItem.Builder().menu(메뉴2).quantity(2L).build();
		given(orderLineItemDao.findAllByOrderId(주문.getId())).willReturn(Arrays.asList(주문_항목1, 주문_항목2));

		// when
		List<Order> saveOrderList = orderService.list();

		// then
		assertThat(saveOrderList).hasSize(1);
		assertThat(saveOrderList.get(0)).isEqualTo(주문);
		assertThat(saveOrderList.get(0).getOrderTable()).isEqualTo(주문_테이블1);
		assertThat(saveOrderList.get(0).getOrderLineItems()).contains(주문_항목1, 주문_항목2);
	}

	@DisplayName("주문 변경 : 성공케이스")
	@Test
	void changeOrderStatus_happyPath() {
		// given
		OrderLineItem 주문_항목1 = new OrderLineItem();
		OrderLineItem 주문_항목2 = new OrderLineItem();
		Order 주문 = new Order.Builder().orderTable(주문_테이블1).orderStatus(OrderStatus.COOKING).orderLineItems(주문_항목1, 주문_항목2).build();

		given(orderDao.findById(주문.getId())).willReturn(Optional.of(주문));
		given(orderLineItemDao.findAllByOrderId(주문.getId())).willReturn(Arrays.asList(주문_항목1, 주문_항목2));

		// when
		Order 임시 = new Order.Builder().orderStatus(OrderStatus.MEAL).build();
		Order saveOrder = orderService.changeOrderStatus(주문.getId(), 임시);

		// then
		assertThat(saveOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
	}

	@DisplayName("주문 변경: 이미 계산 완료 상태인 주문을 변경 시도함")
	@Test
	void changeOrderStatus_exceptionCase() {
		// given
		Order 주문 = new Order.Builder().orderTable(주문_테이블1).orderStatus(OrderStatus.COMPLETION).build();
		given(orderDao.findById(주문.getId())).willReturn(Optional.of(주문));

		// when & then
		Order 임시 = new Order.Builder().orderStatus(OrderStatus.MEAL).build();
		assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), 임시))
			.isInstanceOf(IllegalArgumentException.class);
	}
}

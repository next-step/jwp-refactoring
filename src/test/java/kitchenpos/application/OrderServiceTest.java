package kitchenpos.application;

import static kitchenpos.TestFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.application.MenuService;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderLineItemResponse;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;

@DisplayName("주문 BO 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	@Mock
	private MenuService menuService;
	@Mock
	private OrderDao orderDao;
	@Mock
	private TableService tableService;

	@InjectMocks
	private OrderService orderService;

	@DisplayName("주문 생성")
	@Test
	void create_happyPath() {
		// given
		OrderTable 주문_테이블9 = new OrderTable.Builder().id(-1L).empty(false).build();
		OrderLineItemRequest 주문_항목1_요청 = new OrderLineItemRequest(null, 메뉴1.getId(), 1L);
		OrderLineItemRequest 주문_항목2_요청 = new OrderLineItemRequest(null, 메뉴2.getId(), 2L);
		OrderRequest 주문_요청 = new OrderRequest(주문_테이블9.getId(), Arrays.asList(주문_항목1_요청, 주문_항목2_요청));

		given(menuService.findById(메뉴1.getId())).willReturn(메뉴1);
		given(menuService.findById(메뉴2.getId())).willReturn(메뉴2);
		given(orderDao.save(any(Order.class))).willAnswer(invocation -> {
			Order mock = spy(invocation.getArgument(0, Order.class));
			when(mock.getId()).thenReturn(1L);
			return mock;
		});

		// when
		OrderResponse response = orderService.create(주문_요청);

		// then
		assertThat(response.getId()).isEqualTo(1L);
	}

	@DisplayName("주문 생성 : 주문 항목의 메뉴는 중복")
	@Test
	void create_exceptionCase1() {
		// given
		OrderLineItemRequest 주문_항목1_요청 = new OrderLineItemRequest(null, 메뉴1.getId(), 1L);
		OrderLineItemRequest 주문_항목2_요청 = new OrderLineItemRequest(null, 메뉴1.getId(), 2L);
		OrderRequest 주문_요청 = new OrderRequest(주문_테이블1.getId(), Arrays.asList(주문_항목1_요청, 주문_항목2_요청));

		// when & then
		assertThatThrownBy(() -> orderService.create(주문_요청)).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 생성 : 주문 항목이 비어있음")
	@Test
	void create_exceptionCase2() {
		// given
		OrderRequest 주문_요청 = new OrderRequest(주문_테이블1.getId(), null);

		// when & then
		assertThatThrownBy(() -> orderService.create(주문_요청)).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 생성 : 주문 테이블 번호가 비어있음")
	@Test
	void create_exceptionCase3() {
		// given
		OrderLineItemRequest 주문_항목1_요청 = new OrderLineItemRequest(null, 메뉴1.getId(), 1L);
		OrderRequest 주문_요청 = new OrderRequest(null, Arrays.asList(주문_항목1_요청));

		// when & then
		assertThatThrownBy(() -> orderService.create(주문_요청)).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("주문 목록 조회")
	@Test
	void list() {
		// given
		OrderLineItem 주문_항목1 = new OrderLineItem.Builder().menu(메뉴1).quantity(1L).build();
		OrderLineItem 주문_항목2 = new OrderLineItem.Builder().menu(메뉴2).quantity(2L).build();
		Order 주문 = new Order.Builder().id(-1L)
			.orderLineItems(주문_항목1, 주문_항목2)
			.orderTableId(주문_테이블1.getId()).build();
		given(orderDao.findAll()).willReturn(Arrays.asList(주문));

		// when
		List<OrderResponse> listResponse = orderService.list();

		// then
		assertThat(listResponse).hasSize(1);
		assertThat(listResponse.get(0).getId()).isEqualTo(주문.getId());
		assertThat(listResponse.get(0).getOrderTableId()).isEqualTo(주문_테이블1.getId());
		assertThat(listResponse.get(0).getOrderLineItems())
			.map(OrderLineItemResponse::getMenuId)
			.contains(메뉴1.getId(), 메뉴2.getId());
	}

	@DisplayName("주문 변경 : 성공케이스")
	@Test
	void changeOrderStatus_happyPath() {
		// given
		OrderLineItem 주문_항목1 = new OrderLineItem.Builder().menu(메뉴1).quantity(1L).build();
		OrderLineItem 주문_항목2 = new OrderLineItem.Builder().menu(메뉴2).quantity(2L).build();
		Order 주문 = new Order.Builder().orderTableId(주문_테이블1.getId())
			.orderStatus(OrderStatus.COOKING)
			.orderLineItems(주문_항목1, 주문_항목2)
			.build();

		given(orderDao.findById(주문.getId())).willReturn(Optional.of(주문));

		// when
		OrderResponse response = orderService.changeOrderStatus(주문.getId(), new OrderRequest(OrderStatus.MEAL));

		// then
		assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
	}

	@DisplayName("주문 변경: 이미 계산 완료 상태인 주문을 변경 시도함")
	@Test
	void changeOrderStatus_exceptionCase() {
		// given
		OrderLineItem 주문_항목1 = new OrderLineItem.Builder().menu(메뉴1).quantity(1L).build();
		OrderLineItem 주문_항목2 = new OrderLineItem.Builder().menu(메뉴2).quantity(2L).build();
		Order 주문 = new Order.Builder()
			.orderTableId(주문_테이블1.getId())
			.orderLineItems(주문_항목1, 주문_항목2)
			.orderStatus(OrderStatus.COMPLETION)
			.build();
		given(orderDao.findById(주문.getId())).willReturn(Optional.of(주문));

		// when & then
		assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), new OrderRequest(OrderStatus.MEAL)))
			.isInstanceOf(IllegalArgumentException.class);
	}
}

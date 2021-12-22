package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.order.exception.OrderException;
import kitchenpos.tablegroup.exception.TableException;

@DisplayName("주문 : 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	@Mock
	OrderRepository orderRepository;

	@Mock
	OrderTableRepository orderTableRepository;

	@Mock
	MenuRepository menuRepository;

	@Mock
	Order order;

	@Mock
	Menu menu;

	@Mock
	OrderTable orderTable;

	@Mock
	OrderLineItemRequest orderLineItemRequest;

	@Mock
	List<OrderLineItemRequest> orderLineItemRequests;

	private OrderRequest orderRequest;

	private OrderStatusRequest orderStatusRequest;

	@InjectMocks
	private OrderService orderService;

	@DisplayName("주문 생성시 주문 항목이 비어있는 경우 예외처리 테스트")
	@Test
	void createOrderEmptyOrderLineItems() {
		// given
		given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(orderTable));
		orderRequest = OrderRequest.of(orderTable.getId(), Collections.emptyList());

		// when // then
		assertThatThrownBy(() -> {
			orderService.create(orderRequest);
		}).isInstanceOf(OrderException.class);
	}

	@DisplayName("주문 생성시 주문 테이블이 존재하지 않은 경우 예외처리 테스트")
	@Test
	void createOrderUnknownOrderTable() {
		// given
		orderRequest = OrderRequest.of(orderTable.getId(), Collections.singletonList(orderLineItemRequest));

		// when
		when(orderTableRepository.findById(anyLong())).thenThrow(TableException.class);

		// then
		assertThatThrownBy(() -> {
			orderService.create(orderRequest);
		}).isInstanceOf(TableException.class);
	}

	@DisplayName("주문 생성 테스트")
	@Test
	void createOrder() {
		// given
		given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(orderTable));
		given(menuRepository.findById(anyLong())).willReturn(Optional.of(menu));
		given(orderLineItemRequest.getMenuId()).willReturn(1L);
		given(orderLineItemRequest.getQuantity()).willReturn(2L);
		orderRequest = OrderRequest.of(orderTable.getId(), Collections.singletonList(orderLineItemRequest));

		// when
		when(orderRepository.save(any(Order.class))).thenReturn(order);

		// then
		assertThat(orderService.create(orderRequest)).isEqualTo(order);
	}

	@DisplayName("주문 목록 조회 테스트")
	@Test
	void getList() {
		// given
		orderRequest = OrderRequest.of(orderTable.getId(), Collections.singletonList(orderLineItemRequest));

		// when
		when(orderRepository.findAll()).thenReturn(Collections.singletonList(order));

		// then
		assertThat(orderService.list()).containsExactly(order);
	}

	@DisplayName("주문 상태 변경시 존재하지 않는 주문인 경우 예외처리 테스트")
	@Test
	void changeOrderStatusUnknownOrder() {
		// when
		when(orderRepository.findById(anyLong())).thenThrow(OrderException.class);
		orderStatusRequest = OrderStatusRequest.from(OrderStatus.MEAL);

		// then
		assertThatThrownBy(() -> {
			orderService.changeOrderStatus(anyLong(), orderStatusRequest);
		}).isInstanceOf(OrderException.class);
	}

	@DisplayName("주문 상태 변경시 이미 완료 상태인 경우 예외처리 테스트")
	@Test
	void changeOrderCompletionStatus() {
		// given
		given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));
		orderStatusRequest = OrderStatusRequest.from(OrderStatus.MEAL);

		// when
		doThrow(OrderException.class)
			.when(order)
			.updateOrderStatus(orderStatusRequest.getOrderStatus());

		// then
		assertThatThrownBy(() -> {
			orderService.changeOrderStatus(anyLong(), orderStatusRequest);
		}).isInstanceOf(OrderException.class);
	}

	@DisplayName("주문 상태 변경 테스트")
	@Test
	void changeOrderStatus() {
		// given
		given(orderRepository.findById(anyLong())).willReturn(Optional.of(order));
		orderStatusRequest = OrderStatusRequest.from(OrderStatus.MEAL);

		// when
		when(orderRepository.save(order)).thenReturn(order);

		// then
		assertThat(orderService.changeOrderStatus(anyLong(), orderStatusRequest)).isEqualTo(order);
	}
}

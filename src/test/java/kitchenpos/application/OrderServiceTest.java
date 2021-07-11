package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Price;
import kitchenpos.dto.OrderLineItemRequest;
import kitchenpos.dto.OrderLineItemResponse;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
	@Mock
	private MenuRepository menuRepository;
	@Mock
	private OrderRepository orderRepository;
	@Mock
	private OrderTableRepository orderTableRepository;
	@InjectMocks
	private OrderService orderService;

	private OrderLineItem 피자파스타세트2개;
	private Order 주문;
	private OrderTable 주문테이블;
	private List<OrderLineItem> 주문항목;

	private MenuGroup 양식;
	private Menu 피자파스타세트;

	@BeforeEach
	void setUp() {
		//메뉴 그룹
		양식 = new MenuGroup("양식");
		피자파스타세트 = new Menu("피자파스타세트", new Price(BigDecimal.valueOf(30000)), 양식);

		피자파스타세트2개 = new OrderLineItem(null, 피자파스타세트, 2);
		주문항목 = new ArrayList<>();
		주문항목.add(피자파스타세트2개);
		주문테이블 = new OrderTable(null, null, false);
		주문 = new Order(주문테이블, OrderStatus.COOKING, LocalDateTime.of(2021, 7, 4, 0, 0), 주문항목);

	}

	@DisplayName("빈 테이블에서 주문이 생성된경우 오류 발생")
	@Test
	void testOrderTableEmpty() {

		List<OrderLineItemRequest> orderLIneItems = new ArrayList<>();
		OrderRequest orderRequest = new OrderRequest(1L, null, orderLIneItems);

		OrderTable orderTable = new OrderTable(null, null, true);
		when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));

		assertThatThrownBy(() -> {
			orderService.create(orderRequest);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("비어있는 테이블은 주문할 수 없습니다.");
	}

	@DisplayName("주문하려는 테이블을 찾을 수 없는 경우 오류 발생")
	@Test
	void testNotFoundOrderTable() {
		List<OrderLineItemRequest> orderLIneItems = new ArrayList<>();
		OrderRequest orderRequest = new OrderRequest(1L, null, orderLIneItems);
		when(orderTableRepository.findById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> {
			orderService.create(orderRequest);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("id에 해당하는 주문 테이블을 찾을 수 없습니다.");
	}

	@DisplayName("주문 항목이 비어있는경우 오류 발생")
	@Test
	void testNotContainsOrderLineItemInMenu() {
		List<OrderLineItemRequest> orderLIneItems = new ArrayList<>();
		OrderRequest orderRequest = new OrderRequest(1L, null, orderLIneItems);

		OrderTable orderTable = new OrderTable(null, null, false);
		when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));

		assertThatThrownBy(() -> {
			orderService.create(orderRequest);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("주문 항목을 구성해야 주문이 가능합니다.");
	}

	@DisplayName("주문항목을 메뉴에서 찾을 수 없는경우 오류 발생")
	@Test
	void testEmptyOrderLineItem() {
		List<OrderLineItemRequest> orderLIneItems = new ArrayList<>();
		orderLIneItems.add(new OrderLineItemRequest(2L, 3L));
		OrderRequest orderRequest = new OrderRequest(1L, null, orderLIneItems);

		when(menuRepository.findById(2L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> {
			orderService.create(orderRequest);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("id에 해당하는 메뉴가 없습니다");
	}

	@DisplayName("주문 생성 - happy path")
	@Test
	void testCreateOrder() {
		List<OrderLineItemRequest> orderLIneItems = new ArrayList<>();
		orderLIneItems.add(new OrderLineItemRequest(2L, 2L));
		OrderRequest orderRequest = new OrderRequest(1L, null, orderLIneItems);

		when(orderTableRepository.findById(1L)).thenReturn(Optional.of(주문테이블));
		when(menuRepository.findById(2L)).thenReturn(Optional.of(피자파스타세트));
		when(orderRepository.save(any())).thenReturn(주문);
		OrderResponse actual = orderService.create(orderRequest);
		//then
		assertThat(actual.getOrderStatus()).isEqualTo(주문.getOrderStatus());
		assertThat(actual.getOrderLineItems()).containsExactly(OrderLineItemResponse.of(피자파스타세트2개));
	}

	@DisplayName("주문 상태 변경 오류 - 주문이 이미 완료상태인 경우")
	@Test
	void testAlreadyOrderStatusCompletion() {
		Long orderId = 1L;
		OrderRequest orderRequest = new OrderRequest(null, OrderStatus.MEAL, null);
		주문 = new Order(주문테이블, OrderStatus.COMPLETION, LocalDateTime.of(2021, 7, 4, 0, 0), 주문항목);
		when(orderRepository.findById(orderId)).thenReturn(Optional.of(주문));

		assertThatThrownBy(() -> {
			orderService.changeOrderStatus(orderId, orderRequest);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("이미 완료된 주문입니다.");

	}

	@DisplayName("주문 상태 변경 테스트")
	@Test
	void testChangeOrderStatus() {
		Long orderId = 1L;
		OrderRequest orderRequest = new OrderRequest(null, OrderStatus.MEAL, null);
		when(orderRepository.findById(orderId)).thenReturn(Optional.of(주문));

		OrderResponse actual = orderService.changeOrderStatus(orderId, orderRequest);
		assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
	}

	@DisplayName("주문 상태 변경 오류 - 변경하려는 주문을 찾을 수 없음")
	@Test
	void testOrderNotFound() {
		Long orderId = 1L;
		OrderRequest orderRequest = new OrderRequest(null, OrderStatus.MEAL, null);
		when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> {
			orderService.changeOrderStatus(orderId, orderRequest);
		}).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("orderId에 해당하는 주문정보를 찾을 수 없습니다.");
	}
}
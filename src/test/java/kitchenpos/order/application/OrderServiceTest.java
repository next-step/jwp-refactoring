package kitchenpos.order.application;

import static kitchenpos.common.DomainFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
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
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.exception.NotFoundMenuException;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderAddRequest;
import kitchenpos.order.dto.OrderLineItemAddRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.exception.NotFoundOrderTableException;
import kitchenpos.product.domain.Product;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	@InjectMocks
	private OrderService orderService;

	@Mock
	private MenuRepository menuRepository;
	@Mock
	private OrderRepository orderRepository;
	@Mock
	private OrderTableRepository orderTableRepository;

	private final Product 초밥 = product(1L, "초밥", 3_000);
	private final MenuProduct 메뉴초밥 = menuProduct(1L, null, 초밥, 10);
	private final MenuGroup 메인메뉴그룹 = menuGroup(1L, "메인");
	private final Menu 일식메뉴 = menu(1L, "일식", 30_000, 메인메뉴그룹, Arrays.asList(메뉴초밥));
	final OrderTable 개별_주문테이블 = orderTable(1L, null, 4, false);

	@DisplayName("주문생성")
	@Test
	void create() {
		final OrderLineItem 주문항목 = orderLineItem(1L, null, 일식메뉴, 2);
		final List<OrderLineItem> 주문항목목록 = Arrays.asList(주문항목);
		final Order 주문 = order(1L, 개별_주문테이블, OrderStatus.COOKING, 주문항목목록);

		given(orderTableRepository.findById(any())).willReturn(Optional.of(개별_주문테이블));
		given(menuRepository.findAllById(any())).willReturn(Arrays.asList(일식메뉴));
		given(orderRepository.save(any())).willReturn(주문);

		final OrderResponse createdOrder = orderService.create(
			OrderAddRequest.of(개별_주문테이블.getId(),
				Arrays.asList(OrderLineItemAddRequest.of(일식메뉴.getId(), 2L)))
		);

		assertThat(createdOrder.getId()).isNotNull();
		assertThat(createdOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
		assertThat(createdOrder.getOrderLineItems().size()).isEqualTo(1);
		assertThat(createdOrder.getOrderLineItems().get(0).getId()).isNotNull();
	}

	@DisplayName("주문생성: 주문테이블이 없으면 예외발생")
	@Test
	void create_not_found_order_table() {
		final List<OrderLineItemAddRequest> 생성할_주문항목_목록 =
			Arrays.asList(OrderLineItemAddRequest.of(일식메뉴.getId(), 2L));
		given(orderTableRepository.findById(any())).willReturn(Optional.empty());

		assertThatExceptionOfType(NotFoundOrderTableException.class)
			.isThrownBy(() -> orderService.create(
				OrderAddRequest.of(null, 생성할_주문항목_목록)
			));
	}

	@DisplayName("주문생성: 주문항목의 메뉴가 없으면 예외발생")
	@Test
	void create_not_found_menu() {
		final List<OrderLineItemAddRequest> 생성할_주문항목_목록 =
			Arrays.asList(OrderLineItemAddRequest.of(일식메뉴.getId(), 2L));
		given(menuRepository.findAllById(any())).willReturn(Collections.emptyList());

		assertThatExceptionOfType(NotFoundMenuException.class)
			.isThrownBy(() -> orderService.create(
				OrderAddRequest.of(개별_주문테이블.getId(), 생성할_주문항목_목록)
			));
	}

	@DisplayName("주문 목록 조회")
	@Test
	void list() {
		final OrderLineItem 주문항목 = orderLineItem(1L, null, 일식메뉴, 2);
		final List<OrderLineItem> 주문항목목록 = Arrays.asList(주문항목);
		final Order 주문1 = order(1L, 개별_주문테이블, OrderStatus.COOKING, 주문항목목록);
		given(orderRepository.findAll()).willReturn(Arrays.asList(주문1));

		final List<OrderResponse> orders = orderService.list();

		assertThat(orders.size()).isEqualTo(1);
	}

	@DisplayName("주문 상태 변경: 조리->식사")
	@Test
	void changeOrderStatus() {
		final OrderLineItem 주문항목 = orderLineItem(1L, null, 일식메뉴, 2);
		final List<OrderLineItem> 주문항목목록 = Arrays.asList(주문항목);
		final Order 주문 = order(1L, 개별_주문테이블, OrderStatus.COOKING, 주문항목목록);

		given(orderRepository.findById(any())).willReturn(Optional.of(주문));

		final OrderResponse changedOrder = orderService.changeOrderStatus(
			주문.getId(), OrderStatusRequest.of(OrderStatus.MEAL));

		assertThat(changedOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
	}
}

package kitchenpos.order.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
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

import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderUpdateRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

	@InjectMocks
	private OrderService orderService;
	@Mock
	private MenuService menuService;
	@Mock
	private OrderRepository orderRepository;
	@Mock
	private OrderTableRepository orderTableRepository;
	@Mock
	private OrderValidator orderValidator;

	private Menu 더블후라이드;
	private OrderTable 테이블;
	private OrderTable 빈_테이블;
	private Order 생성된_주문;
	private Order 계산된_주문;
	private OrderLineItem 생성된_주문_항목;

	@BeforeEach
	void setup() {
		Product 후라이드 = Product.of(1L, "후라이드", BigDecimal.valueOf(17_000));
		MenuGroup 추천메뉴 = MenuGroup.of(1L, "추천메뉴");
		MenuProduct 메뉴_상품 = MenuProduct.of(1L, null, 후라이드.getId(), 2L);

		더블후라이드 = Menu.of(1L, "더블 후라이드", BigDecimal.valueOf(30_000), 추천메뉴);
		더블후라이드.addMenuProducts(Collections.singletonList(메뉴_상품));

		테이블 = OrderTable.of(1L, 2, false);
		빈_테이블 = OrderTable.of(2L, 0, true);

		OrderLineItem 생성된_주문_항목 = OrderLineItem.of(1L, 더블후라이드.getId(), 1L);
		생성된_주문 = Order.of(1L, 테이블.getId(), OrderStatus.COOKING);
		계산된_주문 = Order.of(2L, 테이블.getId(), OrderStatus.COMPLETION);

		생성된_주문.addOrderLineItems(Collections.singletonList(생성된_주문_항목));
		계산된_주문.addOrderLineItems(Collections.singletonList(생성된_주문_항목));
	}

	@DisplayName("주문을 생성한다")
	@Test
	void createTest() {
		// given
		given(orderRepository.save(any())).willReturn(생성된_주문);

		OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 1L);
		OrderRequest request = new OrderRequest(1L, Collections.singletonList(orderLineItemRequest));

		// when
		OrderResponse order = orderService.create(request);

		// then
		assertThat(order.getId()).isEqualTo(생성된_주문.getId());
	}

	@DisplayName("주문 시, 주문 항목이 1개 이상이어야 한다")
	@Test
	void createTest2() {
		// given
		OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, 0L);
		OrderRequest request = new OrderRequest(1L, Collections.singletonList(orderLineItemRequest));

		// when, then
		assertThatThrownBy(() -> orderService.create(request))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.WRONG_INPUT.getMessage());
	}

	@DisplayName("주문 목록을 조회한다")
	@Test
	void listTest() {
		// given
		given(orderRepository.findAll()).willReturn(Collections.singletonList(생성된_주문));

		// when
		List<OrderResponse> results = orderService.list();

		// then
		assertThat(results.size()).isEqualTo(1);
	}

	@DisplayName("주문 상태를 변경한다")
	@Test
	void changeOrderStatusTest() {
		// given
		OrderUpdateRequest request = new OrderUpdateRequest(OrderStatus.MEAL);

		given(orderRepository.findById(any())).willReturn(Optional.of(생성된_주문));

		// when
		OrderResponse result = orderService.changeOrderStatus(생성된_주문.getId(), request);

		// then
		assertThat(result.getOrderStatus().name()).isEqualTo(OrderStatus.MEAL.name());
	}

	@DisplayName("주문 상태를 변경 시, 주문 상태가 완료가 아니어야 한다")
	@Test
	void changeOrderStatusTest2() {
		// given
		Long id = 계산된_주문.getId();
		OrderUpdateRequest request = new OrderUpdateRequest(OrderStatus.MEAL);
		given(orderRepository.findById(any())).willReturn(Optional.of(계산된_주문));

		// when
		assertThatThrownBy(() -> orderService.changeOrderStatus(id, request))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.WRONG_INPUT.getMessage());
	}

}

package kitchenpos.application;

import static kitchenpos.common.TestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.common.BaseTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.dto.OrderRequest;
import kitchenpos.dto.OrderResponse;
import kitchenpos.exception.AlreadyOrderCompleteException;
import kitchenpos.exception.EmptyTableException;
import kitchenpos.exception.NotFoundException;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;

@DisplayName("OrderService 테스트")
class OrderServiceTest extends BaseTest {

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderLineItemRepository orderLineItemRepository;

	@DisplayName("주문 등록할 수 있다.")
	@Test
	void create() {
		OrderResponse order = orderService.create(OrderRequest.of(주문대상_테이블ID, Arrays.asList(주문_메뉴1, 주문_메뉴2)));

		Order savedOrder = orderRepository.findById(order.getId()).orElse(null);
		List<OrderLineItem> savedOrderItems = orderLineItemRepository.findAllByOrderId(savedOrder.getId());

		assertAll(
			() -> assertThat(savedOrder.getId()).isNotNull(),
			() -> assertThat(savedOrder.getOrderTable().getId()).isEqualTo(주문대상_테이블ID),
			() -> assertThat(savedOrderItems).hasSize(2)
		);
	}

	@DisplayName("요청된 메뉴가 실제 저장되어 있는 메뉴가 아닌 경우가 포함되어 있으면 주문할 수 없다.")
	@Test
	void createThrow2() {
		assertThatExceptionOfType(NotFoundException.class)
			.isThrownBy(() -> {
				orderService.create(OrderRequest.of(주문대상_테이블ID, Arrays.asList(주문_존재하지않은메뉴, 주문_메뉴2)));
			});

	}

	@DisplayName("존재하지 않는 테이블에는 주문할 수 없다.")
	@Test
	void createThrow3() {
		assertThatExceptionOfType(NotFoundException.class)
			.isThrownBy(() -> {
				orderService.create(OrderRequest.of(존재하지않는_테이블ID, Arrays.asList(주문_메뉴1, 주문_메뉴2)));
			});

	}

	@DisplayName("테이블이 빈테이블 상태인 경우 주문할 수 없다.")
	@Test
	void createThrow4() {
		assertThatExceptionOfType(EmptyTableException.class)
			.isThrownBy(() -> {
				orderService.create(OrderRequest.of(빈테이블ID, Arrays.asList(주문_메뉴1, 주문_메뉴2)));
			});

	}

	@DisplayName("주문을 조회할 수 있다.")
	@Test
	void list() {
		List<OrderResponse> products = orderService.list();

		assertThat(products).hasSize(5);
	}

	@DisplayName("조리상태 주문은 상태변경할 수 있다.")
	@ParameterizedTest
	@MethodSource("paramChangeOrderStatusWhenCooking")
	void changeOrderStatusWhenCooking(OrderStatus orderStatus) {
		OrderRequest orderRequest = OrderRequest.of(조리상태_주문ID, orderStatus);
		orderService.changeOrderStatus(조리상태_주문ID, orderRequest);

		Order order = orderRepository.findById(조리상태_주문ID).orElse(null);

		assertThat(order.getOrderStatus()).isEqualTo(orderStatus);

	}

	public static Stream<Arguments> paramChangeOrderStatusWhenCooking() {
		return Stream.of(
			Arguments.of(OrderStatus.MEAL),
			Arguments.of(OrderStatus.COMPLETION)
		);
	}

	@DisplayName("식사상태 주문은 상태변경할 수 있다.")
	@ParameterizedTest
	@MethodSource("paramChangeOrderStatusWhenMeal")
	void changeOrderStatusWhenMeal(OrderStatus orderStatus) {
		OrderRequest orderRequest = OrderRequest.of(식사상태_주문ID, orderStatus);
		orderService.changeOrderStatus(식사상태_주문ID, orderRequest);

		Order order = orderRepository.findById(식사상태_주문ID).orElse(null);

		assertThat(order.getOrderStatus()).isEqualTo(orderStatus);

	}

	public static Stream<Arguments> paramChangeOrderStatusWhenMeal() {
		return Stream.of(
			Arguments.of(OrderStatus.COOKING),
			Arguments.of(OrderStatus.COMPLETION)
		);
	}

	@DisplayName("완료상태 주문은 상태변경할 수 없다.")
	@ParameterizedTest
	@MethodSource("paramChangeOrderStatusWhenCompletion")
	void changeOrderStatusWhenCompletion(OrderStatus orderStatus) {
		assertThatExceptionOfType(AlreadyOrderCompleteException.class)
			.isThrownBy(() -> {
				OrderRequest orderRequest = OrderRequest.of(완료상태_주문ID, orderStatus);
				orderService.changeOrderStatus(완료상태_주문ID, orderRequest);
			});

	}

	public static Stream<Arguments> paramChangeOrderStatusWhenCompletion() {
		return Stream.of(
			Arguments.of(OrderStatus.COOKING),
			Arguments.of(OrderStatus.MEAL)
		);
	}

}
package kitchenpos.application;

import static kitchenpos.common.TestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.common.BaseTest;
import kitchenpos.common.TestDataUtil;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

@DisplayName("OrderService 테스트")
class OrderServiceTest extends BaseTest {

	@Autowired
	private OrderService orderService;

	@Autowired
	private OrderDao orderDao;

	@Autowired
	private OrderLineItemDao orderLineItemDao;

	@DisplayName("주문 등록할 수 있다.")
	@Test
	void create() {
		Order order = orderService.create(TestDataUtil.createOrder(주문대상_테이블ID, Arrays.asList(일반_메뉴1_ID, 일반_메뉴2_ID)));

		Order savedOrder = orderDao.findById(order.getId()).orElse(null);
		List<OrderLineItem> savedOrderItems = orderLineItemDao.findAllByOrderId(savedOrder.getId());

		assertAll(
			() -> assertThat(savedOrder.getId()).isNotNull(),
			() -> assertThat(savedOrder.getOrderTable().getId()).isEqualTo(주문대상_테이블ID),
			() -> assertThat(savedOrderItems).hasSize(2)
		);
	}

	@DisplayName("요청된 메뉴가 없으면 주문할 수 없다.")
	@Test
	void createThrow1() {
		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> {
				orderService.create(TestDataUtil.createOrder(주문대상_테이블ID, null));
			});

	}

	@DisplayName("요청된 메뉴가 실제 저장되어 있는 메뉴가 아닌 경우가 포함되어 있으면 주문할 수 없다.")
	@Test
	void createThrow2() {
		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> {
				orderService.create(TestDataUtil.createOrder(주문대상_테이블ID, Arrays.asList(존재하지않은메뉴_ID, 일반_메뉴2_ID)));
			});

	}

	@DisplayName("존재하지 않는 테이블에는 주문할 수 없다.")
	@Test
	void createThrow3() {
		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> {
				orderService.create(TestDataUtil.createOrder(존재하지_않는_테이블ID, Arrays.asList(일반_메뉴1_ID, 일반_메뉴2_ID)));
			});

	}

	@DisplayName("테이블이 빈테이블 상태인 경우 주문할 수 없다.")
	@Test
	void createThrow4() {
		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> {
				orderService.create(TestDataUtil.createOrder(빈_테이블ID, Arrays.asList(일반_메뉴1_ID, 일반_메뉴2_ID)));
			});

	}

	@DisplayName("주문을 조회할 수 있다.")
	@Test
	void list() {
		List<Order> products = orderService.list();

		assertThat(products).hasSize(5);
	}

	@DisplayName("조리상태 주문은 상태변경할 수 있다.")
	@ParameterizedTest
	@ValueSource(strings = {"MEAL", "COMPLETION"})
	void changeOrderStatusWhenCooking(String orderStatus) {
		조리상태_주문.setOrderStatus(orderStatus);
		orderService.changeOrderStatus(조리상태_주문.getId(), 조리상태_주문);

		Order order = orderDao.findById(조리상태_주문.getId()).orElse(null);

		assertThat(order.getOrderStatus()).isEqualTo(orderStatus);

	}

	@DisplayName("식사상태 주문은 상태변경할 수 있다.")
	@ParameterizedTest
	@ValueSource(strings = {"COOKING", "COMPLETION"})
	void changeOrderStatusWhenMeal(String orderStatus) {
		식사상태_주문.setOrderStatus(orderStatus);
		orderService.changeOrderStatus(식사상태_주문.getId(), 식사상태_주문);

		Order order = orderDao.findById(식사상태_주문.getId()).orElse(null);

		assertThat(order.getOrderStatus()).isEqualTo(orderStatus);

	}

	@DisplayName("완료상태 주문은 상태변경할 수 없다.")
	@ParameterizedTest
	@ValueSource(strings = {"COOKING", "MEAL"})
	void changeOrderStatusWhenCompletion(String orderStatus) {
		assertThatExceptionOfType(IllegalArgumentException.class)
			.isThrownBy(() -> {
				완료상태_주문.setOrderStatus(orderStatus);
				orderService.changeOrderStatus(완료상태_주문.getId(), 완료상태_주문);
			});

	}

}
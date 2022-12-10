package kitchenpos.acceptance;

import java.util.function.ToLongFunction;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@DisplayName("주문 관련 기능")
public class OrderAcceptanceTest extends AcceptanceTest<Order> {

	static final String REQUEST_PATH = "/api/orders";
	static final String ORDER_STATUS_REQUEST_PATH = "/api/orders/{orderId}/order-status";

	/**
	 * Feature: 주문 관리 기능
	 * Background
	 *   When 상품을 등록을 요청하면
	 *   Then 상품 등록에 성공한다
	 *   When 메뉴를 등록을 요청하면
	 *   Then 메뉴 등록에 성공한다
	 *   When 주문 테이블 등록을 요청하면
	 *   Then 주문 테이블 등록에 성공한다
	 */
	@BeforeEach
	void setup() {

	}

	/**
	 * Scenario: 주문 관리
	 * When 주문 등록을 요청하면
	 * Then 주문 등록에 성공한다
	 * Then 주문이 조리 상태로 설정된다.
	 * When 주문 상태를 '식사'로 변경을 요청할 경우
	 * Then 주문 상태 변경에 성공한다.
	 */
	@Test
	void 주문_관리() {

	}

	/**
	 * Scenario: 주문 등록 실패
	 * When 주문 항목에는 메뉴 ID와 해당 메뉴의 수량이 존재해야 한다.
	 * Then 주문 등록에 실패한다
	 * When 주문 테이블이 존재하지 않을 경우
	 * Then 주문 등록에 실패한다
	 * When 계산 완료의 주문의 주문 상태를 변경 요청할 경우
	 * Then 주문 상태를 변경에 실패한다.
	 */
	@Test
	void 주문_등록_실패() {

	}

	public static Order 주문(OrderTable orderTable, String orderStatus) {
		Order order = new Order();
		order.setOrderTableId(orderTable.getId());
		order.setOrderStatus(orderStatus);
		order.setOrderLineItems(Lists.newArrayList());
		return order;
	}

	public static Order 조리중_주문(OrderTable orderTable) {
		return 주문(orderTable, OrderStatus.COOKING.name());
	}

	@Override
	protected String getRequestPath() {
		return REQUEST_PATH;
	}

	@Override
	protected ToLongFunction<Order> idExtractor() {
		return Order::getId;
	}

	@Override
	protected Class<Order> getDomainClass() {
		return Order.class;
	}
}

package kitchenpos.acceptance;

import static kitchenpos.acceptance.MenuAcceptanceTest.메뉴가격;
import static kitchenpos.acceptance.OrderTableAcceptanceTest.주문_테이블;
import static kitchenpos.fixture.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuFixture;

@DisplayName("주문 관련 기능")
public class OrderAcceptanceTest extends AcceptanceTest<Order> {

	static final String REQUEST_PATH = "/api/orders";
	static final String ORDER_STATUS_REQUEST_PATH = "/api/orders/{orderId}/order-status";

	ProductAcceptanceTest products;
	MenuAcceptanceTest menus;
	OrderTableAcceptanceTest orderTables;
	MenuGroupAcceptanceTest menuGroups;
	private OrderTable 주문_테이블;
	private Menu 메뉴;
	private MenuGroup 메뉴그룹;

	/**
	 * Feature: 주문 관리 기능
	 * Background
	 *   Given 상품을 등록되어 있음
	 *   Given 메뉴 그룹 등록되어 있음
	 *   Given 메뉴를 등록되어 있음
	 *   Given 주문 테이블 등록되어 있음
	 */
	@BeforeEach
	void setup() {
		products = new ProductAcceptanceTest();
		menuGroups = new MenuGroupAcceptanceTest();
		menus = new MenuAcceptanceTest();
		orderTables = new OrderTableAcceptanceTest();

		List<Product> 상품_목록 = Lists.newArrayList(상품("너티 크루아상", 3000),
												 상품("단호박 크림 수프", 6000),
												 상품("사과 가득 젤리", 4000));

		상품_목록 = products.상품_등록되어_있음(상품_목록);
		메뉴그룹 = menuGroups.메뉴_그룹_등록되어_있음();
		메뉴 = menus.메뉴_등록되어_있음(MenuFixture.메뉴(상품_목록, 메뉴그룹, 메뉴가격));
		주문_테이블 = orderTables.주문_테이블_등록되어_있음(주문_테이블(10));
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
		// when
		ExtractableResponse<Response> 등록_요청_응답 = 등록_요청(조리중_주문(주문_테이블, Lists.newArrayList(메뉴)));
		// then
		Order 주문 = 등록됨(등록_요청_응답);
		// then
		주문_상턔_확인(주문, OrderStatus.COOKING);
		// when
		주문.setOrderStatus(OrderStatus.MEAL.name());
		ExtractableResponse<Response> 주문_변경_요청_응답 = 주문_상태_변경_요청(주문);
		// then
		주문_수정됨(주문_변경_요청_응답, 주문);

	}

	/**
	 * When 주문 항목이 없을 경우
	 * Then 주문 등록에 실패한다
	 * When 메뉴가 존재하지 않을 경우
	 * Then 주문 등록에 실패한다
	 */
	@Test
	void 입력_항목이_없음() {
		// when
		ExtractableResponse<Response> 등록_요청_응답 = 등록_요청(조리중_주문(주문_테이블, Lists.emptyList()));
		// then
		등록_실패함(등록_요청_응답);
		// when
		Menu 존재하지_않는_메뉴 = MenuFixture.메뉴(
			Lists.newArrayList(상품("후라이드", 10_000)),
			메뉴그룹,
			10_000);

		등록_요청_응답 = 등록_요청(조리중_주문(주문_테이블, Lists.newArrayList(존재하지_않는_메뉴)));
		// then
		등록_실패함(등록_요청_응답);
	}

	/**
	 * When 주문 테이블이 존재하지 않을 경우
	 * Then 주문 등록에 실패한다
	 */
	@Test
	void 주문_테이블이_존재하지_않음() {
		// when
		OrderTable 존재하지_않는_주문_테이블 = 주문_테이블(10);
		ExtractableResponse<Response> 등록_요청_응답 = 등록_요청(조리중_주문(존재하지_않는_주문_테이블, Lists.newArrayList(메뉴)));
		// then
		등록_실패함(등록_요청_응답);
	}

	/**
	 * Given 주문이 등록되어 있고
	 * Given 주문의 상태가 계산 완료일 떄
	 * When 계산 완료의 주문의 주문 상태를 변경 요청할 경우
	 * Then 주문 상태를 변경에 실패한다.
	 */
	@Test
	void 계산_완료_주문의_상태_변경() {
		// given
		ExtractableResponse<Response> 등록_요청_응답 = 등록_요청(조리중_주문(주문_테이블, Lists.newArrayList(메뉴)));
		Order 주문 = 등록됨(등록_요청_응답);
		// given
		주문.setOrderStatus(OrderStatus.COMPLETION.name());
		ExtractableResponse<Response> 변경_요청_응답 = 주문_상태_변경_요청(주문);
		주문_수정됨(변경_요청_응답, 주문);

		// when
		주문.setOrderStatus(OrderStatus.MEAL.name());
		변경_요청_응답 = 주문_상태_변경_요청(주문);
		수정_실패함(변경_요청_응답);
	}

	/**
	 * When 빈 주문 테이블에 주문을 등록할 경우
	 * Then 주문 등록에 실패한다
	 */
	@Test
	void 빈_주문_테이블에_주문_등록() {
		// when
		주문_테이블.setEmpty(true);
		ExtractableResponse<Response> 등록_요청_응답 = 등록_요청(조리중_주문(주문_테이블, Lists.newArrayList(메뉴)));
		// then
		등록_실패함(등록_요청_응답);
	}

	private void 주문_수정됨(ExtractableResponse<Response> 수정_응답, Order 변경된_주문) {
		수정됨(수정_응답, 변경된_주문, Order::getOrderStatus);
	}

	private ExtractableResponse<Response> 주문_상태_변경_요청(Order order) {
		return 수정_요청(ORDER_STATUS_REQUEST_PATH, order);
	}

	private void 주문_상턔_확인(Order 주문, OrderStatus orderStatus) {
		assertThat(주문.getOrderStatus()).isEqualTo(orderStatus.name());
	}

	public static Order 주문(OrderTable orderTable, List<OrderLineItem> orderLineItems, String orderStatus) {
		Order order = new Order();
		order.setOrderTableId(orderTable.getId());
		order.setOrderStatus(orderStatus);
		order.setOrderLineItems(orderLineItems);
		return order;
	}

	public static Order 조리중_주문(OrderTable orderTable, List<Menu> menus) {
		return 주문(orderTable, 주문_항목(menus), OrderStatus.COOKING.name());
	}

	private static List<OrderLineItem> 주문_항목(List<Menu> menus) {
		return menus.stream()
			.map(OrderAcceptanceTest::주문_항목)
			.collect(Collectors.toList());
	}

	private static OrderLineItem 주문_항목(Menu menu) {
		OrderLineItem orderLineItem = new OrderLineItem();
		orderLineItem.setMenuId(menu.getId());
		orderLineItem.setQuantity(1);
		return orderLineItem;
	}

	public Order 주문_등록되어_있음(OrderTable 주문_테이블) {
		setup();
		ExtractableResponse<Response> 등록_요청_응답 = 등록_요청(조리중_주문(주문_테이블, Lists.newArrayList(메뉴)));
		return 등록됨(등록_요청_응답);
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

package kitchenpos.acceptance;

import static kitchenpos.acceptance.MenuAcceptanceUtils.*;
import static kitchenpos.acceptance.MenuGroupAcceptanceUtils.*;
import static kitchenpos.acceptance.OrderAcceptanceUtils.*;
import static kitchenpos.acceptance.ProductAcceptanceUtils.*;
import static kitchenpos.acceptance.TableAcceptanceUtils.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;

@DisplayName("주문 관련 기능")
class OrderAcceptanceTest extends AcceptanceTest {

	private Menu 반반치킨_세트;
	private OrderTable 주문테이블;

	@BeforeEach
	public void setup() {
		super.setUp();
		MenuGroup 반반치킨_메뉴 = 메뉴_그룹_등록_되어_있음("반반치킨");
		Product 간장반_양념반_치킨 = 상품_등록_되어_있음("간장반 양념반 치킨", BigDecimal.TEN);
		반반치킨_세트 = 메뉴_등록_되어_있음("반반치킨", BigDecimal.TEN, 반반치킨_메뉴.getId(), 간장반_양념반_치킨.getId(), 2);
		주문테이블 = 주문_테이블_등록_되어_있음(2, false);
	}

	@DisplayName("주문을 등록할 수 있다.")
	@Test
	void createOrderTest() {
		// given
		int quantity = 2;

		// when
		ExtractableResponse<Response> 주문_등록_요청 = 주문_등록_요청(주문테이블, 반반치킨_세트, quantity);

		// then
		주문_등록_됨(주문_등록_요청, quantity, 반반치킨_세트);
	}

	@DisplayName("주문 목록을 조회할 수 있다.")
	@Test
	void listOrdersTest() {
		// given
		Order order = 주문_등록_되어_있음(주문테이블, 반반치킨_세트, 2);

		// when
		ExtractableResponse<Response> 주문_목록_조회_요청 = 주문_목록_조회_요청();

		// then
		주문_목록_조회_됨(주문_목록_조회_요청, order);
	}

	@DisplayName("주문 상태를 변경할 수 있다.")
	@Test
	void changeOrderStatusTest() {
		// given
		Order order = 주문_등록_되어_있음(주문테이블, 반반치킨_세트, 2);

		// when
		ExtractableResponse<Response> 주문_상태_변경_요청 = 주문_상태_변경_요청(order.getId(), OrderStatus.COOKING);

		// then
		주문_상태_변경_됨(주문_상태_변경_요청, OrderStatus.COOKING);
	}
}

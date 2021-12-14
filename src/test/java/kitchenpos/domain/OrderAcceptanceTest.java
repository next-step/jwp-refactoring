package kitchenpos.domain;

import static kitchenpos.domain.MenuAcceptanceStaticTest.*;
import static kitchenpos.domain.MenuGroupAcceptanceStaticTest.*;
import static kitchenpos.domain.OrderAcceptanceStaticTest.*;
import static kitchenpos.domain.ProductAcceptanceStaticTest.*;
import static kitchenpos.domain.TableAcceptanceStaticTest.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;

@DisplayName("인수테스트 : 주문 관련")
class OrderAcceptanceTest extends AcceptanceTest {

	private Menu 불닭_메뉴;
	private Menu 없는_메뉴 = new Menu();
	private OrderTable 주문_테이블;
	private OrderTable 존재하지_않는_테이블 = new OrderTable();
	private OrderTable 빈_테이블 = new OrderTable();
	private Order 존재하지_않는_주문 = new Order();

	@BeforeEach
	void setup() {
		MenuGroup 두마리_메뉴_그룹 = 메뉴_그룹_생성되어_있음(메뉴_그룹_생성_요청값_생성("두마리메뉴"));
		Product 불닭 = 상품이_생성_되어있음(상품_요청값_생성("불닭", 16000));
		List<MenuProduct> 불닭_두마리_메뉴_상품_리스트 = 메뉴_상품_생성되어_있음(불닭);
		불닭_메뉴 = 메뉴가_생성_되어있음(메뉴_생성_요청값_생성("불닭 메뉴", 19000, 두마리_메뉴_그룹.getId(), 불닭_두마리_메뉴_상품_리스트));
		없는_메뉴.setId(100L);
		주문_테이블 = 테이블이_생성_되어있음(테이블_요청값_생성(0, false));
		빈_테이블 = 테이블이_생성_되어있음(테이블_요청값_생성(0, true));
		존재하지_않는_테이블.setId(100L);
		존재하지_않는_주문.setId(100L);
	}

	@Test
	void 주문을_생성한다() {
		// given
		Order 주문_요청값 = 주문_요청값_생성(주문_테이블.getId(), 주문_메뉴_생성(불닭_메뉴, 2));

		// when
		ExtractableResponse<Response> response = 주문_요청(주문_요청값);

		// then
		주문_생성됨(response);
	}

	@Test
	void 주문할_메뉴가_비어있는_경우_주문_생성에_실패한다() {
		// given
		Order 주문_요청값 = 주문_요청값_생성(주문_테이블.getId(), Collections.EMPTY_LIST);

		// when
		ExtractableResponse<Response> response = 주문_요청(주문_요청값);

		// then
		주문_생성에_실패함(response);
	}

	@Test
	void 주문할_메뉴가_존재하지_않은_경우_주문_생성에_실패한다() {
		// given
		Order 주문_요청값 = 주문_요청값_생성(주문_테이블.getId(), 주문_메뉴_생성(없는_메뉴, 2));

		// when
		ExtractableResponse<Response> response = 주문_요청(주문_요청값);

		// then
		주문_생성에_실패함(response);
	}

	@Test
	void 주문할_테이블이_존재하지_않을_경우_주문_생성에_실패한다() {
		// given
		Order 주문_요청값 = 주문_요청값_생성(존재하지_않는_테이블.getId(), 주문_메뉴_생성(불닭_메뉴, 2));

		// when
		ExtractableResponse<Response> response = 주문_요청(주문_요청값);

		// then
		주문_생성에_실패함(response);
	}

	@Test
	void 주문할_테이블이_빈_상태인_경우_주문_생성에_실패한다() {
		// given
		Order 주문_요청값 = 주문_요청값_생성(빈_테이블.getId(), 주문_메뉴_생성(불닭_메뉴, 2));

		// when
		ExtractableResponse<Response> response = 주문_요청(주문_요청값);

		// then
		주문_생성에_실패함(response);
	}

	@Test
	void 주문_목록을_조회한다() {
		// given
		Order 생성된_주문 = 주문이_생성_되어_있음(주문_요청값_생성(주문_테이블.getId(), 주문_메뉴_생성(불닭_메뉴, 2)));

		// when
		ExtractableResponse<Response> response = 주문_목록_조회_요청();

		// then
		주문_목록이_조회됨(response, Collections.singletonList(생성된_주문.getId()));
	}

	@Test
	void 주문_상태를_변경한다() {
		// given
		Order 생성된_주문 = 주문이_생성_되어_있음(주문_요청값_생성(주문_테이블.getId(), 주문_메뉴_생성(불닭_메뉴, 2)));
		생성된_주문.setOrderStatus(OrderStatus.MEAL.name());

		// when
		ExtractableResponse<Response> response = 주문_상태_변경_요청(생성된_주문);

		// then
		주문_상태가_변경됨(response);
	}

	@Test
	void 주문이_존재하지_않은_경우_주문_상태_변경에_실패한다() {
		// given
		존재하지_않는_주문.setOrderStatus(OrderStatus.MEAL.name());

		// when
		ExtractableResponse<Response> response = 주문_상태_변경_요청(존재하지_않는_주문);

		// then
		주문_상태_변경에_실패함(response);
	}

	@Test
	void 주문_상태가_완료인_경우_주문_상태_변경에_실패한다() {
		// given
		Order 생성된_주문 = 주문이_생성_되어_있음(주문_요청값_생성(주문_테이블.getId(), 주문_메뉴_생성(불닭_메뉴, 2)));
		Order 완료된_주문 = 주문_상태가_변경_되어_있음(생성된_주문, OrderStatus.COMPLETION.name());

		// when
		ExtractableResponse<Response> response = 주문_상태_변경_요청(완료된_주문);

		// then
		주문_상태_변경에_실패함(response);
	}
}

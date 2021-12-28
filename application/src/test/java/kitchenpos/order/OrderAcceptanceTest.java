package kitchenpos.order;

import static kitchenpos.menu.MenuAcceptanceStaticTest.*;
import static kitchenpos.menugroup.MenuGroupAcceptanceStaticTest.*;
import static kitchenpos.order.OrderAcceptanceStaticTest.*;
import static kitchenpos.product.ProductAcceptanceStaticTest.*;
import static kitchenpos.table.TableAcceptanceStaticTest.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.table.dto.OrderTableResponse;

@DisplayName("주문 : 인수 테스트")
class OrderAcceptanceTest extends AcceptanceTest {

	private MenuResponse 불닭_메뉴;
	private OrderTableResponse 주문_테이블;
	private OrderTableResponse 존재하지_않는_테이블 = OrderTableResponse.of(100L, null, 10, true);

	@BeforeEach
	void setup() {
		MenuGroupResponse 두마리_메뉴_그룹 = 메뉴_그룹_생성되어_있음(메뉴_그룹_생성_요청값_생성("두마리메뉴"));
		ProductResponse 불닭 = 상품이_생성_되어있음(상품_요청값_생성("불닭", 16000));
		List<MenuProductRequest> 불닭_두마리_메뉴_상품_리스트 = 메뉴_상품_요청_생성_되어_있음(불닭);
		불닭_메뉴 = 메뉴가_생성_되어있음(메뉴_생성_요청값_생성("불닭 메뉴", 19000, 두마리_메뉴_그룹.getId(), 불닭_두마리_메뉴_상품_리스트));
		주문_테이블 = 테이블이_생성_되어있음(테이블_요청값_생성(0, false));
	}

	@Test
	void 주문을_생성한다() {
		// given
		OrderRequest 주문_요청값 = 주문_요청값_생성(주문_테이블.getId(), 주문_메뉴_생성(불닭_메뉴.getId(), 2L));

		// when
		ExtractableResponse<Response> response = 주문_요청(주문_요청값);

		// then
		주문_생성됨(response);
	}

	@Test
	void 주문할_메뉴가_비어있는_경우_주문_생성에_실패한다() {
		// given
		OrderRequest 주문_요청값 = 주문_요청값_생성(주문_테이블.getId(), Collections.EMPTY_LIST);

		// when
		ExtractableResponse<Response> response = 주문_요청(주문_요청값);

		// then
		주문_생성에_실패함(response, HttpStatus.BAD_REQUEST.value());
	}

	@Test
	void 주문할_메뉴가_존재하지_않은_경우_주문_생성에_실패한다() {
		// given
		OrderRequest 주문_요청값 = 주문_요청값_생성(주문_테이블.getId(), 주문_메뉴_생성(1000L, 2L));

		// when
		ExtractableResponse<Response> response = 주문_요청(주문_요청값);

		// then
		주문_생성에_실패함(response, HttpStatus.BAD_REQUEST.value());
	}

	@Test
	void 주문할_테이블이_존재하지_않을_경우_주문_생성에_실패한다() {
		// given
		OrderRequest 주문_요청값 = 주문_요청값_생성(존재하지_않는_테이블.getId(), 주문_메뉴_생성(불닭_메뉴.getId(), 2L));

		// when
		ExtractableResponse<Response> response = 주문_요청(주문_요청값);

		// then
		주문_생성에_실패함(response, HttpStatus.BAD_REQUEST.value());
	}

	@Test
	void 주문_목록을_조회한다() {
		// given
		OrderResponse 생성된_주문 = 주문이_생성_되어_있음(주문_요청값_생성(주문_테이블.getId(), 주문_메뉴_생성(불닭_메뉴.getId(), 2L)));

		// when
		ExtractableResponse<Response> response = 주문_목록_조회_요청();

		// then
		주문_목록이_조회됨(response, Collections.singletonList(생성된_주문.getId()));
	}

	@Test
	void 주문_상태를_변경한다() {
		// given
		OrderResponse 생성된_주문 = 주문이_생성_되어_있음(주문_요청값_생성(주문_테이블.getId(), 주문_메뉴_생성(불닭_메뉴.getId(), 2L)));
		OrderStatusRequest 주문_변경_요청값 = OrderStatusRequest.from(OrderStatus.MEAL);

		// when
		ExtractableResponse<Response> response = 주문_상태_변경_요청(생성된_주문.getId(), 주문_변경_요청값);

		// then
		주문_상태가_변경됨(response);
	}

	@Test
	void 주문이_존재하지_않은_경우_주문_상태_변경에_실패한다() {
		// given
		OrderStatusRequest 주문_변경_요청값 = OrderStatusRequest.from(OrderStatus.MEAL);

		// when
		ExtractableResponse<Response> response = 주문_상태_변경_요청(1000L, 주문_변경_요청값);

		// then
		주문_상태_변경에_실패함(response, HttpStatus.BAD_REQUEST.value());
	}

	@Test
	void 주문_상태가_완료인_경우_주문_상태_변경에_실패한다() {
		// given
		OrderResponse 생성된_주문 = 주문이_생성_되어_있음(주문_요청값_생성(주문_테이블.getId(), 주문_메뉴_생성(불닭_메뉴.getId(), 2L)));
		OrderResponse 완료된_주문 = 주문_상태가_변경_되어_있음(생성된_주문.getId(), OrderStatus.COMPLETION);
		OrderStatusRequest 주문_변경_요청값 = OrderStatusRequest.from(OrderStatus.MEAL);

		// when
		ExtractableResponse<Response> response = 주문_상태_변경_요청(완료된_주문.getId(), 주문_변경_요청값);

		// then
		주문_상태_변경에_실패함(response, HttpStatus.BAD_REQUEST.value());
	}
}

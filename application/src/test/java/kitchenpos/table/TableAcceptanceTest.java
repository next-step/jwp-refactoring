package kitchenpos.table;

import static kitchenpos.menu.MenuAcceptanceStaticTest.*;
import static kitchenpos.menugroup.MenuGroupAcceptanceStaticTest.*;
import static kitchenpos.order.OrderAcceptanceStaticTest.*;
import static kitchenpos.product.ProductAcceptanceStaticTest.*;
import static kitchenpos.table.TableAcceptanceStaticTest.*;
import static kitchenpos.tablegroup.TableGroupAcceptanceStaticTest.*;

import java.util.Arrays;
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
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tablegroup.dto.TableGroupRequest;

@DisplayName("주문 테이블 : 인수 테스트")
class TableAcceptanceTest extends AcceptanceTest {

	private MenuResponse 불닭_메뉴;
	private List<MenuProductRequest> 불닭_두마리_메뉴_상품_리스트;

	@BeforeEach
	void setup() {
		MenuGroupResponse 두마리_메뉴_그룹 = 메뉴_그룹_생성되어_있음(메뉴_그룹_생성_요청값_생성("두마리메뉴"));
		ProductResponse 불닭 = 상품이_생성_되어있음(상품_요청값_생성("불닭", 16000));
		불닭_두마리_메뉴_상품_리스트 = 메뉴_상품_요청_생성_되어_있음(불닭);
		불닭_메뉴 = 메뉴가_생성_되어있음(메뉴_생성_요청값_생성("불닭 메뉴", 19000, 두마리_메뉴_그룹.getId(), 불닭_두마리_메뉴_상품_리스트));

	}

	@Test
	void 빈_테이블을_생성한다() {
		// given
		OrderTableRequest 빈_테이블_생성_요청값 = 테이블_요청값_생성(0, true);

		// when
		ExtractableResponse<Response> response = 테이블_생성_요청(빈_테이블_생성_요청값);

		// then
		테이블_생성됨(response);
	}

	@Test
	void 테이블_목록을_조회한다() {
		// given
		OrderTableResponse 생성된_테이블 = 테이블이_생성_되어있음(테이블_요청값_생성(0, true));

		// when
		ExtractableResponse<Response> response = 테이블_목록을_조회함();

		// then
		테이블_목록이_조회됨(response, Collections.singletonList(생성된_테이블.getId()));
	}

	@Test
	void 테이블의_상태를_변경한다() {
		// given
		OrderTableResponse 생성된_테이블 = 테이블이_생성_되어있음(테이블_요청값_생성(1, false));
		OrderResponse 조리_중인_주문 = 주문이_생성_되어_있음(주문_요청값_생성(생성된_테이블.getId(), 주문_메뉴_생성(불닭_메뉴.getId(), 2L)));
		주문_상태가_변경_되어_있음(조리_중인_주문.getId(), OrderStatus.COMPLETION);
		OrderTableRequest 변경할_상태 = 테이블_요청값_생성(1, true);

		// when
		ExtractableResponse<Response> response = 테이블_상태_변경_요청(생성된_테이블.getId(), 변경할_상태);

		// then
		테이블_상태_변경됨(response);
	}

	@Test
	void 테이블_그룹이_존재하면_테이블_상태_변경에_실패한다() {
		// given
		OrderTableResponse 생성된_테이블_1 = 테이블이_생성_되어있음(테이블_요청값_생성(1, true));
		OrderTableResponse 생성된_테이블_2 = 테이블이_생성_되어있음(테이블_요청값_생성(1, true));
		TableGroupRequest 테이블_그룹_생성_요청값 = 테이블_그룹_요청값_생성(Arrays.asList(생성된_테이블_1, 생성된_테이블_2));
		테이블_그룹이_생성_되어있음(테이블_그룹_생성_요청값);
		OrderTableRequest 변경할_상태 = 테이블_요청값_생성(1, true);

		// when
		ExtractableResponse<Response> response = 테이블_상태_변경_요청(생성된_테이블_1.getId(), 변경할_상태);

		// then
		테이블_상태_변경에_실패함(response, HttpStatus.BAD_REQUEST.value());
	}

	@Test
	void 테이블의_주문_상태가_완료가_아닐_경우_테이블_상태_변경에_실패한다() {
		// given
		OrderTableResponse 생성된_테이블 = 테이블이_생성_되어있음(테이블_요청값_생성(1, false));
		OrderResponse 조리_중인_주문 = 주문이_생성_되어_있음(주문_요청값_생성(생성된_테이블.getId(), 주문_메뉴_생성(불닭_메뉴.getId(), 2L)));
		주문_상태가_변경_되어_있음(조리_중인_주문.getId(), OrderStatus.COOKING);
		OrderTableRequest 변경할_상태_요청값 = 테이블_요청값_생성(1, true);

		// when
		ExtractableResponse<Response> response = 테이블_상태_변경_요청(생성된_테이블.getId(), 변경할_상태_요청값);

		// then
		테이블_상태_변경에_실패함(response, HttpStatus.BAD_REQUEST.value());
	}

	@Test
	void 테이블의_손님_인원_변경한다() {
		// given
		OrderTableResponse 생성된_테이블 = 테이블이_생성_되어있음(테이블_요청값_생성(1, false));
		OrderTableRequest 변경할_손님_인원_요청값 = 테이블_요청값_생성(3, true);

		// when
		ExtractableResponse<Response> response = 테이블_손님_인원_변경_요청(생성된_테이블.getId(), 변경할_손님_인원_요청값);

		// then
		테이블_손님_인원이_변경됨(response);
	}

	@Test
	void 테이블의_변경할_손님_인원이_0보다_작은_경우_변경에_실패한다() {
		// given
		OrderTableResponse 생성된_테이블 = 테이블이_생성_되어있음(테이블_요청값_생성(1, false));
		OrderTableRequest 변경할_손님_인원_요청값 = 테이블_요청값_생성(-1, false);

		// when
		ExtractableResponse<Response> response = 테이블_손님_인원_변경_요청(생성된_테이블.getId(), 변경할_손님_인원_요청값);

		// then
		테이블_손님_인원_변경_실패함(response, HttpStatus.BAD_REQUEST.value());
	}

	@Test
	void 존재하지_않는_테이블의_인원을_변경할_경우_변경에_실패한다() {
		// given
		Long 존재하지_않는_테이블_번호 = 200L;
		OrderTableRequest 변경할_손님_인원_요청값 = 테이블_요청값_생성(5, false);

		// when
		ExtractableResponse<Response> response = 테이블_손님_인원_변경_요청(존재하지_않는_테이블_번호, 변경할_손님_인원_요청값);

		// then
		테이블_손님_인원_변경_실패함(response, HttpStatus.BAD_REQUEST.value());
	}

	@Test
	void 인원을_변경하려는_테이블이_비어있는_상태인_경우_변경에_실패한다() {
		// given
		OrderTableResponse 생성된_테이블 = 테이블이_생성_되어있음(테이블_요청값_생성(1, true));
		OrderTableRequest 변경할_손님_인원_요청값 = 테이블_요청값_생성(3, true);

		// when
		ExtractableResponse<Response> response = 테이블_손님_인원_변경_요청(생성된_테이블.getId(), 변경할_손님_인원_요청값);

		// then
		테이블_손님_인원_변경_실패함(response, HttpStatus.BAD_REQUEST.value());
	}

}

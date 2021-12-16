package kitchenpos.table.domain;

import static kitchenpos.menu.domain.MenuAcceptanceStaticTest.*;
import static kitchenpos.menu.domain.MenuGroupAcceptanceStaticTest.*;
import static kitchenpos.order.domain.OrderAcceptanceStaticTest.*;
import static kitchenpos.product.domain.ProductAcceptanceStaticTest.*;
import static kitchenpos.table.domain.TableAcceptanceStaticTest.*;
import static kitchenpos.table.domain.TableGroupAcceptanceStaticTest.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.product.dto.ProductResponse;

@DisplayName("인수테스트 : 테이블 그룹(단체지정) 관련")
class TableGroupAcceptanceTest extends AcceptanceTest {

	private OrderTable 생성된_테이블_1;
	private OrderTable 생성된_테이블_2;
	private OrderTable 존재하지_않는_테이블 = new OrderTable();
	private Menu 불닭_메뉴;

	@BeforeEach
	void setup() {
		생성된_테이블_1 = 테이블이_생성_되어있음(테이블_요청값_생성(2, true));
		생성된_테이블_2 = 테이블이_생성_되어있음(테이블_요청값_생성(3, true));
		존재하지_않는_테이블.setId(200L);
		MenuGroupResponse 두마리_메뉴_그룹 = 메뉴_그룹_생성되어_있음(메뉴_그룹_생성_요청값_생성("두마리메뉴"));
		ProductResponse 불닭 = 상품이_생성_되어있음(상품_요청값_생성("불닭", 16000));
		List<MenuProduct> 불닭_두마리_메뉴_상품_리스트 = 메뉴_상품_생성되어_있음(불닭);
		불닭_메뉴 = 메뉴가_생성_되어있음(메뉴_생성_요청값_생성("불닭 메뉴", 19000, 두마리_메뉴_그룹.getId(), 불닭_두마리_메뉴_상품_리스트));
	}

	@Test
	void 테이블_그룹을_생성한다() {
		// given
		TableGroup 테이블_그룹_생성_요청값 = 테이블_그룹_요청값_생성(Arrays.asList(생성된_테이블_1, 생성된_테이블_2));

		// when
		ExtractableResponse<Response> response = 테이블_그룹_생성_요청(테이블_그룹_생성_요청값);

		// then
		테이블_그룹이_생성됨(response);
	}

	@Test
	void 주문_테이블이_없을_경우_테이블_그룹_생성에_실패한다() {
		// given
		TableGroup 테이블_그룹_생성_요청값 = 테이블_그룹_요청값_생성(Collections.emptyList());

		// when
		ExtractableResponse<Response> response = 테이블_그룹_생성_요청(테이블_그룹_생성_요청값);

		// then
		테이블_그룹_생성_실패함(response);
	}

	@Test
	void 주문_테이블이_하나인_경우_테이블_그룹_생성에_실패한다() {
		// given
		TableGroup 테이블_그룹_생성_요청값 = 테이블_그룹_요청값_생성(Collections.singletonList(생성된_테이블_1));

		// when
		ExtractableResponse<Response> response = 테이블_그룹_생성_요청(테이블_그룹_생성_요청값);

		// then
		테이블_그룹_생성_실패함(response);
	}

	@Test
	void 주문_테이블이_존재하지_않을_경우_테이블_그룹_생성에_실패한다() {
		// given
		TableGroup 테이블_그룹_생성_요청값 = 테이블_그룹_요청값_생성(Arrays.asList(생성된_테이블_1, 생성된_테이블_2, 존재하지_않는_테이블));

		// when
		ExtractableResponse<Response> response = 테이블_그룹_생성_요청(테이블_그룹_생성_요청값);

		// then
		테이블_그룹_생성_실패함(response);
	}

	@Test
	void 주문_테이블_중_비어있지_않은_테이블이_있는_경우_테이블_그룹_생성에_실패한다() {
		// given
		생성된_테이블_1.setEmpty(false);
		TableGroup 테이블_그룹_생성_요청값 = 테이블_그룹_요청값_생성(Arrays.asList(생성된_테이블_1, 생성된_테이블_2));

		// when
		ExtractableResponse<Response> response = 테이블_그룹_생성_요청(테이블_그룹_생성_요청값);

		// then
		테이블_그룹이_생성됨(response);
	}

	@Test
	void 테이블_그룹을_해체한다() {
		// given
		TableGroup 생성된_테이블_그룹 = 테이블_그룹이_생성_되어있음(테이블_그룹_요청값_생성(Arrays.asList(생성된_테이블_1, 생성된_테이블_2)));

		// when
		ExtractableResponse<Response> response = 테이블_그룹_해체_요청(생성된_테이블_그룹);

		// then
		테이블_그룹이_해체됨(response);
	}

	@Test
	void 주문_테이블_중_주문의_상태가_완료가_아닌_경우_테이블_그룹_해체에_실패한다() {
		// given
		TableGroup 생성된_테이블_그룹 = 테이블_그룹이_생성_되어있음(테이블_그룹_요청값_생성(Arrays.asList(생성된_테이블_1, 생성된_테이블_2)));
		Order 조리_중인_주문 = 주문이_생성_되어_있음(주문_요청값_생성(생성된_테이블_1.getId(), 주문_메뉴_생성(불닭_메뉴, 2)));
		조리_중인_주문.setOrderStatus(OrderStatus.COOKING.name());
		생성된_테이블_1.setEmpty(false);
		테이블_상태_변경_요청(생성된_테이블_1.getId(), 생성된_테이블_1);

		// when
		ExtractableResponse<Response> response = 테이블_그룹_해체_요청(생성된_테이블_그룹);

		// then
		테이블_그룹_해체에_실패함(response);
	}

}

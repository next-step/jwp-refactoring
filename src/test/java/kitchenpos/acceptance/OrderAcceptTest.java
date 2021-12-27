package kitchenpos.acceptance;

import static kitchenpos.acceptance.step.OrderAcceptStep.*;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.acceptance.step.TableAcceptStep;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.menu.acceptance.step.MenuAcceptStep;
import kitchenpos.menu.acceptance.step.MenuGroupAcceptStep;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.acceptance.ProductAcceptStep;
import kitchenpos.product.dto.ProductResponse;

@DisplayName("주문 인수테스트")
public class OrderAcceptTest extends AcceptanceTest {

	private static final String BASE_URL = "/api/orders";

	private MenuResponse 후라이드둘;
	private OrderTable 테이블;

	@BeforeEach
	void setup() {
		ProductResponse 후라이드 = ProductAcceptStep.상품이_등록되어_있음("후라이드", 15_000);
		MenuGroupResponse 추천메뉴 = MenuGroupAcceptStep.메뉴_그룹이_등록되어_있음("추천메뉴");
		MenuProductRequest 메뉴_상품 = new MenuProductRequest(후라이드.getId(), 2L);

		후라이드둘 = MenuAcceptStep.메뉴가_등록되어_있음("후라이드둘", 30_000, 추천메뉴, 메뉴_상품);
		테이블 = TableAcceptStep.테이블_등록_되어_있음(2, false);
	}

	@DisplayName("주문 관리")
	@Test
	void 주문_관리() {
		// given
		OrderLineItem 주문_항목 = new OrderLineItem();
		주문_항목.setMenuId(후라이드둘.getId());
		주문_항목.setQuantity(1);

		Order 등록_요청_데이터 = new Order();
		등록_요청_데이터.setOrderTableId(테이블.getId());
		등록_요청_데이터.setOrderLineItems(Collections.singletonList(주문_항목));

		// when
		ExtractableResponse<Response> 주문_생성_응답 = 주문_생성_요청(등록_요청_데이터);

		// then
		Order 생성된_주문 = 주문_등록_확인(주문_생성_응답, 등록_요청_데이터);

		// when
		ExtractableResponse<Response> 주문_목록_조회_응답 = 주문_목록_조회_요청();

		// then
		주문_목록_조회_확인(주문_목록_조회_응답, 생성된_주문);

		// given
		Order 상태_변경_요청_데이터 = new Order();
		상태_변경_요청_데이터.setOrderStatus("MEAL");

		// when
		ExtractableResponse<Response> 주문_상태_변경_응답 = 주문_상태_변경_요청(주문_생성_응답, 상태_변경_요청_데이터);

		// then
		주문_상태_변경_확인(주문_상태_변경_응답, 상태_변경_요청_데이터);
	}

}

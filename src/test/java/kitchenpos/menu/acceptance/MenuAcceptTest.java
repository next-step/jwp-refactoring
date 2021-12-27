package kitchenpos.menu.acceptance;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.acceptance.ProductAcceptStep;
import kitchenpos.product.dto.ProductResponse;

@DisplayName("메뉴 인수 테스트")
public class MenuAcceptTest extends AcceptanceTest {

	private MenuGroupResponse 추천메뉴;
	private ProductResponse 후라이드;

	@BeforeEach
	void setup() {
		추천메뉴 = MenuGroupAcceptStep.메뉴_그룹이_등록되어_있음("추천메뉴");
		후라이드 = ProductAcceptStep.상품이_등록되어_있음("후라이드", 15_000);
	}

	@DisplayName("메뉴를 관리한다")
	@Test
	void 메뉴를_관리한다() {
		// given
		MenuProductRequest 메뉴_상품 = new MenuProductRequest(후라이드.getId(), 2L);

		MenuRequest 메뉴_생성_요청_데이터 = new MenuRequest("후라이드+후라이드", BigDecimal.valueOf(30_000), 추천메뉴.getId(),
			Collections.singletonList(메뉴_상품));

		// when
		ExtractableResponse<Response> 메뉴_생성_응답 = MenuAcceptStep.메뉴_생성_요청(메뉴_생성_요청_데이터);

		// then
		MenuResponse 생성된_메뉴 = MenuAcceptStep.메뉴_생성_확인(메뉴_생성_응답, 메뉴_생성_요청_데이터);

		// when
		ExtractableResponse<Response> 메뉴_조회_응답 = MenuAcceptStep.메뉴_목록_조회_요청();

		// then
		MenuAcceptStep.메뉴_목록_조회_확인(메뉴_조회_응답, 생성된_메뉴);
	}
}

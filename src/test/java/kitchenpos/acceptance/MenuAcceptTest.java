package kitchenpos.acceptance;

import java.math.BigDecimal;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.acceptance.step.MenuAcceptStep;
import kitchenpos.acceptance.step.MenuGroupAcceptStep;
import kitchenpos.acceptance.step.ProductAcceptStep;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@DisplayName("메뉴 인수 테스트")
public class MenuAcceptTest extends AcceptanceTest {

	private MenuGroup 추천메뉴;
	private Product 후라이드;

	@BeforeEach
	void setup() {
		추천메뉴 = MenuGroupAcceptStep.메뉴_그룹이_등록되어_있음("추천메뉴");
		후라이드 = ProductAcceptStep.상품이_등록되어_있음("후라이드", 15_000);
	}

	@DisplayName("메뉴를 관리한다")
	@Test
	void 메뉴를_관리한다() {
		// given
		MenuProduct 메뉴_상품 = new MenuProduct();
		메뉴_상품.setProductId(후라이드.getId());
		메뉴_상품.setQuantity(2);

		Menu 메뉴_생성_요청_데이터 = new Menu();
		메뉴_생성_요청_데이터.setName("후라이드+후라이드");
		메뉴_생성_요청_데이터.setPrice(BigDecimal.valueOf(30_000));
		메뉴_생성_요청_데이터.setMenuGroupId(추천메뉴.getId());
		메뉴_생성_요청_데이터.setMenuProducts(Collections.singletonList(메뉴_상품));

		// when
		ExtractableResponse<Response> 메뉴_생성_응답 = MenuAcceptStep.메뉴_생성_요청(메뉴_생성_요청_데이터);

		// then
		Menu 생성된_메뉴 = MenuAcceptStep.메뉴_생성_확인(메뉴_생성_응답, 메뉴_생성_요청_데이터);

		// when
		ExtractableResponse<Response> 메뉴_조회_응답 = MenuAcceptStep.메뉴_목록_조회_요청();

		// then
		MenuAcceptStep.메뉴_목록_조회_확인(메뉴_조회_응답, 생성된_메뉴);
	}
}

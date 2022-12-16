package kitchenpos.acceptance.menu;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest2;
import kitchenpos.acceptance.menugroup.MenuGroupAcceptanceTestStep;
import kitchenpos.acceptance.menugroup.MenuGroupFixture;
import kitchenpos.acceptance.product.ProductAcceptanceTestStep;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.ui.dto.MenuGroupResponse;
import kitchenpos.ui.dto.MenuRequest;
import kitchenpos.ui.dto.ProductRequest;
import kitchenpos.ui.dto.ProductResponse;

@DisplayName("메뉴 관리")
class MenuAcceptanceTest extends AcceptanceTest2 {

	MenuAcceptanceTestStep step = new MenuAcceptanceTestStep();
	MenuGroupAcceptanceTestStep menuGroups = new MenuGroupAcceptanceTestStep();
	ProductAcceptanceTestStep products = new ProductAcceptanceTestStep();

	List<ProductResponse> 상품목록;

	/**
	 * Feature: 메뉴 관리 기능
	 * Background
	 *   When 상품목록 등록을 요청하면
	 *   Then 상품목록 등록에 성공한다
	 */
	@BeforeEach
	void setup() {
		List<ProductRequest> 상품목록 = ProductFixture.상품목록2(3);

		List<ExtractableResponse<Response>> 등록_응답 = products.등록_요청(상품목록);

		this.상품목록 = products.등록됨(등록_응답);
	}

	/**
	 * Scenario: 메뉴 관리
	 * When 메뉴 그룹 등록을 요청하면
	 * Then 메뉴 그룹 등록에 성공한다
	 * When 메뉴 등록을 요청하면
	 * Then 메뉴 등록에 성공한다
	 */
	@Test
	void 메뉴_관리() {

		MenuGroupResponse 메뉴_그룹 = 메뉴_그룹_등록되어_있음();

		MenuRequest 메뉴 = MenuFixture.메뉴(상품목록, 메뉴_그룹);

		ExtractableResponse<Response> 등록_요청_응답 = step.등록_요청(메뉴);

		step.등록됨(등록_요청_응답);
	}

	/**
	 * When 메뉴 그룹이 존재하지 않을 경우
	 * Than 메뉴 등록에 실패한다
	 */
	@Test
	void 메뉴_그룹이_존재하지_않을_경우() {
		// given
		MenuGroupResponse 존재하지_않는_메뉴_그룹 = MenuGroupFixture.메뉴그룹(1L, "존재하지 않는 메뉴 그룹");
		MenuRequest 메뉴 = MenuFixture.메뉴(상품목록, 존재하지_않는_메뉴_그룹);
		// when
		ExtractableResponse<Response> 등록_요청_응답 = step.등록_요청(메뉴);
		// then
		step.등록_실패함(등록_요청_응답);
	}

	/**
	 * When 메뉴의 가격이 상품의 가격의 합보다 작으면
	 * Than 메뉴 등록에 실패한다
	 */
	@Test
	void 상품가격이_메뉴가격의_합보다_작음() {
		// given
		MenuGroupResponse 메뉴_그룹 = 메뉴_그룹_등록되어_있음();
		MenuRequest 메뉴 = MenuFixture.유효하지_않은_가격_메뉴(상품목록, 메뉴_그룹);
		// when
		ExtractableResponse<Response> 등록_요청_응답 = step.등록_요청(메뉴);
		// then
		step.등록_실패함(등록_요청_응답);
	}

	private MenuGroupResponse 메뉴_그룹_등록되어_있음() {
		ExtractableResponse<Response> 등록_응답 = menuGroups.등록_요청(MenuGroupFixture.메뉴그룹());
		return menuGroups.등록됨(등록_응답);
	}

}

package kitchenpos.acceptance;

import java.util.List;
import java.util.function.ToLongFunction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.ProductFixture;

@DisplayName("메뉴 관리")
class MenuAcceptanceTest extends AcceptanceTest<Menu> {

	static final String MENU_REQUEST_PATH = "/api/menus";
	static final int 메뉴가격 = 10_000;

	List<Product> 상품목록;

	MenuGroupAcceptanceTest menuGroups;
	ProductAcceptanceTest products;

	/**
	 * Feature: 메뉴 관리 기능
	 * Background
	 *   When 상품목록 등록을 요청하면
	 *   Then 상품목록 등록에 성공한다
	 */
	@BeforeEach
	void setup() {
		menuGroups = new MenuGroupAcceptanceTest();
		products = new ProductAcceptanceTest();

		상품목록 = products.상품_등록되어_있음(ProductFixture.상품목록(3));
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
		MenuGroup 메뉴_그룹 = menuGroups.메뉴_그룹_등록되어_있음();

		Menu 메뉴 = MenuFixture.메뉴(상품목록, 메뉴_그룹);

		ExtractableResponse<Response> 등록_요청_응답 = 등록_요청(메뉴);

		등록됨(등록_요청_응답);
	}

	/**
	 * Scenario: 메뉴 등록 실패
	 * Given 메뉴 그룹이 등록되어 있고
	 * When 메뉴 그룹이 존재하지 않을 경우
	 * Than 메뉴 등록에 실패한다
	 * When 메뉴 가격이 상품의 가격 합보다 크면
	 * Than 메뉴 등록에 실패한다
	 */
	@Test
	void 메뉴_등록_실패() {
		// given
		MenuGroup 메뉴_그룹 = menuGroups.메뉴_그룹_등록되어_있음();

		// when
		Menu 메뉴 = MenuFixture.메뉴(상품목록, 메뉴_그룹);
		메뉴.setMenuGroupId(null);
		ExtractableResponse<Response> 등록_요청_응답 = 등록_요청(메뉴);
		// then
		등록_실패함(등록_요청_응답);

		// when
		int 유효하지_않은_메뉴_가격 = 메뉴가격 * 2;
		메뉴 = MenuFixture.메뉴(상품목록, 메뉴_그룹, 유효하지_않은_메뉴_가격);
		등록_요청_응답 = 등록_요청(메뉴);
		// then
		등록_실패함(등록_요청_응답);
	}

	public Menu 메뉴_등록되어_있음(Menu 메뉴) {
		ExtractableResponse<Response> 등록_요청 = 등록_요청(메뉴);
		return 등록됨(등록_요청);
	}

	@Override
	protected String getRequestPath() {
		return MENU_REQUEST_PATH;
	}

	@Override
	protected ToLongFunction<Menu> idExtractor() {
		return Menu::getId;
	}

	@Override
	protected Class<Menu> getDomainClass() {
		return Menu.class;
	}
}

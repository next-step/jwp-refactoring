package kitchenpos.acceptance;

import static kitchenpos.acceptance.ProductAcceptanceTest.상품;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@DisplayName("메뉴 관리")
class MenuAcceptanceTest extends AcceptanceTest<Menu> {

	static final String MENU_REQUEST_PATH = "/api/menus";
	static final int 메뉴가격 = 10_000;
	static final String 메뉴명 = "후라이드치킨";

	List<Long> 상품_아이디_목록 = new ArrayList<>();

	MenuGroupAcceptanceTest menuGroups;
	ProductAcceptanceTest products;

	/**
	 * Feature: 메뉴 관리 기능
	 * Background
	 *   When 상품 등록을 요청하면
	 *   Then 상품 등록에 성공한다
	 */
	@BeforeEach
	void setup() {
		menuGroups = new MenuGroupAcceptanceTest();
		products = new ProductAcceptanceTest();

		List<Product> 상품_목록 = Lists.newArrayList(상품("너티 크루아상", 3000),
												 상품("단호박 크림 수프", 6000),
												 상품("사과 가득 젤리", 4000));
		상품_아이디_목록.addAll(
			상품_목록.stream()
			.map(상품 -> {
				ExtractableResponse<Response> 등록_응답 = products.등록_요청(상품);
				return products.등록됨(등록_응답).getId();
			}).collect(Collectors.toList()));
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

		Menu 메뉴 = 메뉴(메뉴_그룹);

		ExtractableResponse<Response> 등록_요청_응답 = 등록_요청(메뉴);

		등록됨(등록_요청_응답);
	}

	/**
	 * Scenario: 메뉴 등록 실패
	 * Given 메뉴 그룹이 등록되어 있고
	 * When 메뉴 가격이 0원보다 작을 경우
	 * Than 메뉴 등록에 실패한다
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
		int 유효하지_않은_메뉴_가격 = -1;
		Menu 메뉴 = 메뉴(메뉴_그룹, 유효하지_않은_메뉴_가격, 메뉴상품(상품_아이디_목록));
		ExtractableResponse<Response> 등록_요청_응답 = 등록_요청(메뉴);
		// then
		등록_실패함(등록_요청_응답);

		// when
		메뉴 = 메뉴(메뉴_그룹);
		메뉴.setMenuGroupId(null);
		등록_요청_응답 = 등록_요청(메뉴);
		// then
		등록_실패함(등록_요청_응답);

		// when
		유효하지_않은_메뉴_가격 = 메뉴가격 * 2;
		메뉴 = 메뉴(메뉴_그룹, 유효하지_않은_메뉴_가격, 메뉴상품(상품_아이디_목록));
		등록_요청_응답 = 등록_요청(메뉴);
		// then
		등록_실패함(등록_요청_응답);
	}

	private Menu 메뉴(MenuGroup 메뉴_그룹) {
		return 메뉴(메뉴_그룹, 메뉴가격, 메뉴상품(상품_아이디_목록));
	}

	public static Menu 메뉴(MenuGroup 메뉴_그룹, List<Product> 상품, int 가격) {
		List<Long> 상품_아이디_목록 = 상품.stream()
			.map(Product::getId)
			.collect(Collectors.toList());
		Menu menu = new Menu();
		menu.setName(메뉴명);
		menu.setMenuGroupId(메뉴_그룹.getId());
		menu.setMenuProducts(메뉴상품(상품_아이디_목록));
		menu.setPrice(BigDecimal.valueOf(가격));
		return menu;
	}

	public static Menu 메뉴(MenuGroup 메뉴_그룹, int 가격, List<MenuProduct> 메뉴상품) {
		Menu menu = new Menu();
		menu.setName(메뉴명);
		menu.setMenuGroupId(메뉴_그룹.getId());
		menu.setMenuProducts(메뉴상품);
		menu.setPrice(BigDecimal.valueOf(가격));
		return menu;
	}

	private static List<MenuProduct> 메뉴상품(List<Long> 상품_아이디_목록) {
		return 상품_아이디_목록.stream()
			.map(id -> {
				MenuProduct menuProduct = new MenuProduct();
				menuProduct.setProductId(id);
				menuProduct.setQuantity(1);
				return menuProduct;
			})
			.collect(Collectors.toList());
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

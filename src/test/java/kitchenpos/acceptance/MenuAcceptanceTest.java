package kitchenpos.acceptance;

import static kitchenpos.acceptance.ProductAcceptanceTest.상품;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.utils.RestAssuredUtils;

@DisplayName("메뉴 관리")
class MenuAcceptanceTest extends AcceptanceTest {

	static final String MENU_REQUEST_PATH = "/api/menus";
	static final String MENU_GROUP_REQUEST_PATH = "/api/menu-groups";

	List<Long> 상품_아이디_목록 = new ArrayList<>();
	Long 메뉴_그룹_아이디;

	/**
	 * Feature: 메뉴 관리 기능
	 * Background
	 *   when 상품을 등록을 요청하면
	 *   then 상품 등록에 성공한다
	 */
	@BeforeEach
	void setup() {
		Long 상품_아이디 = ProductAcceptanceTest.상품_등록_요청(상품("너티 크루아상", 3000));
		상품_아이디_목록.add(상품_아이디);
		상품_아이디 = ProductAcceptanceTest.상품_등록_요청(상품("단호박 크림 수프", 6000));
		상품_아이디_목록.add(상품_아이디);
		상품_아이디 = ProductAcceptanceTest.상품_등록_요청(상품("사과 가득 젤리", 4000));
		상품_아이디_목록.add(상품_아이디);
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
		MenuGroup 메뉴_그룹 = new MenuGroup();
		메뉴_그룹.setName("푸드");
		메뉴_그룹_아이디 = 메뉴_그룹_등록_요청(메뉴_그룹);

		메뉴_그룹_등록됨(메뉴_그룹_아이디);

		Menu 메뉴 = 메뉴();

		Long 메뉴_아이디 = 메뉴_등록_요청(메뉴);
		메뉴_등록됨(메뉴_아이디);
	}

	/**
	 * Scenario: 메뉴 등록 실패
	 * Given 메뉴 그룹이 등록되어 있고
	 * When 메뉴 가격이 0원보다 작을 경우
	 * Than 메뉴 등록에 실패한다
	 * When 메뉴 그룹이 존재하지 않을 경우
	 * Than 메뉴 등록에 실패한다
	 * When 메뉴 가격이 상품의 가격 합보다 작으면
	 * Than 메뉴 등록에 실패한다
	 */
	@Test
	void 메뉴_등록_실패() {
		메뉴그룹_등록되어_있음();

		int 메뉴_가격 = -1;
		Menu 메뉴 = 메뉴(메뉴_가격);

		메뉴_등록_실패함(메뉴);

		메뉴 = 메뉴();
		메뉴.setMenuGroupId(null);
		메뉴_등록_실패함(메뉴);

		메뉴 = 메뉴(50_000);
		메뉴_등록_실패함(메뉴);
	}

	private void 메뉴그룹_등록되어_있음() {
		MenuGroup 메뉴_그룹 = new MenuGroup();
		메뉴_그룹.setName("푸드");
		메뉴_그룹_아이디 = 메뉴_그룹_등록_요청(메뉴_그룹);

		메뉴_그룹_등록됨(메뉴_그룹_아이디);
	}

	private Long 메뉴_등록_요청(Menu menu) {
		ExtractableResponse<Response> 등록_응답 = RestAssuredUtils.post(MENU_REQUEST_PATH, menu);
		assertThat(등록_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		return 등록_응답.body().as(Menu.class).getId();
	}

	public static Long 메뉴_그룹_등록_요청(MenuGroup menuGroup) {
		ExtractableResponse<Response> 등록_응답 = RestAssuredUtils.post(MENU_GROUP_REQUEST_PATH, menuGroup);
		assertThat(등록_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		return 등록_응답.body().as(MenuGroup.class).getId();
	}

	private void 메뉴_그룹_등록됨(Long 메뉴_그룹_아이디) {
		ExtractableResponse<Response> 목록_응답 = RestAssuredUtils.get(MENU_GROUP_REQUEST_PATH);

		assertThat(목록_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(목록_응답.body().as(new TypeRef<List<MenuGroup>>(){}))
			.extracting(MenuGroup::getId)
			.contains(메뉴_그룹_아이디);
	}

	private void 메뉴_등록됨(Long ...메뉴_그룹_아이디) {
		ExtractableResponse<Response> 목록_응답 = RestAssuredUtils.get(MENU_REQUEST_PATH);

		assertThat(목록_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(목록_응답.body().as(new TypeRef<List<Menu>>(){}))
			.extracting(Menu::getId)
			.contains(메뉴_그룹_아이디);
	}

	private void 메뉴_등록_실패함(Menu 메뉴) {
		ExtractableResponse<Response> 등록_응답 = RestAssuredUtils.post(MENU_REQUEST_PATH, 메뉴);
		assertThat(등록_응답.statusCode()).isNotEqualTo(HttpStatus.OK.value());
	}

	private Menu 메뉴() {
		return 메뉴(10_000);
	}

	private Menu 메뉴(int price) {
		Menu menu = new Menu();
		menu.setName("메뉴1");
		menu.setMenuGroupId(메뉴_그룹_아이디);
		menu.setMenuProducts(메뉴상품(상품_아이디_목록));
		menu.setPrice(BigDecimal.valueOf(price));
		return menu;
	}

	private List<MenuProduct> 메뉴상품(List<Long> 상품_아이디_목록) {
		return 상품_아이디_목록.stream()
			.map(id -> {
				MenuProduct menuProduct = new MenuProduct();
				menuProduct.setProductId(id);
				menuProduct.setQuantity(1);
				return menuProduct;
			})
			.collect(Collectors.toList());
	}
}

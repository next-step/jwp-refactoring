package kitchenpos.acceptance.menu;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.menugroup.MenuGroupAcceptance;
import kitchenpos.acceptance.product.ProductAcceptance;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@DisplayName("메뉴와 메뉴상품 인수 테스트")
public class MenuAcceptanceTest extends MenuAcceptance {

	private Product 치킨;
	private Product 피자;
	private MenuGroup food;
	private List<MenuProduct> products;

	@BeforeEach
	void initData() {
		치킨 = ProductAcceptance.상품_등록되어_있음("치킨", 16000).as(Product.class);
		피자 = ProductAcceptance.상품_등록되어_있음("피자", 20000).as(Product.class);
		food = MenuGroupAcceptance.메뉴_그룹_등록되어_있음("음식").as(MenuGroup.class);

		products = Arrays.asList(MenuProduct.of(치킨, 2), MenuProduct.of(피자, 1));
	}

	@DisplayName("메뉴를 생성한다.")
	@Test
	void createMenuTest() {
		// given
		Menu menu = Menu.of(null, "치피세트", 50000, food.getId(), products);

		// when
		ExtractableResponse<Response> response = 메뉴_등록_요청(menu);

		// then
		메뉴_등록됨(response);
	}

	@DisplayName("메뉴 생성에 실패한다.")
	@ParameterizedTest
	@ValueSource(longs = {-50000, 55000})
	void createErrorMenuTest(long price) {
		// given
		Menu menu = Menu.of(null, "치피세트", price, food.getId(), products);

		// when
		ExtractableResponse<Response> response = 메뉴_등록_요청(menu);

		// then
		메뉴_등록_실패됨(response);
	}

	@DisplayName("모든 메뉴를 조회한다.")
	@Test
	void selectMenusTest() {
		// given
		Menu menu1 = Menu.of(null, "치피세트", 50000, food.getId(), products);
		Menu menu2 = Menu.of(null, "치치세트", 30000, food.getId(), Collections.singletonList(MenuProduct.of(치킨, 2)));
		ExtractableResponse<Response> createResponse1 = 메뉴_등록_요청(menu1);
		ExtractableResponse<Response> createResponse2 = 메뉴_등록_요청(menu2);

		// when
		ExtractableResponse<Response> response = 메뉴_조회_요청();

		// then
		메뉴_목록_조회됨(response);
		메뉴_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2));
	}
}

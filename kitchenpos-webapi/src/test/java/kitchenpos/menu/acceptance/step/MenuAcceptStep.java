package kitchenpos.menu.acceptance.step;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.http.HttpStatus;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.testutils.RestAssuredUtils;

public class MenuAcceptStep {

	private static final String BASE_URL = "/api/menus";

	public static ExtractableResponse<Response> 메뉴_생성_요청(MenuRequest 메뉴_생성_요청_데이터) {
		return RestAssuredUtils.post(BASE_URL, 메뉴_생성_요청_데이터);
	}

	public static MenuResponse 메뉴_생성_확인(ExtractableResponse<Response> 메뉴_생성_응답, MenuRequest 메뉴_생성_요청_데이터) {
		MenuResponse 생성된_메뉴 = 메뉴_생성_응답.as(MenuResponse.class);
		assertAll(
			() -> assertThat(메뉴_생성_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
			() -> assertThat(생성된_메뉴).satisfies(생성된_메뉴_확인(메뉴_생성_요청_데이터))
		);
		return 생성된_메뉴;
	}

	private static Consumer<MenuResponse> 생성된_메뉴_확인(MenuRequest 생성_요청_메뉴) {
		return menu -> {
			assertThat(menu.getId()).isNotNull();
			assertThat(menu.getName()).isEqualTo(생성_요청_메뉴.getName());
			assertThat(menu.getMenuGroupId()).isEqualTo(생성_요청_메뉴.getMenuGroupId());
			메뉴_상품_확인(menu.getMenuProducts(), 생성_요청_메뉴.getMenuProducts());
		};
	}

	private static void 메뉴_상품_확인(List<MenuProductResponse> menuProducts, List<MenuProductRequest> expectedList) {
		MenuProductRequest expected = expectedList.get(0);

		assertThat(menuProducts.size()).isOne();
		assertThat(menuProducts).first()
			.satisfies(menuProduct -> {
				assertThat(menuProduct.getProductId()).isEqualTo(expected.getProductId());
				assertThat(menuProduct.getQuantity()).isEqualTo(expected.getQuantity());
			});
	}

	public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
		return RestAssuredUtils.get(BASE_URL);
	}

	public static void 메뉴_목록_조회_확인(ExtractableResponse<Response> 메뉴_조회_응답, MenuResponse 등록된_메뉴) {
		List<MenuResponse> 조회된_메뉴_목록 = 메뉴_조회_응답.as(new TypeRef<List<MenuResponse>>() {
		});

		assertAll(
			() -> assertThat(메뉴_조회_응답.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(조회된_메뉴_목록).satisfies(조회된_메뉴_목록_확인(등록된_메뉴))
		);
	}

	private static Consumer<List<? extends MenuResponse>> 조회된_메뉴_목록_확인(MenuResponse 등록된_메뉴) {
		return menus -> {
			assertThat(menus.size()).isOne();
			assertThat(menus).first()
				.satisfies(menu -> {
					assertThat(menu.getId()).isEqualTo(등록된_메뉴.getId());
					assertThat(menu.getName()).isEqualTo(등록된_메뉴.getName());
					assertThat(menu.getPrice()).isEqualByComparingTo(등록된_메뉴.getPrice());
				});
		};
	}

	public static MenuResponse 메뉴가_등록되어_있음(String menuName, int price, MenuGroupResponse 추천메뉴,
		MenuProductRequest 메뉴_상품) {
		MenuRequest menu = new MenuRequest(menuName, BigDecimal.valueOf(price), 추천메뉴.getId(),
			Collections.singletonList(메뉴_상품));
		return 메뉴_생성_요청(menu).as(MenuResponse.class);
	}
}

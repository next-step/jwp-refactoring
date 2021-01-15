package kitchenpos.menu.ui;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.ui.MenuGroupAcceptanceTest;
import kitchenpos.product.ui.ProductAcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MenuAcceptanceTest extends AcceptanceTest {

	@DisplayName("메뉴를 관리한다")
	@Test
	void manage() {
		//메뉴그룹이 등록되어 있다.
		Long menuGroupId = MenuGroupAcceptanceTest.메뉴그룹이_등록되어_있다("모닝세트");
		//상품이 등록되어 있다.
		Long productId1 = ProductAcceptanceTest.상품이_등록되어_있다("샐러드", 5_000);
		Long productId2 = ProductAcceptanceTest.상품이_등록되어_있다("아메리카노", 2_500);
		List<MenuProductRequest> menuProductRequests = Arrays.asList(
			  new MenuProductRequest(productId1, 1L),
			  new MenuProductRequest(productId2, 1L)
		);

		MenuRequest menuRequest = new MenuRequest("모닝 샐러드세트", new BigDecimal(7_500), menuGroupId,
			  menuProductRequests);

		//when
		ExtractableResponse<Response> response = 메뉴_등록을_요청한다(menuRequest);

		//then
		메뉴가_등록됨(response);

		//when
		ExtractableResponse<Response> listResponse = 메뉴목록_조회를_요청한다();

		메뉴목록이_조회됨(response, listResponse);
	}

	@DisplayName("등록되지 않은 메뉴그룹인 경우 상품을 등록할 수 없음")
	@Test
	void createWithNotExistMenuGroup() {
		//상품이 등록되어 있다.
		Long productId1 = ProductAcceptanceTest.상품이_등록되어_있다("파니니", 5_000);
		Long productId2 = ProductAcceptanceTest.상품이_등록되어_있다("카페라떼", 2_500);
		List<MenuProductRequest> menuProductRequests = Arrays.asList(
			  new MenuProductRequest(productId1, 1L),
			  new MenuProductRequest(productId2, 1L)
		);
		MenuRequest menuRequest = new MenuRequest("브런치세트", new BigDecimal(7_500), 0L,
			  menuProductRequests);

		//when
		ExtractableResponse<Response> response = 메뉴_등록을_요청한다(menuRequest);

		//then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	public static Long 메뉴가_등록되어_있음(String menuName, String menuGroupName, int price, Long... productIds) {
		//메뉴그룹이 등록되어 있다.
		Long menuGroupId = MenuGroupAcceptanceTest.메뉴그룹이_등록되어_있다(menuGroupName);
		//상품이 등록되어 있다.
		List<MenuProductRequest> menuProductRequests = Arrays.stream(productIds)
			  .map(id -> new MenuProductRequest(id, 1L))
			  .collect(Collectors.toList());

		MenuRequest menuRequest = new MenuRequest(menuName, new BigDecimal(price), menuGroupId,
			  menuProductRequests);

		ExtractableResponse<Response> response = 메뉴_등록을_요청한다(menuRequest);
		return response.jsonPath().getLong("id");
	}

	public static ExtractableResponse<Response> 메뉴_등록을_요청한다(MenuRequest menuRequest) {
		return RestAssured.given().log().all()
			  .contentType(MediaType.APPLICATION_JSON_VALUE)
			  .body(menuRequest)
			  .when().post("/api/menus")
			  .then().log().all()
			  .extract();
	}

	private ExtractableResponse<Response> 메뉴목록_조회를_요청한다() {
		ExtractableResponse<Response> listResponse = RestAssured.given().log().all()
			  .contentType(MediaType.APPLICATION_JSON_VALUE)
			  .when().get("/api/menus")
			  .then().log().all()
			  .extract();
		return listResponse;
	}

	private void 메뉴가_등록됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.header("Location")).isNotEmpty();
	}

	private void 메뉴목록이_조회됨(ExtractableResponse<Response> response,
		  ExtractableResponse<Response> listResponse) {
		assertThat(listResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(listResponse.jsonPath().getList(".", MenuResponse.class)).contains(
			  response.body().as(MenuResponse.class));
	}
}

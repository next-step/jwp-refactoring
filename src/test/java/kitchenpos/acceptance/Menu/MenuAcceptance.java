package kitchenpos.acceptance.Menu;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.util.AcceptanceTest;
import kitchenpos.domain.Menu;

public class MenuAcceptance extends AcceptanceTest {

	public static final String MENUS_REQUEST_URL = "/api/menus";

	public static ExtractableResponse<Response> 메뉴_등록_요청(Menu menu) {
		return RestAssured
			.given().log().all()
			.body(menu)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post(MENUS_REQUEST_URL)
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 메뉴_조회_요청() {
		return RestAssured
			.given().log().all()
			.when().get(MENUS_REQUEST_URL)
			.then().log().all().extract();
	}

	public static void 메뉴_등록됨(ExtractableResponse<Response> response) {
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
			() -> assertThat(response.as(Menu.class)).isNotNull(),
			() -> assertThat(response.as(Menu.class).getId()).isNotNull()
		);
	}

	public static void 메뉴_등록_실패됨(ExtractableResponse<Response> response) {
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value())
		);
	}

	public static void 메뉴_목록_조회됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	public static void 메뉴_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> expected) {
		List<Long> expectedMenuIds = expected.stream()
			.map(it -> it.as(Menu.class).getId())
			.collect(Collectors.toList());

		List<Long> resultMenuIds = response.jsonPath().getList(".", Menu.class).stream()
			.map(Menu::getId)
			.collect(Collectors.toList());

		assertThat(resultMenuIds).containsAll(expectedMenuIds);
	}
}

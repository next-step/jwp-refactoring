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
import kitchenpos.domain.MenuGroup;

public class MenuGroupAcceptance extends AcceptanceTest {

	public static final String MENU_GROUP_REQUEST_URL = "/api/menu-groups";

	public static ExtractableResponse<Response> 메뉴_그룹_등록_요청(MenuGroup menuGroup) {
		return RestAssured
			.given().log().all()
			.body(menuGroup)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post(MENU_GROUP_REQUEST_URL)
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 메뉴_그룹_조회_요청() {
		return RestAssured
			.given().log().all()
			.when().get(MENU_GROUP_REQUEST_URL)
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 메뉴_그룹_등록되어_있음(String menuName) {
		return 메뉴_그룹_등록_요청(MenuGroup.of(null, menuName));
	}

	public static void 메뉴_그룹_등록됨(ExtractableResponse<Response> response) {
		MenuGroup expected = response.as(MenuGroup.class);
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
			() -> assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE),
			() -> assertThat(response.header("Location")).isEqualTo(MENU_GROUP_REQUEST_URL + "/" + expected.getId()),
			() -> assertThat(expected).isNotNull(),
			() -> assertThat(expected.getId()).isNotNull()
		);
	}

	public static void 메뉴_그룹_목록_조회됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	public static void 메뉴_그룹_목록_포함됨(ExtractableResponse<Response> response,
		List<ExtractableResponse<Response>> expected) {
		List<Long> expectedMenuGroupIds = expected.stream()
			.map(it -> it.as(MenuGroup.class).getId())
			.collect(Collectors.toList());

		List<Long> resultMenuGroupIds = response.jsonPath().getList(".", MenuGroup.class).stream()
			.map(MenuGroup::getId)
			.collect(Collectors.toList());

		assertThat(resultMenuGroupIds).containsAll(expectedMenuGroupIds);
	}
}

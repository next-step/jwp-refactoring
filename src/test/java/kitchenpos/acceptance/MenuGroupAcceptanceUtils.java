package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.MenuGroup;

public class MenuGroupAcceptanceUtils {

	public static ExtractableResponse<Response> 메뉴_그룹_등록_요청(String name) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(createRequest(name))
			.when()
			.post("/api/menu-groups")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청() {
		return RestAssured.given().log().all()
			.when()
			.get("/api/menu-groups")
			.then().log().all()
			.extract();
	}


	public static void 메뉴_그룹_등록_됨(ExtractableResponse<Response> response, String expectedName) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.as(MenuGroup.class)).isNotNull();
	}

	public static void 메뉴_그룹_목록_조회_됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.jsonPath().getList(".", MenuGroup.class)).isNotNull();
	}


	private static MenuGroup createRequest(String name) {
		return new MenuGroup(1L, name);
	}

}

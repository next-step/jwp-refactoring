package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;

public class MenuGroupAcceptanceStaticTest {

	public static final String MENU_GROUP_PATH = "/api/menu-groups";

	public static MenuGroupResponse 메뉴_그룹_생성되어_있음(MenuGroupRequest params) {
		return 메뉴_그룹_생성_요청(params).as(MenuGroupResponse.class);
	}

	public static ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청() {
		return RestAssured.given().log().all()
			.when()
			.get(MENU_GROUP_PATH)
			.then().log().all()
			.extract();
	}

	public static void 메뉴_그룹_목록_조회됨(ExtractableResponse<Response> response, List<Long> idList) {
		List<Long> responseIdList = response.body()
			.jsonPath().getList(".", MenuGroupResponse.class)
			.stream()
			.map(MenuGroupResponse::getId)
			.collect(Collectors.toList());

		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(responseIdList).containsAnyElementsOf(idList);
	}

	public static MenuGroupRequest 메뉴_그룹_생성_요청값_생성(String name) {
		return MenuGroupRequest.from(name);
	}

	public static ExtractableResponse<Response> 메뉴_그룹_생성_요청(MenuGroupRequest params) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(params)
			.when()
			.post(MENU_GROUP_PATH)
			.then().log().all()
			.extract();
	}

	public static void 메뉴_그룹_생성됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}
}

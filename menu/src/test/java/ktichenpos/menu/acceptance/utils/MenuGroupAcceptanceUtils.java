package ktichenpos.menu.acceptance.utils;

import static ktichenpos.menu.acceptance.utils.RestAssuredUtils.*;
import static org.assertj.core.api.Assertions.*;

import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import ktichenpos.menu.menu.ui.request.MenuGroupRequest;
import ktichenpos.menu.menu.ui.response.MenuGroupResponse;

public class MenuGroupAcceptanceUtils {

	private static final String MENU_GROUP_API_URL = "/api/menu-groups";

	public static MenuGroupResponse 메뉴_그룹_등록_되어_있음(String name) {
		return 메뉴_그룹_등록_요청(name).as(MenuGroupResponse.class);
	}

	public static ExtractableResponse<Response> 메뉴_그룹_등록_요청(String name) {
		return post(MENU_GROUP_API_URL, createRequest(name)).extract();
	}

	public static ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청() {
		return get(MENU_GROUP_API_URL).extract();
	}


	public static void 메뉴_그룹_등록_됨(ExtractableResponse<Response> response, String expectedName) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.as(MenuGroupResponse.class)).isNotNull();
	}

	public static void 메뉴_그룹_목록_조회_됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.jsonPath().getList(".", MenuGroupResponse.class)).isNotNull();
	}


	private static MenuGroupRequest createRequest(String name) {
		return new MenuGroupRequest(name);
	}

}

package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class MenuGroupAcceptanceStaticTest {

	public static final String MENU_GROUP_PATH = "/api/menu-groups";

	public static MenuGroup 메뉴_그룹_생성되어_있음(MenuGroup params) {
		return 메뉴_그룹_생성_요청(params).as(MenuGroup.class);
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
			.jsonPath().getList(".", MenuGroup.class)
			.stream()
			.map(MenuGroup::getId)
			.collect(Collectors.toList());

		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(responseIdList).containsAnyElementsOf(idList);
	}

	public static MenuGroup 메뉴_그룹_생성_요청값_생성(String name) {
		MenuGroup menuGroup = new MenuGroup();
		menuGroup.setName(name);
		return menuGroup;
	}

	public static ExtractableResponse<Response> 메뉴_그룹_생성_요청(MenuGroup params) {
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

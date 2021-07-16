package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.MenuGroup;

public class MenuGroupAcceptanceTest extends AcceptanceTest {
	@DisplayName("메뉴 그룹 등록 및 조회 시나리오")
	@Test
	void createMenuAndFindMenuScenario() {
		// Scenario : 메뉴 그룹 등록 및 조회 시나리오
		// When
		ExtractableResponse<Response> menuGroupCreatedResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new MenuGroup("인기 메뉴"))
			.when().post("/api/menu-groups")
			.then().log().all()
			.extract();
		MenuGroup createdMenuGroup = menuGroupCreatedResponse.as(MenuGroup.class);
		// Then
		assertThat(menuGroupCreatedResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(createdMenuGroup.getName()).isEqualTo("인기 메뉴");
		// When
		ExtractableResponse<Response> menuGroupResponse = RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/api/menu-groups")
			.then().log().all()
			.extract();

		String menuName = menuGroupResponse.jsonPath().getList(".", MenuGroup.class).stream()
			.filter(menuGroup -> menuGroup.getId() == createdMenuGroup.getId())
			.map(MenuGroup::getName)
			.findFirst()
			.get()
			;

		assertThat(menuName).isEqualTo("인기 메뉴");
	}
}

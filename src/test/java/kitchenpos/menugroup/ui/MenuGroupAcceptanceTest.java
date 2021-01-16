package kitchenpos.menugroup.ui;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MenuGroupAcceptanceTest extends AcceptanceTest {

	@DisplayName("메뉴그룹을 추가한다.")
	@Test
	void create() {
		//given
		MenuGroupRequest request = new MenuGroupRequest("신메뉴");

		//when
		ExtractableResponse<Response> response = 메뉴그룹_등록을_요청한다(
			  request);

		//then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.header("Location")).isNotEmpty();
	}

	@DisplayName("메뉴그룹을 조회한다.")
	@Test
	void list() {
		//when
		ExtractableResponse<Response> response = RestAssured.given().log().all()
			  .contentType(MediaType.APPLICATION_JSON_VALUE)
			  .when().get("/api/menu-groups")
			  .then().log().all()
			  .extract();

		//then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.jsonPath().getList(".", MenuGroupResponse.class)).isNotNull();
	}

	public static Long 메뉴그룹이_등록되어_있다(String name) {
		ExtractableResponse<Response> response = 메뉴그룹_등록을_요청한다(new MenuGroupRequest(name));
		return response.jsonPath().getLong("id");
	}

	public static ExtractableResponse<Response> 메뉴그룹_등록을_요청한다(MenuGroupRequest request) {
		ExtractableResponse<Response> response = RestAssured.given().log().all()
			  .contentType(MediaType.APPLICATION_JSON_VALUE)
			  .body(request)
			  .when().post("/api/menu-groups")
			  .then().log().all()
			  .extract();
		return response;
	}
}

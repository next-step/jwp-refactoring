package kitchenpos.menugroup.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;

@DisplayName("메뉴그룹 기능 인수테스트")
class MenuGroupAcceptanceTest extends AcceptanceTest {

	@Test
	@DisplayName("메뉴그룹 생성 테스트")
	public void crateMenuGroupTest() {
		//given
		MenuGroupRequest menuGroupRequest = new MenuGroupRequest("후라이드+양념");

		//when
		ExtractableResponse<Response> response = 메뉴그룹_생성_요청(menuGroupRequest);

		//then
		메뉴그룹_생성_성공(response);
	}

	@Test
	@DisplayName("메뉴그룹 목록 조회 테스트")
	public void findAllMenuGroupTest() {
		//given
		//when
		ExtractableResponse<Response> response = 메뉴그룹_목록_조회_요청();

		//then
		메뉴그룹_목록_조회_성공(response);
	}

	private ExtractableResponse<Response> 메뉴그룹_목록_조회_요청() {
		return RestAssured
			.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/api/menu-groups/")
			.then().log().all()
			.extract();
	}

	private void 메뉴그룹_목록_조회_성공(ExtractableResponse<Response> response) {
		List<MenuGroupResponse> menuGroupResponses = response.jsonPath().getList(".", MenuGroupResponse.class);
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(menuGroupResponses).hasSize(4);
	}

	private ExtractableResponse<Response> 메뉴그룹_생성_요청(MenuGroupRequest menuGroupRequest) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(menuGroupRequest)
			.when().post("/api/menu-groups/")
			.then().log().all()
			.extract();
	}

	private void 메뉴그룹_생성_성공(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.header("Location")).isEqualTo("/api/menu-groups/5");
	}

}

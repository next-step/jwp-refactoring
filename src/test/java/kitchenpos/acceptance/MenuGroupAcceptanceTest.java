package kitchenpos.acceptance;

import static kitchenpos.acceptance.MenuGroupAcceptanceUtils.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("메뉴 그룹 관련 기능")
class MenuGroupAcceptanceTest extends AcceptanceTest{

	/**
	 * given 메뉴 이름을 정하고
	 * when 메뉴 그룹 등록을 요청하면
	 * then 메뉴 그룹이 등록된다
	 */
	@DisplayName("메뉴 그룹을 등록할 수 있다.")
	@Test
	void createMenuGroupTest() {
		// given
		String name = "한마리메뉴";

		// when
		ExtractableResponse<Response> 메뉴_그룹_등록_요청 = 메뉴_그룹_등록_요청(name);

		// then
		메뉴_그룹_등록_됨(메뉴_그룹_등록_요청, "한마리메뉴");
	}

	/**
	 * given 메뉴 그룹이 등록되어 있고
	 * when 메뉴 그룹 목록을 조회하면
	 * then 메뉴 그룹 목록이 조회된다
	 */
	@DisplayName("메뉴 그룹들을 조회할 수 있다.")
	@Test
	void listMenuGroupTest() {
		// given
		메뉴_그룹_등록_요청("한마리메뉴");

		// when
		ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청 = 메뉴_그룹_목록_조회_요청();

		// then
		메뉴_그룹_목록_조회_됨(메뉴_그룹_목록_조회_요청);
	}

}

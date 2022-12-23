package kitchenpos.acceptance.menugroup;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.menu.ui.dto.MenuGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 그룹 관리")
class MenuGroupAcceptanceTest extends AcceptanceTest {

	MenuGroupAcceptanceTestStep step = new MenuGroupAcceptanceTestStep();

	/**
	 * Feature: 메뉴 그룹 관리
	 * When 메뉴 그룹 등록을 요청하면
	 * Then 메뉴 그룹 등록에 성공한다
	 */
	@Test
	void 메뉴_그룹_관리() {
		MenuGroupRequest 메뉴그룹 = MenuGroupFixture.메뉴그룹();

		ExtractableResponse<Response> 등록_요청_응답 = step.등록_요청(메뉴그룹);

		step.등록됨(등록_요청_응답);
	}

}

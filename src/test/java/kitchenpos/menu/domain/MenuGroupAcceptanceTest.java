package kitchenpos.menu.domain;

import static kitchenpos.menu.domain.MenuGroupAcceptanceStaticTest.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;

@DisplayName("인수테스트 : 메뉴 그룹 관련")
class MenuGroupAcceptanceTest extends AcceptanceTest {

	private MenuGroupRequest 두마리메뉴_그룹_생성_요청값;
	private MenuGroupRequest 한마리메뉴_그룹_생성_요청값;

	@BeforeEach
	void setup() {
		두마리메뉴_그룹_생성_요청값 = 메뉴_그룹_생성_요청값_생성("두마리메뉴");
		한마리메뉴_그룹_생성_요청값 = 메뉴_그룹_생성_요청값_생성("한마리메뉴");

	}

	@Test
	void 메뉴_그룹을_생성한다() {
		// when
		ExtractableResponse<Response> response = 메뉴_그룹_생성_요청(두마리메뉴_그룹_생성_요청값);

		// then
		메뉴_그룹_생성됨(response);
	}

	@Test
	void 메뉴_그룹_목록을_조회한다() {
		// given
		MenuGroupResponse 두마리_메뉴_그룹_생성_되어있음 = 메뉴_그룹_생성되어_있음(두마리메뉴_그룹_생성_요청값);
		MenuGroupResponse 한마리_메뉴_그룹_생성_되어있음 = 메뉴_그룹_생성되어_있음(한마리메뉴_그룹_생성_요청값);

		// when
		ExtractableResponse<Response> response = 메뉴_그룹_목록_조회_요청();

		// then
		메뉴_그룹_목록_조회됨(response, Arrays.asList(두마리_메뉴_그룹_생성_되어있음.getId(), 한마리_메뉴_그룹_생성_되어있음.getId()));
	}
}

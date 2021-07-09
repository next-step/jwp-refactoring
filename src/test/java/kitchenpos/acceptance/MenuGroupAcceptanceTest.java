package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@DisplayName("메뉴그룹 관리 기능")
public class MenuGroupAcceptanceTest extends AcceptanceTest {
    @DisplayName("메뉴그룹을 관리한다.")
    @Test
    public void menuGroupManager() throws Exception {
        MenuGroupRequest 치킨 = new MenuGroupRequest("치킨메뉴");
        ExtractableResponse<Response> postResponse = 메뉴그룹_등록_요청(치킨);
        메뉴그룹_등록됨(postResponse);

        ExtractableResponse<Response> getResponse = 메뉴그룹_목록조회_요청();
        메뉴그룹_목록조회됨(getResponse);
    }

    public static MenuGroupResponse 메뉴그룹_등록_되어있음(MenuGroupRequest 메뉴그룹) {
        ExtractableResponse<Response> postResponse = 메뉴그룹_등록_요청(메뉴그룹);
        메뉴그룹_등록됨(postResponse);
        return postResponse.as(MenuGroupResponse.class);
    }

    private void 메뉴그룹_목록조회됨(ExtractableResponse<Response> getResponse) {
        assertHttpStatus(getResponse, OK);
    }

    private ExtractableResponse<Response> 메뉴그룹_목록조회_요청() {
        return get("/api/menu-groups");
    }

    private static void 메뉴그룹_등록됨(ExtractableResponse<Response> postResponse) {
        assertHttpStatus(postResponse, CREATED);
    }

    private static ExtractableResponse<Response> 메뉴그룹_등록_요청(MenuGroupRequest menuGroupRequest) {
        return post("/api/menu-groups", menuGroupRequest);
    }
}

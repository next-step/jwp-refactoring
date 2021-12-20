package kitchenpos.menugroup;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;

import static kitchenpos.AcceptanceTest.*;

public class MenuGroupSteps {

    private static final String MENU_GROUP_URI = "/api/menu-groups";

    public static MenuGroupResponse 메뉴_그룹_등록되어_있음(MenuGroupRequest menuGroupRequest) {
        return 메뉴_그룹_등록_요청(menuGroupRequest).as(MenuGroupResponse.class);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_등록_요청(MenuGroupRequest menuGroupRequest) {
        return post(MENU_GROUP_URI, menuGroupRequest);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청() {
        return get(MENU_GROUP_URI);
    }
}

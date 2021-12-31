package kitchenpos.fixture;


import static kitchenpos.common.AcceptanceFixture.*;

import io.restassured.response.*;
import kitchenpos.common.*;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.*;

public class MenuGroupAcceptanceFixture extends AcceptanceTest {

    public static MenuGroup 한마리_세트 = menuGroup("한마리 세트");
    public static MenuGroupRequest 한마리_세트_요청 = MenuGroupRequest.from(한마리_세트.getName());

    public static MenuGroup menuGroup(String name) {
        return new MenuGroup(name);
    }

    public static MenuGroupResponse 한마리_세트_생성됨() {
        return 메뉴_그룹_생성_요청(한마리_세트_요청).as(MenuGroupResponse.class);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_생성_요청(MenuGroupRequest menuGroupRequest) {
        return post("/api/menu-groups", menuGroupRequest);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_조회_요청() {
        return get("/api/menu-groups");
    }

}

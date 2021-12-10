package kitchenpos.menu.domain.fixture;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.domain.menugroup.MenuGroup;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;

import static kitchenpos.utils.AcceptanceFixture.get;
import static kitchenpos.utils.AcceptanceFixture.post;

public class MenuGroupDomainFixture {

    public static MenuGroup 일인_세트 = menuGroup("일인 세트");
    public static MenuGroupRequest 일인_세트_요청 = MenuGroupRequest.from(일인_세트.getName());

    public static MenuGroup 패밀리_세트 = menuGroup("패밀리 세트");
    public static MenuGroupRequest 패밀리_세트_요청 = MenuGroupRequest.from(패밀리_세트.getName());

    public static MenuGroup menuGroup(String name) {
        return new MenuGroup(name);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_생성_요청(MenuGroupRequest menuGroupRequest) {
        return post("/api/menu-groups", menuGroupRequest);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_조회_요청() {
        return get("/api/menu-groups");
    }

}

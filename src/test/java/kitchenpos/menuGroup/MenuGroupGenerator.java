package kitchenpos.menuGroup;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.Acceptance.utils.RestAssuredRequest;
import kitchenpos.menuGroup.domain.MenuGroup;
import kitchenpos.menuGroup.dto.MenuGroupCreateRequest;

import java.util.Collections;

public class MenuGroupGenerator {
    private static final String PATH = "/api/menu-groups";

    public static MenuGroup 메뉴_그룹_생성(String name) {
        return new MenuGroup(name);
    }

    public static MenuGroupCreateRequest 메뉴_그룹_생성_요청(String name) {
        return new MenuGroupCreateRequest(name);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_생성_API_호출(String name) {
        return RestAssuredRequest.postRequest(PATH, Collections.emptyMap(), 메뉴_그룹_생성_요청(name));
    }

    public static ExtractableResponse<Response> 메뉴_그룹_목록_조회_API_호출() {
        return RestAssuredRequest.getRequest(PATH, Collections.emptyMap());
    }

    public static ExtractableResponse<Response> 메뉴_그룹_조회_API_요청(long id) {
        return RestAssuredRequest.getRequest(PATH + "/{id}", Collections.emptyMap(), id);
    }
}

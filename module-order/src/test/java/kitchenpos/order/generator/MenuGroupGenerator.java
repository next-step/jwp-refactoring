package kitchenpos.order.generator;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
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
}

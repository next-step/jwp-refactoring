package kitchenpos.menu.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.TestFixture;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;

public class MenuGroupTestFixture extends TestFixture {

    public static final String MENU_GROUP_BASE_URI = "/api/menu-groups";

    public static ExtractableResponse<Response> 메뉴_그룹_생성_요청함(String name) {
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest(name);
        return post(MENU_GROUP_BASE_URI, menuGroupRequest);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_조회_요청함() {
        return get(MENU_GROUP_BASE_URI);
    }

    public static void 메뉴_그룹_조회_요청_응답됨(ExtractableResponse<Response> response) {
        ok(response);
    }

    public static void 메뉴_그룹_생성됨(ExtractableResponse<Response> response) {
        MenuGroupResponse menuGroupResponse = response.as(MenuGroupResponse.class);
        created(response);
        assertAll(
                ()-> assertThat(menuGroupResponse.getId()).isNotNull(),
                ()-> assertThat(menuGroupResponse.getName()).isNotNull()
        );
    }

    public static void 메뉴_그룹_조회_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> menuGroupResponses) {
        List<Long> actualIds = response.jsonPath()
                .getList(".", MenuGroup.class)
                .stream()
                .map(MenuGroup::getId)
                .collect(Collectors.toList());

        List<Long> expectIds = menuGroupResponses.stream()
                .map(r -> r.as(MenuGroup.class))
                .collect(Collectors.toList())
                .stream()
                .map(MenuGroup::getId)
                .collect(Collectors.toList());

        assertThat(actualIds).containsAll(expectIds);
    }

}

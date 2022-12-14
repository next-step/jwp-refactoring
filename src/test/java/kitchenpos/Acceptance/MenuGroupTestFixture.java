package kitchenpos.Acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.TestFixture;
import kitchenpos.domain.MenuGroup;
import org.assertj.core.api.Assertions;

public class MenuGroupTestFixture extends TestFixture {

    public static final String MENU_GROUP_BASE_URI = "/api/menu-groups";

    public static ExtractableResponse<Response> 메뉴_그룹_생성_요청함(MenuGroup menuGroup) {
        return post(MENU_GROUP_BASE_URI, menuGroup);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_조회_요청함() {
        return get(MENU_GROUP_BASE_URI);
    }

    public static void 메뉴_그룹_조회_요청_응답됨(ExtractableResponse<Response> response) {
        ok(response);
    }

    public static void 메뉴_그룹_생성됨(ExtractableResponse<Response> response) {
        created(response);
    }

    public static void 메뉴_그룹_조회_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> productResponses) {
        List<Long> actualIds = response.jsonPath()
                .getList(".", MenuGroup.class)
                .stream()
                .map(MenuGroup::getId)
                .collect(Collectors.toList());

        List<Long> expectIds = productResponses.stream()
                .map(r -> r.as(MenuGroup.class))
                .collect(Collectors.toList())
                .stream()
                .map(MenuGroup::getId)
                .collect(Collectors.toList());

        Assertions.assertThat(actualIds).containsAll(expectIds);
    }

}

package kitchenpos.acceptance;

import static kitchenpos.acceptance.AcceptanceTestHelper.assertCreatedStatus;
import static kitchenpos.acceptance.AcceptanceTestHelper.assertInternalServerErrorStatus;
import static kitchenpos.acceptance.AcceptanceTestHelper.assertOkStatus;
import static kitchenpos.acceptance.MenuGroupAcceptanceTestHelper.createMenuGroup;
import static kitchenpos.acceptance.MenuGroupAcceptanceTestHelper.getMenuGroups;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;

class MenuGroupAcceptanceTest extends AcceptanceTest {

    @Test
    void 생성시_이름이_존재하지않는경우_생성실패() {
        assertInternalServerErrorStatus(createMenuGroup(null));
    }

    @Test
    void 생성시_이름이_존재하는경우_생성성공() {
        assertCreatedStatus(createMenuGroup("lunch-menu-group"));
    }

    @Test
    void 조회시_존재하는목록반환() {
        MenuGroup created = createMenuGroup("lunch-menu-group").as(MenuGroup.class);
        ExtractableResponse<Response> response = getMenuGroups();
        assertAll(
            () -> assertOkStatus(response),
            () -> assertThat(mapToMenuGroupIds(response)).contains(created.getId())
        );
    }

    private List<Long> mapToMenuGroupIds(ExtractableResponse<Response> response) {
        return response.jsonPath()
            .getList(".", MenuGroup.class)
            .stream()
            .map(MenuGroup::getId)
            .collect(Collectors.toList());
    }

}

package kitchenpos.acceptance;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menugroup.dto.MenuGroupCreateRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.menugroup.fixture.MenuGroupFixture;
import kitchenpos.rest.MenuGroupRestAssured;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertAll;

public class MenuGroupAcceptanceTest extends BaseAcceptanceTest {

    @Test
    @DisplayName("신규 메뉴 그룹 정보가 주어진 경우 메뉴 그룹 등록 요청시 요청에 성공한다")
    void createMenuGroupThenReturnMenuGroupResponse() {
        // given
        MenuGroupCreateRequest request = new MenuGroupCreateRequest("한마리메뉴");

        // when
        ExtractableResponse<Response> response = MenuGroupRestAssured.메뉴_그룹_등록_요청(request);

        // then
        MenuGroupResponse menuGroupResponse = response.as(MenuGroupResponse.class);
        assertAll(
                () -> Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> Assertions.assertThat(menuGroupResponse.getId()).isNotNull()
        );
    }

    @Test
    @DisplayName("메뉴 그룹 목록 조회 요청시 요청에 성공한다")
    void findAllMenuGroupsThenReturnMenuGroupResponses() {
        // given
        MenuGroupResponse groupResponse = MenuGroupRestAssured.메뉴_그룹_등록됨(MenuGroupFixture.한마리메뉴);

        // when
        ExtractableResponse<Response> response = MenuGroupRestAssured.메뉴_그룹_목록_조회_요청();

        // then
        JsonPath jsonPath = response.jsonPath();
        assertAll(
                () -> Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> Assertions.assertThat(jsonPath.getList("name", String.class)).containsExactly(groupResponse.getName())
        );
    }
}

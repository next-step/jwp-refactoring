package kitchenpos.acceptance;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.MenuGroup;
import kitchenpos.rest.MenuGroupRestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class MenuGroupAcceptanceTest extends BaseAcceptanceTest {

    @Test
    void 신규_메뉴_그룹_정보가_주어진_경우_메뉴_그룹_등록_요청시_요청에_성공한다() {
        // given
        String 메뉴명 = "한마리메뉴";

        // when
        ExtractableResponse<Response> response = MenuGroupRestAssured.메뉴_그룹_등록_요청(메뉴명);

        // then
        메뉴_그룹_등록됨(response, 메뉴명);
    }

    @Test
    void 메뉴_그룹_목록_조회_요청시_요청에_성공한다() {
        // given
        String 메뉴명 = "한마리메뉴";
        MenuGroupRestAssured.메뉴_그룹_등록_요청(메뉴명);

        // when
        ExtractableResponse<Response> response = MenuGroupRestAssured.메뉴_그룹_목록_조회_요청();

        // then
        메뉴_그룹_목록_조회됨(response, 메뉴명);
    }

    private void 메뉴_그룹_등록됨(ExtractableResponse<Response> response, String menuGroupName) {
        MenuGroup menuGroup = response.as(MenuGroup.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(menuGroup.getName()).isEqualTo(menuGroupName)
        );
    }

    private void 메뉴_그룹_목록_조회됨(ExtractableResponse<Response> response, String... menuGroupNames) {
        JsonPath jsonPath = response.jsonPath();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(jsonPath.getList("name", String.class)).containsExactly(menuGroupNames)
        );
    }
}

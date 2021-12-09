package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.*;

@DisplayName("메뉴 그룹 인수 테스트")
public class MenuGroupAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("메뉴 그룹을 등록한다.")
    void create() {
        // given
        MenuGroup menuGroup = new MenuGroup("세마리메뉴");

        // when
        ExtractableResponse<Response> response = 메뉴_그룹_등록_요청(menuGroup);

        // then
        메뉴_그룹_등록됨(response);
    }

    @Test
    @DisplayName("메뉴 그룹의 목록을 조회한다.")
    void list() {
        // when
        ExtractableResponse<Response> response = 메뉴_그룹_목록_조회_요청();

        // then
        메뉴_그룹_목록_조회됨(response);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_등록_요청(MenuGroup menuGroup) {
        return post("/api/menu-groups", menuGroup);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청() {
        return get("/api/menu-groups");
    }

    private void 메뉴_그룹_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private void 메뉴_그룹_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList(".", MenuGroup.class).size()).isPositive();
    }
}

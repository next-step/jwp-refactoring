package kitchenpos.menu;

import static kitchenpos.menu.MenuAcceptanceAPI.메뉴_그룹_생성_요청;
import static kitchenpos.menu.MenuAcceptanceAPI.메뉴_그룹_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class MenuGroupAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Test
    @DisplayName("메뉴_그룹을_생성한다")
    void 메뉴_그룹을_생성한다() {
        // when
        ExtractableResponse<Response> response = 메뉴_그룹_생성_요청("추천메뉴");

        // then
        메뉴_그룹_생성됨(response);
    }

    @Test
    @DisplayName("메뉴_그룹을_조회한다")
    void 메뉴_그룹을_조회한다() {
        // given
        메뉴_그룹_생성_요청("추천메뉴");

        // when
        ExtractableResponse<Response> response = 메뉴_그룹_조회_요청();

        // then
        assertThat(response.jsonPath().getList("name")).hasSize(1);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_생성되어_있음(String name) {
        return 메뉴_그룹_생성_요청(name);
    }

    public static void 메뉴_그룹_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }
}

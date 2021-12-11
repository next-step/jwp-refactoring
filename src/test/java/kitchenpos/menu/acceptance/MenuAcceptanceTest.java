package kitchenpos.menu.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Collections;

import static org.assertj.core.api.Assertions.*;

@DisplayName("메뉴 인수 테스트")
public class MenuAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("메뉴를 등록한다.")
    void create() {
        // given
        MenuProductRequest menuProductRequest = new MenuProductRequest(1L, 1);
        MenuRequest menuRequest = new MenuRequest(
                "후라이드치킨", new BigDecimal(16_000), 2L, Collections.singletonList(menuProductRequest));

        // when
        ExtractableResponse<Response> response = 메뉴_등록_요청(menuRequest);

        // then
        메뉴_등록됨(response);
    }

    @Test
    @DisplayName("메뉴의 목록을 조회한다.")
    void list() {
        // when
        ExtractableResponse<Response> response = 메뉴_목록_조회_요청();

        // then
        메뉴_목록_조회됨(response);
    }

    public static ExtractableResponse<Response> 메뉴_등록_요청(MenuRequest menuRequest) {
        return post("/api/menus", menuRequest);
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return get("/api/menus");
    }

    private void 메뉴_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private void 메뉴_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList(".", Menu.class).size()).isPositive();
    }
}

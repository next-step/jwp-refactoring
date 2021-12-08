package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
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
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(1);

        Menu menu = new Menu();
        menu.setName("후라이드치킨");
        menu.setPrice(new BigDecimal(16_000));
        menu.setMenuGroupId(2L);
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        // when
        ExtractableResponse<Response> response = 메뉴_등록_요청(menu);

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

    public static ExtractableResponse<Response> 메뉴_등록_요청(Menu menu) {
        return post("/api/menus", menu);
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

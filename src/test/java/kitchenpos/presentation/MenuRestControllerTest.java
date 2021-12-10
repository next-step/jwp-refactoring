package kitchenpos.presentation;

import java.math.BigDecimal;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;
import kitchenpos.testassistance.config.TestConfig;

@DisplayName("메뉴 API기능에 관한")
public class MenuRestControllerTest extends TestConfig {
    @DisplayName("메뉴가 저장된다.")
    @Test
    void save_menu() {
        // given
        Menu menu = 신메뉴();

        // when
        ExtractableResponse<Response> response = 메뉴_저장요청(menu);

        // then
        매뉴_저장됨(response);
    }

    @DisplayName("메뉴가 조회된다.")
    @Test
    void search_menu() {
        // when
        ExtractableResponse<Response> response = 메뉴_조회요청();

        // then
        메뉴_조회됨(response);
    }

    private void 메뉴_조회됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 메뉴_조회요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menus")
                .then().log().all()
                .extract();
    }

    private void 매뉴_저장됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 메뉴_저장요청(Menu menu) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menu)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }

    public static Menu 신메뉴() {
        MenuProduct menuProduct = MenuProduct.of(null, null, 2);
        Menu menu = Menu.of("후라이드+후라이드", Price.of(19_000), null, List.of(menuProduct));
        return menu;
    }
}

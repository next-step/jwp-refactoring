package kitchenpos.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.MenuProduct;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DisplayName("메뉴 관련 기능")
public class MenuAcceptanceTest extends AcceptanceTest {
    @DisplayName("메뉴 등록")
    @Test
    void create() {
        // given
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(new MenuProduct(1L, 1L, 1L));
        menuProducts.add(new MenuProduct(2L, 2L, 1L));
        menuProducts.add(new MenuProduct(3L, 3L, 1L));

        Map<String, Object> params = new HashMap<>();
        params.put("name", "후라이드치킨");
        params.put("price", 16000);
        params.put("menuGroupId", 2);
        params.put("menuProducts", menuProducts);

        // when
        ExtractableResponse<Response> response = 메뉴_등록_요청(params);

        // then
        메뉴_둥록됨(response);
    }

    @DisplayName("메뉴 목록")
    @Test
    void list() {
        // when
        ExtractableResponse<Response> response = 메뉴_목록_조회_요청();

        // then
        메뉴_목록_조회됨(response, HttpStatus.OK);
    }

    private ExtractableResponse<Response> 메뉴_등록_요청(Map<String, Object> params) {
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/menus")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("/api/menus")
                .then().log().all().extract();
        return response;
    }

    private void 메뉴_목록_조회됨(ExtractableResponse<Response> response, HttpStatus ok) {
        Assertions.assertThat(response.statusCode()).isEqualTo(ok.value());
    }



    private void 메뉴_둥록됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }


}

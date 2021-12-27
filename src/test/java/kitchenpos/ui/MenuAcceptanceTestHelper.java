package kitchenpos.ui;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.dto.MenuResponse;

public class MenuAcceptanceTestHelper {
    private MenuAcceptanceTestHelper() {
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(Map<String, Object> params) {
        return RestAssured
            .given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/api/menus")
            .then().log().all().extract();
    }

    public static void 메뉴_생성됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 메뉴_생성_실패(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static ExtractableResponse<Response> 메뉴_조회_요청() {
        return RestAssured
            .given().log().all()
            .when().get("/api/menus")
            .then().log().all().extract();
    }

    public static void 메뉴_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 메뉴_갯수_예상과_일치(ExtractableResponse<Response> response, int expected) {
        List<MenuResponse> actual = response.jsonPath().getList(".", MenuResponse.class);

        assertThat(actual).hasSize(expected);
    }
}

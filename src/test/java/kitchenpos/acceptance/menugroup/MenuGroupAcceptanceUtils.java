package kitchenpos.acceptance.menugroup;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class MenuGroupAcceptanceUtils {
    private MenuGroupAcceptanceUtils() {}

    public static ExtractableResponse<Response> 메뉴_그룹_등록_요청(String name) {
        Map<String, String> body = new HashMap<>();
        body.put("name", name);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .when().post("/api/menu-groups")
            .then().log().all()
            .extract();
    }

    public static void 메뉴_그룹_등록_완료(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .when().get("/api/menu-groups")
            .then().log().all()
            .extract();
    }

    public static void 메뉴_그룹_목록_조회_완료(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static MenuGroupId 메뉴_그룹_ID_추출(ExtractableResponse<Response> response) {
        return new MenuGroupId(response.jsonPath().get("id"));
    }
}

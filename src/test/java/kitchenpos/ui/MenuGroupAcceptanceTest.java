package kitchenpos.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

@DisplayName("메뉴 그룹 관련 기능")
class MenuGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("메뉴 그룹 등록")
    @Test
    void create() {
        // given
        Map<String, Object> params = new HashMap<>();
        params.put("name", "안주메뉴");

        // when
        ExtractableResponse<Response> response = 메뉴_그룹_등록_요청(params);

        // then
        메뉴_그룹_둥록됨(response);
    }

    @DisplayName("메뉴 그룹 목록")
    @Test
    void list() {
        // when
        ExtractableResponse<Response> response = 메뉴_그룹_목록_조회_요청();

        // then
        메뉴_그룹_목록_조회됨(response, HttpStatus.OK);
    }

    private ExtractableResponse<Response> 메뉴_그룹_등록_요청(Map<String, Object> params) {
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/menu-groups")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청() {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("/api/menu-groups")
                .then().log().all().extract();
        return response;
    }

    private void 메뉴_그룹_목록_조회됨(ExtractableResponse<Response> response, HttpStatus ok) {
        Assertions.assertThat(response.statusCode()).isEqualTo(ok.value());
    }



    private void 메뉴_그룹_둥록됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

}
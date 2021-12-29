package kitchenpos.menu;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴그룹 관련 기능")
public class MenuGroupAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("메뉴그룹을 등록한다.")
    void postMenuGroup() {
        // when
        ExtractableResponse<Response> initResponse = 메뉴_그룹_조회_요청();

        // then
        assertThat(initResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(initResponse.jsonPath().getList(".")).hasSize(0);

        // when
        ExtractableResponse<Response> response = 메뉴_그룹_등록_요청("두마리메뉴");

        // then
        메뉴_그룹_등록됨(response);

        // when
        ExtractableResponse<Response> getResponse = 메뉴_그룹_조회_요청();

        // then
        assertThat(getResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(getResponse.jsonPath().getList(".")).hasSize(1);
    }

    private ExtractableResponse<Response> 메뉴_그룹_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menu-groups")
                .then().log().all().extract();
    }

    private void 메뉴_그룹_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 메뉴_그룹_등록_요청(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/menu-groups")
                .then().log().all().extract();
    }

}

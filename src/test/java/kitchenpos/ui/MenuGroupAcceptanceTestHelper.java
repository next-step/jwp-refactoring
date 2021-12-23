package kitchenpos.ui;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.MenuGroup;

public class MenuGroupAcceptanceTestHelper {
    private MenuGroupAcceptanceTestHelper() {
    }

    public static ExtractableResponse<Response> 메뉴_그룹_생성_요청(Map<String, String> params) {
        return RestAssured
            .given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/api/menu-groups")
            .then().log().all().extract();
    }

    public static void 메뉴_그룹_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 메뉴_그룹_예상괴_일치(ExtractableResponse<Response> response, String name) {
        MenuGroup menuGroup = response.as(MenuGroup.class);
        assertThat(menuGroup.getId()).isNotNull();
        assertThat(menuGroup.getName()).isEqualTo(name);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_조회_요청() {
        return RestAssured
            .given().log().all()
            .when().get("/api/menu-groups")
            .then().log().all().extract();
    }

    public static void 메뉴_그룹_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 메뉴_그룹_갯수_예상과_일치(ExtractableResponse<Response> response, int expected) {
        List<MenuGroup> list = response.jsonPath().getList(".", MenuGroup.class);
        assertThat(list).hasSize(expected);
    }
}

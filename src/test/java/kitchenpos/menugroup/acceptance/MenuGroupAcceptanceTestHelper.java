package kitchenpos.menugroup.acceptance;

import static org.assertj.core.api.Assertions.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.MenuGroup;

public class MenuGroupAcceptanceTestHelper {
    public static ExtractableResponse<Response> 메뉴그룹_등록되어_있음(String name) {
        return 메뉴그룹_등록_요청(name);
    }

    public static ExtractableResponse<Response> 메뉴그룹_등록_요청(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(menuGroup)
            .when().post("/api/menu-groups")
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 메뉴_그룹_목록_요청() {
        return RestAssured
            .given().log().all()
            .when().get("/api/menu-groups")
            .then().log().all().extract();
    }

    public static void 메뉴그룹_조회됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 메뉴그룹_등록됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}

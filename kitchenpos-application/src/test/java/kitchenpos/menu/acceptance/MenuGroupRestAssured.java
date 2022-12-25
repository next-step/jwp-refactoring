package kitchenpos.menu.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.dto.MenuGroupRequest;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuGroupRestAssured {
    public static ExtractableResponse<Response> 메뉴그룹_생성_요청(String name) {
        MenuGroupRequest request = new MenuGroupRequest(name);

        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴그룹_조회_요청() {
        return RestAssured.given().log().all()
                .when().get("/api/menu-groups")
                .then().log().all()
                .extract();
    }
}

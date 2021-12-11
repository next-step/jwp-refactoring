package kitchenpos.menu.step;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.menu.ui.request.MenuGroupRequest;
import kitchenpos.menu.ui.response.MenuGroupResponse;
import org.springframework.http.HttpStatus;

public class MenuGroupAcceptanceStep {

    public static MenuGroupResponse 메뉴_그룹_등록_되어_있음(String name) {
        return 메뉴_그룹_등록_요청(name).as(MenuGroupResponse.class);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_등록_요청(String name) {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(new MenuGroupRequest(name))
            .when()
            .post("/api/menu-groups")
            .then().log().all()
            .extract();
    }

    public static void 메뉴_그룹_등록_됨(ExtractableResponse<Response> response, String expectedName) {
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
            () -> assertThat(response.as(MenuGroupResponse.class))
                .extracting(MenuGroupResponse::getName)
                .isEqualTo(expectedName)
        );
    }

    public static ExtractableResponse<Response> 메뉴_그룹_목록_조회_요청() {
        return RestAssured.given().log().all()
            .when()
            .get("/api/menu-groups")
            .then().log().all()
            .extract();
    }

    public static void 메뉴_그룹_목록_조회_됨(ExtractableResponse<Response> response,
        MenuGroupResponse expectedMenuGroup) {
        List<MenuGroupResponse> groups = response.as(new TypeRef<List<MenuGroupResponse>>() {
        });
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(groups)
                .first()
                .extracting(MenuGroupResponse::getId)
                .isEqualTo(expectedMenuGroup.getId())
        );
    }
}

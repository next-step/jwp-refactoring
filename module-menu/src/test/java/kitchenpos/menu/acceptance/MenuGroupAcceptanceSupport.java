package kitchenpos.menu.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.menu.dto.request.MenuGroupRequest;
import kitchenpos.menu.dto.response.MenuGroupResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MenuGroupAcceptanceSupport {

    public static ExtractableResponse<Response> 메뉴_그룹_등록요청(MenuGroupRequest menuGroupRequest) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(menuGroupRequest)
            .when().post("/api/menu-groups")
            .then().log().all()
            .extract();
    }

    public static void 메뉴_그룹_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static ExtractableResponse<Response> 메뉴_그룹목록_조회요청() {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api/menu-groups")
            .then().log().all().
            extract();
    }

    public static void 메뉴_그룹목록_조회됨(ExtractableResponse<Response> response, int size) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<MenuGroupResponse> result = response.jsonPath().getList(".", MenuGroupResponse.class);
        assertThat(result).isNotNull();
        assertThat(result).hasSize(size);
    }
}

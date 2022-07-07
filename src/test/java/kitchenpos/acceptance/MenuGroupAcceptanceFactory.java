package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class MenuGroupAcceptanceFactory {

    public static ExtractableResponse<Response> 메뉴그룹_등록_요청(String name) {
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest(name);
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuGroupRequest)
                .when()
                .post("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴그룹_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static void 메뉴그룹_등록됨(ExtractableResponse<Response> 메뉴그룹_등록_결과) {
        assertThat(메뉴그룹_등록_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 메뉴그룹_조회됨(ExtractableResponse<Response> 메뉴그룹_조회_결과, List<MenuGroupResponse> 예상조회결과) {
        List<MenuGroupResponse> productList = 메뉴그룹_조회_결과.as(List.class);
        assertAll(
                () -> assertThat(메뉴그룹_조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(productList.size()).isEqualTo(예상조회결과.size())
        );
    }

}

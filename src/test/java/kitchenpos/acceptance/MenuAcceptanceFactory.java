package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuAcceptanceFactory {


    public static ExtractableResponse<Response> 메뉴_등록_요청(String 이름, Integer 가격, Long 메뉴그룹Id, List<MenuProductRequest> 메뉴상품들) {
        MenuRequest menuRequest = new MenuRequest(이름, 가격, 메뉴그룹Id, 메뉴상품들);

        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuRequest)
                .when()
                .post("/api/menus")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/menus")
                .then().log().all()
                .extract();
    }

    public static void 메뉴_등록_성공(ExtractableResponse<Response> 메뉴등록_결과) {
        assertThat(메뉴등록_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 메뉴_등록_실패(ExtractableResponse<Response> 메뉴등록_결과) {
        assertThat(메뉴등록_결과.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 메뉴_조회_성공(ExtractableResponse<Response> 메뉴조회_결과) {
        assertThat(메뉴조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}

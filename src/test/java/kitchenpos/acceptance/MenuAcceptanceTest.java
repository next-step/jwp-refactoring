package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MenuAcceptanceTest extends BaseAcceptanceTest{

    @DisplayName("메뉴를 관리한다")
    @Test
    public void manageMenu() {
        //메뉴 생성
        //given
        MenuRequest 메뉴 = MenuRequest.of("메뉴", 1000, 1L, createMenuProducts());
        //when
        ExtractableResponse<Response> 메뉴_그룹_생성_요청 = 메뉴_생성_요청(메뉴);
        //then
        응답코드_확인(메뉴_그룹_생성_요청, HttpStatus.CREATED);

        //메뉴 그룹 조회
        //when
        ExtractableResponse<Response> 메뉴_목록_조회_요청 = 메뉴_목록_조회_요청();
        //then
        응답코드_확인(메뉴_목록_조회_요청, HttpStatus.OK);
        메뉴_조회됨(메뉴_목록_조회_요청, 메뉴.getName());
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(MenuRequest menu) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(menu)
            .when().post("/api/menus")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api/menus")
            .then().log().all()
            .extract();
    }

    public static void 응답코드_확인(final ExtractableResponse<Response> response, HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }

    public static void 메뉴_조회됨(final ExtractableResponse<Response> response, String menuName) {
        assertThat(response.jsonPath().getList(".", MenuResponse.class).stream().anyMatch(searchMenu -> searchMenu.getName().equals(menuName))).isTrue();
    }

    //16000원
    private static List<MenuProductRequest> createMenuProducts() {
        return Arrays.asList(new MenuProductRequest( 1L, 1000l));
    }
}

package kitchenpos.menu.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.utils.TestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 관련 기능")
class MenuAcceptanceTest extends AcceptanceTest {
//    @Test
//    void createMenu() {
//        List<MenuProductRequest> menuProducts = Collections.singletonList(new MenuProductRequest(등록된_product, 1));
//        MenuRequest menuRequest = new MenuRequest(등록되어_있지_않은_menu_id, "후라이드치킨", BigDecimal.valueOf(16000),
//                등록된_menuGroup, menuProducts);
//
//        ExtractableResponse<Response> response = 메뉴_생성_요청(menuRequest);
//
//        메뉴_생성됨(response);
//    }
//
//    @DisplayName("메뉴 그룹이 등록되어 있지 않을 경우 생성하지 못한다.")
//    @Test
//    void createMenuException1() {
//        List<MenuProductRequest> menuProducts = Collections.singletonList(new MenuProductRequest(등록된_product, 1));
//        MenuRequest menuRequest = new MenuRequest(등록되어_있지_않은_menu_id, "후라이드치킨", BigDecimal.valueOf(16000),
//                등록되어_있지_않은_menuGroup, menuProducts);
//
//        ExtractableResponse<Response> response = 메뉴_생성_요청(menuRequest);
//
//        메뉴_생성_실패(response);
//    }
//
//    @DisplayName("등록되어 있지 않은 상품으로 만들어진 메뉴 상품이 있으면 생성할 수 없다.")
//    @Test
//    void createMenuException2() {
//        List<MenuProductRequest> menuProducts = Collections.singletonList(new MenuProductRequest(등록되어_있지_않은_product, 1));
//        MenuRequest menuRequest = new MenuRequest(등록되어_있지_않은_menu_id, "후라이드치킨", BigDecimal.valueOf(16000),
//                등록된_menuGroup, menuProducts);
//
//        ExtractableResponse<Response> response = 메뉴_생성_요청(menuRequest);
//
//        메뉴_생성_실패(response);
//    }
//
//    @DisplayName("메뉴 목록 조회")
//    @Test
//    void getMenuList() {
//        ExtractableResponse<Response> response = 메뉴_목록_조회_요청();
//
//        메뉴_목록_조회됨(response);
//        메뉴_목록_포함됨(response, Arrays.asList("후라이드치킨", "양념치킨", "반반치킨", "통구이", "간장치킨", "순살치킨"));
//    }

    private static ExtractableResponse<Response> 메뉴_생성_요청(MenuRequest menuRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuRequest)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/menus")
                .then().log().all()
                .extract();
    }

    private static void 메뉴_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private static void 메뉴_생성_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private static void 메뉴_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 메뉴_목록_포함됨(ExtractableResponse<Response> response, List<String> resultNames) {
        List<String> menuGroupNames = response.jsonPath().getList(".", MenuResponse.class).stream()
                .map(MenuResponse::getName)
                .collect(Collectors.toList());

        assertThat(menuGroupNames).containsAll(resultNames);
    }
}

package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 관리")
public class MenuAcceptanceTest extends AcceptanceTest {
    @DisplayName("메뉴를 관리한다")
    @Test
    void manage() {
        메뉴_생성();
        메뉴_조회();
    }

    private void 메뉴_생성() {
        MenuGroupResponse menuGroup = MenuGroupAcceptanceTest.생성_요청(MenuGroupAcceptanceTest.createRequest())
                .as(MenuGroupResponse.class);
        ProductResponse product = ProductAcceptanceTest.생성_요청(ProductAcceptanceTest.createRequest())
                .as(ProductResponse.class);

        MenuRequest request = createRequest(menuGroup, product);
        ExtractableResponse<Response> createdResponse = 생성_요청(request);

        생성됨(createdResponse, request);
    }

    private void 메뉴_조회() {
        ExtractableResponse<Response> selectedResponse = 조회_요청();

        조회됨(selectedResponse);
    }

    public static MenuRequest createRequest(MenuGroupResponse menuGroup, ProductResponse product) {
        MenuProductRequest menuProductRequest = MenuProductRequest.builder()
                .productId(product.getId())
                .quantity(2)
                .build();

        List<MenuProductRequest> menuProductRequests = Collections.singletonList(menuProductRequest);

        return MenuRequest.builder()
                .name("후라이드+후라이드")
                .price(19_000)
                .menuGroupId(menuGroup.getId())
                .menuProducts(menuProductRequests)
                .build();
    }

    public static ExtractableResponse<Response> 생성_요청(MenuRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }

    public static void 생성됨(ExtractableResponse<Response> response, MenuRequest request) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        MenuResponse menu = response.as(MenuResponse.class);
        assertThat(menu.getName()).isEqualTo(request.getName());
        assertThat(menu.getPrice()).isEqualTo(request.getPrice());
        assertThat(menu.getMenuGroupId()).isEqualTo(request.getMenuGroupId());
    }

    public static ExtractableResponse<Response> 조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menus")
                .then().log().all()
                .extract();
    }

    public static void 조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<MenuResponse> menu = Arrays.asList(response.as(MenuResponse[].class));
        assertThat(menu.size()).isEqualTo(1);
    }
}

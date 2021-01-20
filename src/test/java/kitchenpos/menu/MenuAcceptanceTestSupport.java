package kitchenpos.menu;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.HttpStatusAssertion;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuAcceptanceTestSupport extends AcceptanceTest {
    public static ExtractableResponse<Response> 메뉴_등록_되어있음(MenuGroupResponse menuGroup, List<ProductResponse> products) {
        MenuRequest params = new MenuRequest("메뉴이름", BigDecimal.valueOf(sumOfProductPrice(products)),
                menuGroup.getId(), convertParamsOfProducts(products));

        ExtractableResponse<Response> response = 메뉴_등록_요청(params);
        메뉴_생성_완료(response);
        return response;
    }

    public static ExtractableResponse<Response> 메뉴_등록_요청(MenuRequest params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/menus")
                .then().log().all()
                .extract();
    }

    public static void 메뉴_생성_완료(ExtractableResponse<Response> response) {
        HttpStatusAssertion.CREATED(response);
    }

    public static void 메뉴_목록_응답(ExtractableResponse<Response> response) {
        HttpStatusAssertion.OK(response);
    }

    private static int sumOfProductPrice(List<ProductResponse> products) {
        return products.stream()
                .map(ProductResponse::getPrice)
                .mapToInt(BigDecimal::intValue)
                .sum();
    }

    private static List<MenuProductRequest> convertParamsOfProducts(List<ProductResponse> products) {
        return products.stream()
                .map(product -> new MenuProductRequest(product.getId(), 1L))
                .collect(Collectors.toList());
    }
}

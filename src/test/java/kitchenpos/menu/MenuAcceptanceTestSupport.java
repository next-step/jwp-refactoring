package kitchenpos.menu;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.HttpStatusAssertion;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MenuAcceptanceTestSupport extends AcceptanceTest {
    public static ExtractableResponse<Response> 메뉴_등록_되어있음(MenuGroup menuGroup, List<Product> products) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "메뉴이름");
        params.put("price", sumOfProductPrice(products));
        params.put("menuGroupId", menuGroup.getId());
        params.put("menuProducts", convertParamsOfProducts(products));
        ExtractableResponse<Response> response = 메뉴_등록_요청(params);
        메뉴_생성_완료(response);
        return response;
    }

    public static ExtractableResponse<Response> 메뉴_등록_요청(Map<String, Object> params) {
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

    private static int sumOfProductPrice(List<Product> products) {
        return products.stream()
                .map(Product::getPrice)
                .mapToInt(BigDecimal::intValue)
                .sum();
    }

    private static List<Map<String, Object>> convertParamsOfProducts(List<Product> products) {
        return products.stream()
                .map(product -> {
                    Map<String, Object> request = new HashMap<>();
                    request.put("productId", product.getId());
                    request.put("quantity", 1);
                    return request;
                })
                .collect(Collectors.toList());
    }
}

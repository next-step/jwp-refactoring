package kitchenpos.menu;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.HttpStatusAssertion;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuAcceptanceTestSupport extends AcceptanceTest {
    public static ExtractableResponse<Response> 메뉴_등록_되어있음(MenuGroup menuGroup, List<Product> products) {
        Menu params = new Menu();
        params.setName("메뉴이름");
        params.setPrice(new BigDecimal(sumOfProductPrice(products)));
        params.setMenuGroupId(menuGroup.getId());
        params.setMenuProducts(convertParamsOfProducts(products));

        ExtractableResponse<Response> response = 메뉴_등록_요청(params);
        메뉴_생성_완료(response);
        return response;
    }

    public static ExtractableResponse<Response> 메뉴_등록_요청(Menu params) {
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

    private static List<MenuProduct> convertParamsOfProducts(List<Product> products) {
        return products.stream()
                .map(product -> {
                    MenuProduct menuProduct = new MenuProduct();
                    menuProduct.setProductId(product.getId());
                    menuProduct.setQuantity(1);
                    return menuProduct;
                })
                .collect(Collectors.toList());
    }
}

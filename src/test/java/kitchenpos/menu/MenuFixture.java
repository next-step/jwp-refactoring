package kitchenpos.menu;

import static kitchenpos.menu.MenuGroupFixture.추천메뉴;
import static kitchenpos.product.ProductFixture.강정치킨;
import static kitchenpos.product.ProductFixture.개손해치킨;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import org.springframework.http.MediaType;

public class MenuFixture {

    public static final MenuProduct 더블강정치킨상품 = new MenuProduct(1L, null, 강정치킨.getId(), 2L);
    public static final MenuProduct 더블개손해치킨상품 = new MenuProduct(1L, null, 개손해치킨.getId(), 2L);
    public static final Menu 더블강정치킨 = new Menu(1L, "더블강정치킨", new BigDecimal(19_000), 추천메뉴,
        Collections.singletonList(더블강정치킨상품));

    public static ExtractableResponse<Response> 메뉴_등록(String name, BigDecimal price,
        Long menuGroupId, List<MenuProductRequest> menuProducts) {
        MenuRequest menuRequest = new MenuRequest(name, price, menuGroupId, menuProducts);

        return RestAssured
            .given().log().all()
            .body(menuRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/api/menus")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회() {

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api/menus")
            .then().log().all()
            .extract();
    }

    public static MenuRequest createMenuRequest(Menu menu) {
        return new MenuRequest(menu.getName(), menu.getPrice(), menu.menuGroupId(),
            createMenuProductReqeust(menu.menuProducts()));
    }

    public static List<MenuProductRequest> createMenuProductReqeust(
        List<MenuProduct> menuProducts) {
        return menuProducts.stream()
            .map(menuProduct -> new MenuProductRequest(menuProduct.productId(),
                menuProduct.getQuantity()))
            .collect(Collectors.toList());
    }
}

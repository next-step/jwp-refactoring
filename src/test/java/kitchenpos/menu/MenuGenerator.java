package kitchenpos.menu;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.Acceptance.utils.RestAssuredRequest;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.product.domain.Product;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class MenuGenerator {
    private static final String PATH = "/api/menus";

    private MenuGenerator() {}

    public static Menu 메뉴_생성(String name, int price, MenuGroup menuGroup, MenuProducts menuProducts) {
        return new Menu(null, name, new Price(new BigDecimal(price)), menuGroup, menuProducts);
    }

    public static MenuGroup 메뉴_그룹_생성(String name) {
        return new MenuGroup(name);
    }

    public static MenuProduct 메뉴_상품_생성(Product product, Long quantity) {
        return new MenuProduct(null, product, quantity);
    }

    public static MenuProducts 메뉴_상품_목록_생성(List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    public static MenuCreateRequest 메뉴_생성_요청(String name, int price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        return new MenuCreateRequest(name, new BigDecimal(price), menuGroupId, menuProductRequests);
    }

    public static MenuProductRequest 메뉴_상품_생성_요청(Long productId, Long quantity) {
        return new MenuProductRequest(productId, quantity);
    }

    public static ExtractableResponse<Response> 메뉴_생성_API(
            String name, int menuPrice, Long menuGroupId, List<MenuProductRequest> menuProducts
    ) {
        MenuCreateRequest body = 메뉴_생성_요청(name, menuPrice, menuGroupId, menuProducts);

        return RestAssuredRequest.postRequest(PATH, Collections.emptyMap(), body);
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_API() {
        return RestAssuredRequest.getRequest(PATH, Collections.emptyMap());
    }
}

package kitchenpos.menu;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.dto.ProductRequest;

public class MenuAcceptanceAPI {

    public static ExtractableResponse<Response> 상품_생성_요청(String name, BigDecimal price) {
        ProductRequest productRequest = new ProductRequest(name, price);

        return AcceptanceTest.doPost("/api/products", productRequest);
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(String name, BigDecimal price, Long menuGroupId,
                                                         MenuProduct menuProduct) {
        List<MenuProductRequest> menuProductRequests = Collections.singletonList(
                new MenuProductRequest(menuProduct.getProductId(), menuProduct.getQuantity()));
        MenuRequest menuRequest = new MenuRequest(name, price, menuGroupId, menuProductRequests);

        return AcceptanceTest.doPost("/api/menus", menuRequest);
    }

    public static ExtractableResponse<Response> 메뉴_조회_요청() {
        return AcceptanceTest.doGet("/api/menus");
    }

    public static ExtractableResponse<Response> 메뉴_그룹_생성_요청(String name) {
        MenuGroup menuGroup = new MenuGroup(name);

        return AcceptanceTest.doPost("/api/menu-groups", menuGroup);
    }

    public static ExtractableResponse<Response> 메뉴_그룹_조회_요청() {
        return AcceptanceTest.doGet("/api/menu-groups");
    }
}

package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

class MenuAcceptanceTestHelper {

    static ExtractableResponse<Response> createMenu(
        String name,
        BigDecimal price,
        MenuGroup menuGroup,
        List<MenuProduct> menuProducts
    ) {
        return createMenu(new Menu(name, price, menuGroup.getId(), menuProducts));
    }

    static ExtractableResponse<Response> createMenu(Menu requestBody) {
        return AcceptanceTestHelper.post("/api/menus", requestBody);
    }

    static ExtractableResponse<Response> getMenus() {
        return AcceptanceTestHelper.get("/api/menus");
    }

    static MenuProduct mapToMenuProduct(Product product, int quantity) {
        return new MenuProduct(product.getId(), quantity);
    }

}

package kitchenpos.menu;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;

public class MenuAcceptanceAPI {

    public static ExtractableResponse<Response> 메뉴_생성_요청(String name, BigDecimal price, Long menuGroupId,
                                                         List<MenuProduct> menuProductList) {
        Menu menu = new Menu(name, price, menuGroupId, menuProductList);

        return AcceptanceTest.doPost("/api/menus", menu);
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

package kitchenpos.application.fixture;


import java.util.List;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuRequest;

public class MenuFixture {

    private MenuFixture() {
    }

    public static MenuRequest 요청_메뉴(String name, int price, Long menuGroupId,
        List<MenuProductRequest> menuProductRequests) {
        return new MenuRequest(name, price, menuGroupId, menuProductRequests);
    }
}

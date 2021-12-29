package kitchenpos.menu.fixtures;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import org.assertj.core.util.Lists;

import java.math.BigDecimal;
import java.util.List;

/**
 * packageName : kitchenpos.fixtures
 * fileName : MenuFixtures
 * author : haedoang
 * date : 2021/12/17
 * description :
 */
public class MenuFixtures {
    public static MenuRequest 양념치킨두마리메뉴요청() {
        return MenuRequest.of(
                "양념치킨두마리메뉴",
                new BigDecimal(32000),
                1L,
                Lists.newArrayList(MenuProductRequest.of(2L, 2L))
        );
    }

    public static MenuRequest 후라이드두마리메뉴요청() {
        return MenuRequest.of(
                "두마리메뉴",
                new BigDecimal(32000),
                3L,
                Lists.newArrayList(MenuProductRequest.of(1L, 2L))
        );
    }

    public static Menu 메뉴(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(name, price, menuGroupId, menuProducts);
    }

    public static MenuRequest 후라이드두마리메뉴요청(BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        return MenuRequest.of(
                "두마리메뉴",
                price,
                menuGroupId,
                menuProductRequests
        );
    }

    public static MenuRequest 후라이드반양념반메뉴요청(BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        return MenuRequest.of(
                "후라이드반양념반메뉴",
                price,
                menuGroupId,
                menuProductRequests
        );
    }

    public static MenuRequest 메뉴등록요청(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        return MenuRequest.of(
                name,
                price,
                menuGroupId,
                menuProductRequests
        );
    }

    public static Menu 양념치킨두마리메뉴() {
        return 양념치킨두마리메뉴요청().toEntity();
    }
}


package kitchenpos.menu.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.*;
import org.assertj.core.util.Lists;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TestMenuFactory {
    public static MenuRequest 메뉴_요청(final String name, final int price, final Long menugroupId) {
        return MenuRequest.of(name, price, menugroupId, new ArrayList<>());
    }

    public static MenuRequest 메뉴_요청(final String name, final int price, final Long menugroupId, final List<MenuProductRequest> menuProductRequests) {
        return MenuRequest.of(name, price, menugroupId, menuProductRequests);
    }

    public static MenuProductRequest 메뉴_상품_요청(final Long productId, final int quatity) {
        return MenuProductRequest.of(productId, quatity);
    }

    public static List<MenuProductRequest> 메뉴_상품_목록_요청(final MenuProductRequest...menuProductRequest) {
        final List<MenuProductRequest> menuReuqests = Lists.newArrayList(menuProductRequest);
        return menuReuqests;
    }

    public static Menu 메뉴_생성(final MenuGroup menuGroup, final String menuName, final long menuPrice) {
        return Menu.of(menuName, menuPrice, menuGroup);
    }

    public static Menu 메뉴_생성됨(final Long id, final String name, final long price, final String menuGroupName) {
        return Menu.of(id, name, price, TestMenuGroupFactory.메뉴그룹_생성(menuGroupName));
    }

    public static Menu 메뉴_조회됨(final Long id, final String name, final long price, final String menuGroupName) {
        return Menu.of(id, name, price, TestMenuGroupFactory.메뉴그룹_생성(menuGroupName));
    }

    public static void 메뉴_생성_확인됨(MenuResponse actual, Menu menu) {
        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(menu.getId()),
                () -> assertThat(actual.getName()).isEqualTo(menu.getName().toName()),
                () -> assertThat(actual.getPrice()).isEqualTo(menu.getPrice().toLong()),
                () -> assertThat(actual.getMenuGroupResponse().getName()).isEqualTo(menu.getMenuGroup().getName()),
                () -> assertThat(actual.getMenuProductResponses()).hasSize(menu.getMenuProducts().toList().size())
        );
    }

    public static List<Menu> 메뉴_목록_조회됨(final int countMenu) {
        final List<Menu> menus = new ArrayList<>();
        for (int i = 1; i <= countMenu; i++) {
            menus.add(Menu.of(Long.valueOf(i), "메뉴", 1000, MenuGroup.of(1L, "메뉴그룹")));
        }
        return menus;
    }

    public static void 메뉴_목록_조회_확인됨(final List<MenuResponse> actual, final List<Menu> menus) {
        assertThat(actual).hasSize(menus.size());
    }

    public static List<MenuProduct> 메뉴상품_목록(final Long productId, final long price, final int quantity ) {
        final MenuProduct menuProduct = MenuProduct.of(productId, quantity);
        return Lists.newArrayList(menuProduct);
    }

    public static MenuResponse 메뉴_응답(final Long id, final String name, final long price, final MenuGroupResponse menuGroupResponse, final List<MenuProductResponse> menuProducts) {
        return MenuResponse.of(id, name, price, menuGroupResponse, menuProducts);
    }

    public static List<MenuProductResponse> 메뉴상품목록_응답(final Long productId, final int quantity) {
        return Lists.newArrayList(MenuProductResponse.of(productId, quantity));
    }
}

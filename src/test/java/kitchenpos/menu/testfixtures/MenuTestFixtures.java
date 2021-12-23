package kitchenpos.menu.testfixtures;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.dao.MenuDao;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;

public class MenuTestFixtures {

    public static void 메뉴_저장_결과_모킹(MenuDao menuDao, Menu menu) {
        given(menuDao.save(any())).willReturn(menu);
    }

    public static void 메뉴_전체조회_모킹(MenuDao menuDao, List<Menu> menus) {
        given(menuDao.findAll()).willReturn(menus);
    }

    public static MenuRequest convertToMenuRequest(Menu menu) {
        MenuProducts menuProducts = menu.getMenuProducts();
        return new MenuRequest(menu.getName(), menu.getPrice(), menu.getMenuGroup().getId(),
            menuProducts.getMenuProducts()
                .stream()
                .map(MenuTestFixtures::convertToMenuProductRequest)
                .collect(Collectors.toList()));
    }

    public static MenuProductRequest convertToMenuProductRequest(MenuProduct menuProduct) {
        return new MenuProductRequest(menuProduct.getProduct().getId(), menuProduct.getQuantity());
    }


    public static final Menu 서비스군만두 = new Menu(1L, "서비스군만두", BigDecimal.valueOf(0),
        MenuGroupTestFixtures.추천메뉴);

    public static void 특정_메뉴_조회_모킹(MenuService menuService, Menu menu) {
        given(menuService.findMenu(menu.getId()))
            .willReturn(menu);
    }
}

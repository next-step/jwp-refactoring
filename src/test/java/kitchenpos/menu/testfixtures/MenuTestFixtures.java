package kitchenpos.menu.testfixtures;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuDao;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductDao;

public class MenuTestFixtures {

    public static void 메뉴_저장_결과_모킹(MenuDao menuDao, Menu menu) {
        given(menuDao.save(any())).willReturn(menu);
    }

    public static void 메뉴_전체조회_모킹(MenuDao menuDao, List<Menu> menus) {
        given(menuDao.findAll()).willReturn(menus);
    }

    public static void 메뉴상품_저장_결과_모킹(MenuProductDao menuProductDao,
        List<MenuProduct> menuProducts) {
        menuProducts.stream().forEach(
            menuProduct -> given(menuProductDao.save(menuProduct)).willReturn(menuProduct));
    }

    public static void 특정_메뉴상품_조회_결과_모킹(MenuProductDao menuProductDao,
        List<MenuProduct> menuProducts) {
        given(menuProductDao.findAllByMenuId(any())).willReturn(menuProducts);
    }

    public static void 특정_리스트에_해당하는_메뉴_개수_조회_모킹(MenuDao menuDao, long size) {
        given(menuDao.countByIdIn(any()))
            .willReturn(size);
    }
}

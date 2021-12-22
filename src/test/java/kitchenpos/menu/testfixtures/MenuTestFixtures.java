package kitchenpos.menu.testfixtures;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
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

    public static void 특정_리스트에_해당하는_메뉴_개수_조회_모킹(MenuDao menuDao, long size) {
        given(menuDao.countByIdIn(any()))
            .willReturn(size);
    }

    public static MenuRequest convertToMenuRequest(Menu menu) {
        return new MenuRequest(menu.getName(), menu.getPrice(), menu.getMenuGroup().getId(),
            menu.getMenuProducts()
                .stream()
                .map(MenuTestFixtures::convertToMenuProductRequest)
                .collect(Collectors.toList()));
    }

    public static MenuProductRequest convertToMenuProductRequest(MenuProduct menuProduct) {
        return new MenuProductRequest(menuProduct.getProduct().getId(), menuProduct.getQuantity());
    }
}

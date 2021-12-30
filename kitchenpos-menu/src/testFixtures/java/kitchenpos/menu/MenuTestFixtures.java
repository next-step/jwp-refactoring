package kitchenpos.menu;

import static org.mockito.BDDMockito.given;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menugroup.MenuGroupTestFixtures;
import org.mockito.ArgumentMatchers;
import kitchenpos.common.vo.Price;

public class MenuTestFixtures {

    public static void 메뉴_저장_결과_모킹(MenuRepository menuRepository, Menu menu) {
        given(menuRepository.save(ArgumentMatchers.any())).willReturn(menu);
    }

    public static void 메뉴_전체조회_모킹(MenuRepository menuRepository, List<Menu> menus) {
        given(menuRepository.findAll()).willReturn(menus);
    }

    public static MenuRequest convertToMenuRequest(Menu menu) {
        return new MenuRequest(menu.getName(), menu.getPriceVal(), menu.getMenuGroup().getId(),
            menu.getMenuProductList()
                .stream()
                .map(MenuTestFixtures::convertToMenuProductRequest)
                .collect(Collectors.toList()));
    }

    public static MenuProductRequest convertToMenuProductRequest(MenuProduct menuProduct) {
        return new MenuProductRequest(menuProduct.getProductId(),
            menuProduct.getQuantityVal());
    }

    public static final Menu 서비스군만두 = new Menu(1L, "서비스군만두", Price.valueOf(BigDecimal.valueOf(0)),
        MenuGroupTestFixtures.추천메뉴);
}

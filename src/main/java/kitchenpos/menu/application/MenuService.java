package kitchenpos.menu.application;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final ProductService productService;
    private final MenuGroupService menuGroupService;

    public MenuService(MenuRepository menuRepository,
                       ProductService productService,
                       MenuGroupService menuGroupService) {
        this.menuRepository = menuRepository;
        this.productService = productService;
        this.menuGroupService = menuGroupService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupService.findMenuGroup(menuRequest.getMenuGroupId());
        Menu menu = menuRequest.toMenu();
        menu.setMenuGroup(menuGroup);
        addMenuProduct(menuRequest, menu);
        menu.checkAmount();

        final Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.from(savedMenu);
    }

    private void addMenuProduct(MenuRequest menuRequest, Menu menu) {
        for (final MenuProductRequest menuProductRequest : menuRequest.getMenuProducts()) {
            final Product product = productService.findProduct(menuProductRequest.getProductId());
            menu.addMenuProduct(new MenuProduct(menuProductRequest.getQuantity(), menu, product));
        }
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        List<MenuResponse> menuResponses = new ArrayList<>();
        final List<Menu> menus = menuRepository.findAll();
        for (Menu menu : menus){
            MenuResponse menuResponse = MenuResponse.from(menu);
            menuResponses.add(menuResponse);
        }
        return menuResponses;
    }

    public Menu findMenu(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 메뉴가 등록되어있지 않습니다."));
    }

}

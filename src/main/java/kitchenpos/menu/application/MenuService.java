package kitchenpos.menu.application;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
                       ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 메뉴 그룹이 등록되어있지 않습니다."));

        Menu menu = menuRequest.toMenu();
        menu.setMenuGroup(menuGroup);

        for (final MenuProductRequest menuProductRequest : menuRequest.getMenuProducts()) {
            final Product product = productRepository.findById(menuProductRequest.getProductId()).orElseThrow(IllegalArgumentException::new);
            menu.addMenuProduct(new MenuProduct(menuProductRequest.getQuantity(), menu, product));
        }

        menu.checkAmount();

        final Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.from(savedMenu);
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
}

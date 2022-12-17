package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.application.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;

    public MenuService(MenuRepository menuRepository, MenuGroupService menuGroupService,
            ProductService productService) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    @Transactional
    public Menu create(final Menu requestMenu) {
        menuGroupService.existsById(requestMenu.getMenuGroupId());
        productService.existProducts(requestMenu.productList());
        final Menu menu = Menu.of(requestMenu.getName(), requestMenu.getPrice(), requestMenu.getMenuGroupId(),
                requestMenu.getMenuProducts());
        menu.updateMenuToMenuProducts();
        return menuRepository.save(menu);
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuRepository.findAll();
    }
}

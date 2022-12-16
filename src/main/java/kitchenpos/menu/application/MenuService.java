package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final MenuProductRepository menuProductRepository;
    private final ProductService productService;

    public MenuService(MenuRepository menuRepository, MenuGroupService menuGroupService,
                       MenuProductRepository menuProductRepository, ProductService productService) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.menuProductRepository = menuProductRepository;
        this.productService = productService;
    }

    @Transactional
    public Menu create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupService.findById(menuRequest.getMenuGroupId());

        return menuRepository.save(new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup,
                createMenuProducts(menuRequest)));
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        final List<Menu> menus = menuRepository.findAllWithMenuGroupAndMenuProducts();

        for (final Menu menu : menus) {
            menu.addMenuProducts(menuProductRepository.findAllByMenuId(menu.getId()));
        }

        return menus;
    }

    private List<MenuProduct> createMenuProducts(MenuRequest menuRequest) {
        return menuRequest.getMenuProductRequests().stream()
                .map(menuProductRequest -> {
                    Product product = productService.findById(menuProductRequest.getProductId());
                    return new MenuProduct(null, product, menuProductRequest.getQuantity());
                })
                .collect(Collectors.toList());
    }
}

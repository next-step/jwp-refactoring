package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuRequest;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
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

        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup);

        menu.addMenuProducts(createMenuProducts(menuRequest));
        menu.validateProductsPrice();

        return menuRepository.save(menu);
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        final List<Menu> menus = menuRepository.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductRepository.findAllByMenuId(menu.getId()));
        }

        return menus;
    }

    @Transactional(readOnly = true)
    public long countByIdIn(List<Long> makeMenuIds) {
        return menuRepository.countByIdIn(makeMenuIds);
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

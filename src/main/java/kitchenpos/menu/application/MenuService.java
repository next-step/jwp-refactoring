package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.validator.MenuValidator;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductService productService;
    private final MenuValidator menuValidator;

    public MenuService(MenuRepository menuRepository,
                       MenuProductRepository menuProductRepository,
                       ProductService productService, MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuProductRepository = menuProductRepository;
        this.productService = productService;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public Menu create(final MenuRequest menuRequest) {
        menuValidator.validateCreation(menuRequest.getMenuGroupId());
        return menuRepository.save(new Menu(menuRequest.getName(), menuRequest.getPrice(), menuRequest.getMenuGroupId(),
                createMenuProducts(menuRequest)));
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        final List<Menu> menus = menuRepository.findAllWithMenuProducts();

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

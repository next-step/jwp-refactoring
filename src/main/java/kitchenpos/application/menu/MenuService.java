package kitchenpos.application.menu;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.application.ProductService;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.exception.NotFoundMenuException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final ProductService productService;
    private final MenuValidator menuValidator;

    public MenuService(
            final MenuRepository menuRepository,
            final ProductService productService,
            final MenuValidator menuValidator
    ) {
        this.menuRepository = menuRepository;
        this.productService = productService;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        Menu menu = Menu.of(menuRequest.getName(), menuRequest.getPrice(), menuRequest.getMenuGroupId());
        List<MenuProduct> menuProducts = this.createMenuProducts(menuRequest);
        menu.appendAllMenuProducts(menuProducts);

        menuValidator.validate(menu);

        return MenuResponse.from(menuRepository.save(menu));
    }

    public List<MenuResponse> list() {
        return MenuResponse.fromList(menuRepository.findAll());
    }

    public Menu findMenu(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new NotFoundMenuException(menuId));
    }

    public long countByIdIn(List<Long> menuIds) {
        return menuRepository.countByIdIn(menuIds);
    }

    private List<MenuProduct> createMenuProducts(MenuRequest menuRequest) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : menuRequest.getMenuProducts()) {
            Product product = productService.findProduct(menuProductRequest.getProductId());
            menuProducts.add(MenuProduct.of(product, menuProductRequest.getQuantity()));
        }

        return menuProducts;
    }
}

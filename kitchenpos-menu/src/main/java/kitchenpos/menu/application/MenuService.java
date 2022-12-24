package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuPrice;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.port.MenuPort;
import kitchenpos.menu.validator.MenuValidator;
import kitchenpos.product.domain.Product;
import kitchenpos.product.port.ProductPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuService {
    private final MenuPort menuPort;
    private final MenuValidator menuValidator;
    private final ProductPort productPort;

    public MenuService(MenuPort menuPort, MenuValidator menuValidator, ProductPort productPort) {
        this.menuPort = menuPort;
        this.menuValidator = menuValidator;
        this.productPort = productPort;
    }

    public MenuResponse create(MenuRequest request) {
        MenuProducts menuProducts = makeMenuProduct(request);
        Menu menu = new Menu(request.getName(), new MenuPrice(request.getPrice()), request.getMenuGroupId(), menuProducts);
        menuValidator.validCheckMakeMenu(menu);
        Menu saveMenu = menuPort.save(menu);

        return MenuResponse.from(saveMenu);
    }

    private MenuProducts makeMenuProduct(MenuRequest request) {
        List<MenuProduct> menuProducts = request.getMenuProduct()
                .stream()
                .map(it -> new MenuProduct(findProductById(it.getProductId()).getId(), it.getQuantity()))
                .collect(Collectors.toList());

        return new MenuProducts(menuProducts);
    }

    private Product findProductById(Long id) {
        return productPort.findById(id);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuPort.findAll();

        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}

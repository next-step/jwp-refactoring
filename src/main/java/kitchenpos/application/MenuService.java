package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.menu.Menu;
import kitchenpos.infra.menu.MenuGroupAdapter;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.menu.exceptions.InvalidMenuPriceException;
import kitchenpos.domain.menu.exceptions.ProductEntityNotFoundException;
import kitchenpos.infra.menu.ProductAdapter;
import kitchenpos.ui.dto.menu.MenuProductRequest;
import kitchenpos.ui.dto.menu.MenuRequest;
import kitchenpos.ui.dto.menu.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuDao menuDao;
    private final MenuGroupAdapter menuGroupAdapter;
    private final MenuProductDao menuProductDao;
    private final ProductAdapter productAdapter;

    public MenuService(
            final MenuDao menuDao,
            final MenuGroupAdapter menuGroupAdapter,
            final MenuProductDao menuProductDao,
            final ProductAdapter productAdapter
    ) {
        this.menuDao = menuDao;
        this.menuGroupAdapter = menuGroupAdapter;
        this.menuProductDao = menuProductDao;
        this.productAdapter = productAdapter;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        final BigDecimal price = menuRequest.getPrice();
        final Menu menu = Menu.of(menuRequest.getName(), price, menuRequest.getMenuGroupId());

        menuGroupAdapter.isExistMenuGroup(menuRequest.getMenuGroupId());

        final List<MenuProductRequest> menuProductRequests = menuRequest.getMenuProducts();
        List<MenuProduct> menuProducts = menuProductRequests.stream()
                .map(it -> MenuProduct.of(null, it.getProductId(), it.getQuantity()))
                .collect(Collectors.toList());

        productAdapter.isValidMenuPrice(price, menuProducts);

        final Menu savedMenu = menuDao.save(menu);

        for (final MenuProductRequest menuProductRequest : menuProductRequests) {
            final MenuProduct menuProduct = MenuProduct.of(
                    savedMenu.getId(), menuProductRequest.getProductId(), menuProductRequest.getQuantity());
            MenuProduct savedMenuProduct = menuProductDao.save(menuProduct);
            savedMenu.addMenuProduct(savedMenuProduct);
        }

        return MenuResponse.of(savedMenu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            List<MenuProduct> menuProducts = menuProductDao.findAllByMenuId(menu.getId());
            menuProducts.forEach(menu::addMenuProduct);
        }

        return menus.stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }
}

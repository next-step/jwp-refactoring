package kitchenpos.application;

import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.infra.menu.MenuDao;
import kitchenpos.infra.menu.MenuProductDao;
import kitchenpos.domain.menu.Menu;
import kitchenpos.infra.menu.MenuGroupAdapter;
import kitchenpos.domain.menu.MenuProduct;
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
    private final MenuRepository menuRepository;

    public MenuService(
            final MenuDao menuDao,
            final MenuGroupAdapter menuGroupAdapter,
            final MenuProductDao menuProductDao,
            final ProductAdapter productAdapter,
            final MenuRepository menuRepository
    ) {
        this.menuDao = menuDao;
        this.menuGroupAdapter = menuGroupAdapter;
        this.menuProductDao = menuProductDao;
        this.productAdapter = productAdapter;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        final BigDecimal price = menuRequest.getPrice();

        final List<MenuProductRequest> menuProductRequests = menuRequest.getMenuProducts();
        List<MenuProduct> menuProducts = menuProductRequests.stream()
                .map(it -> MenuProduct.of(null, it.getProductId(), it.getQuantity()))
                .collect(Collectors.toList());

        final Menu menu = Menu.of(menuRequest.getName(), price, menuRequest.getMenuGroupId(), menuProducts);

        menuGroupAdapter.isExistMenuGroup(menuRequest.getMenuGroupId());
        productAdapter.isValidMenuPrice(price, menuProducts);

        final Menu savedMenu = menuRepository.save(menu);

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

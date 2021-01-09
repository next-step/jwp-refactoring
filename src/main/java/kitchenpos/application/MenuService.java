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
    private final MenuGroupAdapter menuGroupAdapter;
    private final ProductAdapter productAdapter;
    private final MenuRepository menuRepository;

    public MenuService(
            final MenuGroupAdapter menuGroupAdapter,
            final ProductAdapter productAdapter,
            final MenuRepository menuRepository
    ) {
        this.menuGroupAdapter = menuGroupAdapter;
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
        productAdapter.isValidMenuPrice(menu);

        final Menu savedMenu = menuRepository.save(menu);

        return MenuResponse.of(savedMenu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }
}

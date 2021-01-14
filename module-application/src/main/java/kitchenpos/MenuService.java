package kitchenpos;

import dto.menu.MenuProductRequest;
import dto.menu.MenuRequest;
import dto.menu.MenuResponse;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.ProductPrice;
import kitchenpos.domain.menu.exceptions.InvalidMenuPriceException;
import kitchenpos.infra.menu.MenuGroupAdapter;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.infra.menu.ProductAdapter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        final Menu menu = parseRequestToMenu(menuRequest);

        validateMenuCreation(menu);

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

    private Menu parseRequestToMenu(final MenuRequest menuRequest) {
        final List<MenuProductRequest> menuProductRequests = menuRequest.getMenuProducts();
        List<MenuProduct> menuProducts = menuProductRequests.stream()
                .map(it -> MenuProduct.of(it.getProductId(), it.getQuantity()))
                .collect(Collectors.toList());

        return Menu.of(menuRequest.getName(), menuRequest.getPrice(), menuRequest.getMenuGroupId(), menuProducts);
    }

    private void validateMenuCreation(final Menu menu) {
        menuGroupAdapter.isExistMenuGroup(menu.getMenuGroupId());
        productAdapter.isValidMenuPrice(menu);
    }
}

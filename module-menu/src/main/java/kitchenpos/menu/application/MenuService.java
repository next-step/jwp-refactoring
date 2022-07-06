package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.dao.MenuProductRepository;
import kitchenpos.menu.dao.MenuRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuProductRepository menuProductRepository;
    private final MenuValidator menuValidator;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuProductRepository menuProductRepository,
            final MenuValidator menuValidator
    ) {
        this.menuRepository = menuRepository;
        this.menuProductRepository = menuProductRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        menuValidator.validateMenuGroupCheck(menuRequest.getMenuGroupId());
        menuValidator.validatePriceCheck(menuRequest);
        Menu savedMenu = menuRepository.save(menuRequest.toMenu());

        MenuProducts menuProducts = createMenuProduct(menuRequest.getMenuProducts());
        menuProducts.saveMenu(savedMenu);
        menuProductRepository.saveAll(menuProducts.getMenuProducts());

        return MenuResponse.of(savedMenu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    private MenuProducts createMenuProduct(List<MenuProductRequest> menuProductRequestList) {
        List<MenuProduct> menuProducts = menuProductRequestList.stream()
                .map(menuProductRequest -> new MenuProduct(menuProductRequest.getProductId(),
                        menuProductRequest.getQuantity()))
                .collect(Collectors.toList());

        return new MenuProducts(menuProducts);
    }
}

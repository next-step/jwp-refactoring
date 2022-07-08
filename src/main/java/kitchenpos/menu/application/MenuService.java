package kitchenpos.menu.application;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(final MenuRepository menuRepository, MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        final MenuProducts menuProducts = createMenuProducts(request);

        Menu menu = new Menu.Builder()
                .name(request.getName())
                .price(request.getPrice())
                .menuGroupId(request.getMenuGroupId())
                .menuProducts(menuProducts)
                .build();

        menuValidator.validate(menu);

        return MenuResponse.from(menuRepository.save(menu));
    }

    private MenuProducts createMenuProducts(MenuRequest request) {
        MenuProducts menuProducts = new MenuProducts();
        for (final MenuProductRequest menuProductRequest : request.getMenuProducts()) {
            menuProducts.add(MenuProduct.of(menuProductRequest.getProductId(), menuProductRequest.getQuantity()));
        }
        return menuProducts;
    }

    public List<MenuResponse> list() {
        return MenuResponse.from(menuRepository.findAll());
    }
}

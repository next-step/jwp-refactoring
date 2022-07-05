package kitchenpos.menu.application;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.validator.MenuValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuValidator menuValidator;
    private final MenuRepository menuRepository;

    public MenuService(MenuValidator menuValidator, MenuRepository menuRepository) {
        this.menuValidator = menuValidator;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        menuValidator.validateCreateMenu(menuRequest);

        final Name name = Name.of(menuRequest.getName());
        final Price price = Price.of(menuRequest.getPrice());
        final MenuProducts menuProducts = MenuProducts.of(convertToMenuProducts(menuRequest.getMenuProducts()));

        Menu menu = Menu.of(name, price, menuRequest.getMenuGroupId(), menuProducts);
        return MenuResponse.of(menuRepository.save(menu));
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream().map(MenuResponse::of).collect(Collectors.toList());
    }

    private List<MenuProduct> convertToMenuProducts(List<MenuProductRequest> menuProducts) {
        return menuProducts.stream().map(menuProductRequest ->
                        MenuProduct.of(menuProductRequest.getProductId(), Quantity.of(menuProductRequest.getQuantity())))
                .collect(Collectors.toList());
    }
}

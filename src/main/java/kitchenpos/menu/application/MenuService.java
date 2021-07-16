package kitchenpos.menu.application;

import kitchenpos.common.Price;
import kitchenpos.common.Quantity;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(final MenuRepository menuRepository, final MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    public MenuResponse create(final MenuRequest menuRequest) {
        Menu menu = toMenu(menuRequest);
        menuValidator.validate(menu);
        Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.of(savedMenu);
    }

    private Menu toMenu(MenuRequest menuRequest) {
        return new Menu(menuRequest.getName(),
                Price.valueOf(menuRequest.getPrice()),
                menuRequest.getMenuGroupId(),
                toMenuProductList(menuRequest.getMenuProductRequests()));
    }

    private List<MenuProduct> toMenuProductList(List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
                .map(this::toMenuProduct)
                .collect(Collectors.toList());
    }

    private MenuProduct toMenuProduct(MenuProductRequest menuProductRequest) {
        return new MenuProduct(menuProductRequest.getMenuId(),
                menuProductRequest.getProductId(),
                Quantity.of(menuProductRequest.getQuantity()));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return MenuResponse.ofList(menus);
    }
}

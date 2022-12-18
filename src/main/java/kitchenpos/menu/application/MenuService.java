package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;

@Transactional(readOnly = true)
@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(final MenuRepository menuRepository, final MenuValidator menuValidator
    ) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        menuValidator.validate(menuRequest);
        Menu savedMenu = menuRepository.save(generateMenu(menuRequest));

        return MenuResponse.from(savedMenu);
    }

    private Menu generateMenu(MenuRequest menuRequest) {
        return Menu.generate(
            menuRequest.getName(),
            menuRequest.getPrice(),
            menuRequest.getMenuGroupId(),
            menuRequest.getMenuProducts().stream()
                .map(menuProduct -> MenuProduct.generate(
                    menuProduct.getProductId(),
                    menuProduct.getQuantity()
                )).collect(Collectors.toList())
        );
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList());
    }
}

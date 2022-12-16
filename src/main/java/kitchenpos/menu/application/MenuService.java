package kitchenpos.menu.application;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.validator.MenuValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuProductRepository menuProductRepository;
    private final MenuValidator menuValidator;

    public MenuService(MenuRepository menuRepository, MenuProductRepository menuProductRepository,
                       MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuProductRepository = menuProductRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public Menu create(final MenuRequest menuRequest) {
        menuValidator.validateCreation(menuRequest.getMenuGroupId());
        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuRequest.getMenuGroupId(),
                menuValidator.createMenuProducts(menuRequest.getMenuProductRequests()));
        menuValidator.validateProductsPrice(menu);
        return menuRepository.save(menu);
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        final List<Menu> menus = menuRepository.findAllWithMenuProducts();

        for (final Menu menu : menus) {
            menu.addMenuProducts(menuProductRepository.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}

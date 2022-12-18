package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuCreateValidator;
import kitchenpos.menu.domain.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuCreateValidator menuCreateValidator;

    public MenuService(MenuRepository menuRepository, MenuCreateValidator menuCreateValidator) {
        this.menuRepository = menuRepository;
        this.menuCreateValidator = menuCreateValidator;
    }

    @Transactional
    public Menu create(final Menu requestMenu) {
        menuCreateValidator.validate(requestMenu);
        final Menu menu = Menu.of(requestMenu.getName(), requestMenu.getPrice(), requestMenu.getMenuGroupId(),
                requestMenu.getMenuProducts());
        menu.updateMenuToMenuProducts();
        return menuRepository.save(menu);
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuRepository.findAll();
    }
}

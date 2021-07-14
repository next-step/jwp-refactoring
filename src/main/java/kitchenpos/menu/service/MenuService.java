package kitchenpos.menu.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.dto.MenuRequest;

@Service
public class MenuService {
    private final MenuValidator menuValidator;
    private final MenuRepository menuRepository;

    public MenuService(MenuValidator menuValidator, MenuRepository menuRepository) {
        this.menuValidator = menuValidator;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public Menu create(final MenuRequest request) {
        Menu menu = request.toEntity();
        menu.setMenuGroupId(menuValidator, request.getMenuGroupId());
        return menuRepository.save(menu);
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}

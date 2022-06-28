package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuValidator;
import kitchenpos.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(MenuRepository menuRepository, MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public Menu create(final Menu menu) {
        menuValidator.checkMenuGroup(menu);
        menuValidator.checkPrice(menu);
        return menuRepository.save(menu);
    }

    public List<Menu> list() {
        return menuRepository.findAllWithMenuProduct();
    }
}

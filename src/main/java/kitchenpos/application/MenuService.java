package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu2;
import kitchenpos.domain.MenuRepository;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Transactional
    public Menu2 create2(final Menu2 menu) {
        menu.validatePrice();
        return menuRepository.save(menu);
    }

    public List<Menu2> list2() {
        return menuRepository.findAll();
    }
}

package kitchenpos.application;

import kitchenpos.dao.MenuRepository;
import kitchenpos.domain.Menu;
import kitchenpos.ui.dto.MenuCreateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class MenuService {
    private final MenuCreator menuCreator;
    private final MenuRepository menuRepository;

    public MenuService(MenuCreator menuCreator, MenuRepository menuRepository) {
        this.menuCreator = menuCreator;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public Menu create(final MenuCreateRequest request) {
        return menuRepository.save(menuCreator.create(request));
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}

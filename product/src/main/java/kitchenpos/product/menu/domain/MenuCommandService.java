package kitchenpos.product.menu.domain;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuCommandService {

    private final MenuRepository menuRepository;

    public MenuCommandService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public Menu save(Menu menu) {
        return menuRepository.save(menu);
    }
}

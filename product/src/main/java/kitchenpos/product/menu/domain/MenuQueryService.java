package kitchenpos.product.menu.domain;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuQueryService {

    private final MenuRepository menuRepository;

    public MenuQueryService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<Menu> findAll() {
        return menuRepository.findAll();
    }
}

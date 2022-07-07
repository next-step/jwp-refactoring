package kitchenpos.menu.application;

import kitchenpos.menu.application.validator.MenuValidatorGroup;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuValidatorGroup menuValidatorGroup;

    public MenuService(MenuRepository menuRepository, MenuValidatorGroup menuValidatorGroup) {
        this.menuRepository = menuRepository;
        this.menuValidatorGroup = menuValidatorGroup;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        menuValidatorGroup.validate(menuRequest);
        Menu menu = menuRequest.toEntity();
        Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.of(savedMenu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAllWithMenuProducts();
        return MenuResponse.of(menus);
    }

    public long countByIdIn(List<Long> menuIds) {
        return menuRepository.countByIdIn(menuIds);
    }
}

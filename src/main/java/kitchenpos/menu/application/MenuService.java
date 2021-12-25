package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuRepository;

@Service
public class MenuService {
    private final MenuValidator menuValidator;
    private final MenuRepository menuRepository;

    public MenuService(MenuValidator menuValidator, MenuRepository menuRepository) {
        this.menuValidator = menuValidator;
        this.menuRepository = menuRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        Menu menu = menuRequest.toEntity();
        menuValidator.validateCreate(menu);
        return new MenuResponse(menuRepository.save(menu));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findMenus();
        return menus.stream()
            .map(MenuResponse::new)
            .collect(Collectors.toList());
    }
}

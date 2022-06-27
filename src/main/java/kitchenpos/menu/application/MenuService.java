package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(
            final MenuRepository menuRepository, final MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        menuValidator.validate(request);
        Menu menu = new Menu(request.getName(), request.getPrice(), request.getMenuGroupId(), request.getMenuProducts());
        menuRepository.save(menu);
        return new MenuResponse(menu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream().map(MenuResponse::new).collect(Collectors.toList());
    }
}

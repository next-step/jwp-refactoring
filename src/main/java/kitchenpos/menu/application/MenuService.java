package kitchenpos.menu.application;

import kitchenpos.menu.dao.MenuRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.validator.MenuValidator;
import kitchenpos.menu.domain.Menus;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(final MenuRepository menuRepository, MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        Menu menu = request.of();
        menuValidator.validateMenu(menu);

        return MenuResponse.from(menuRepository.save(menu));
    }

    public List<MenuResponse> listResponse() {
        return new Menus(menuRepository.findAll()).toResponse();
    }

    public Menus findMenusInIds(final List<Long> ids) {
        return new Menus(menuRepository.findAllById(ids));
    }
}

package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.mapper.MenuMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(final MenuRepository menuRepository, final MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    public MenuResponse create(final MenuCreateRequest request) {
        final Menu menu = request.toEntity();
        menuValidator.menuCreateValidator(menu);

        menu.addMenu();
        final Menu savedMenu = menuRepository.save(menu);

        return MenuMapper.toMenuResponse(savedMenu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return MenuMapper.toMenuResponses(menus);
    }
}

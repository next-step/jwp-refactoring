package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.exception.NotFoundMenuGroupException;
import kitchenpos.menu.mapper.MenuMapper;
import kitchenpos.menu.validator.MenuValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuService {
    private final MenuMapper menuMapper;
    private final MenuValidator menuValidator;
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuService(MenuMapper menuMapper, MenuValidator menuValidator, MenuRepository menuRepository, MenuGroupRepository menuGroupRepository) {
        this.menuMapper = menuMapper;
        this.menuValidator = menuValidator;
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(() -> new NotFoundMenuGroupException());
        Menu menu = menuMapper.mapFormToMenu(menuRequest, menuGroup);
        menu.validateToMenu(menuValidator);
        return MenuResponse.of(menuRepository.save(menu));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }
}

package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuIdsExistRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(MenuRepository menuRepository, MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    public MenuResponse create(final MenuRequest menuRequest) {
        Menu menu = menuRequest.toMenu();

        menuValidator.validateCreateMenu(menu);

        return MenuResponse.from(menuRepository.save(menu));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public void isExistMenuIds(MenuIdsExistRequest menuIdsExistRequest) {
        List<Long> menuIds = menuIdsExistRequest.getMenuIds();

        List<Menu> menus = getMenus(menuIds);

        menuIds.forEach(checkExistMenuId(menus));
    }

    private List<Menu> getMenus(List<Long> menuIds) {
        return menuRepository.findAllById(menuIds);
    }

    private Consumer<Long> checkExistMenuId(List<Menu> menus) {
        return menuId -> menus.stream()
                .filter(menu -> menu.isEqualMenuId(menuId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

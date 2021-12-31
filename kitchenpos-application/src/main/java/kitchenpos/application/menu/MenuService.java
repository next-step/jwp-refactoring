package kitchenpos.application.menu;

import kitchenpos.application.menu.dto.MenuResponse;
import kitchenpos.domain.menu.domain.Menu;
import kitchenpos.domain.menu.domain.MenuRepository;
import kitchenpos.domain.menu.validator.MenuCreateValidator;
import kitchenpos.domain.menu.validator.MenuGroupMenuCreateValidator;
import kitchenpos.domain.menu.validator.MenuPriceMenuCreateValidator;
import kitchenpos.application.menu.dto.MenuRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final List<MenuCreateValidator> menuCreateValidators;

    public MenuService(MenuRepository menuRepository, MenuPriceMenuCreateValidator menuPriceMenuCreateValidator,
                       MenuGroupMenuCreateValidator menuGroupMenuCreateValidator) {
        this.menuRepository = menuRepository;
        this.menuCreateValidators = Arrays.asList(menuPriceMenuCreateValidator, menuGroupMenuCreateValidator);
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        final Menu menu = Menu.create(request.getName(), request.getPrice(), request.getMenuGroupId(), request.getMenuProducts(),
                menuCreateValidators);
        return MenuResponse.of(menuRepository.save(menu));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }
}

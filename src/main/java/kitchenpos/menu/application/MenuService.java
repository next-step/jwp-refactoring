package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.validator.MenuGroupMenuCreateValidator;
import kitchenpos.menu.domain.validator.MenuPriceMenuCreateValidator;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.infra.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuPriceMenuCreateValidator menuPriceMenuCreateValidator;
    private final MenuGroupMenuCreateValidator menuGroupMenuCreateValidator;

    public MenuService(MenuRepository menuRepository, MenuPriceMenuCreateValidator menuPriceMenuCreateValidator,
                       MenuGroupMenuCreateValidator menuGroupMenuCreateValidator) {
        this.menuRepository = menuRepository;
        this.menuPriceMenuCreateValidator = menuPriceMenuCreateValidator;
        this.menuGroupMenuCreateValidator = menuGroupMenuCreateValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        final Menu menu = Menu.create(request.getName(), request.getPrice(), request.getMenuGroupId(), request.getMenuProducts(),
                Arrays.asList(menuGroupMenuCreateValidator, menuPriceMenuCreateValidator));
        return MenuResponse.of(menuRepository.save(menu));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }
}

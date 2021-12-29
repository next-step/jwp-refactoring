package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.validator.MenuGroupCreateValidator;
import kitchenpos.menu.domain.validator.MenuPriceCreateValidator;
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
    private final MenuPriceCreateValidator menuPriceCreateValidator;
    private final MenuGroupCreateValidator menuGroupCreateValidator;

    public MenuService(MenuRepository menuRepository, MenuPriceCreateValidator menuPriceCreateValidator,
                       MenuGroupCreateValidator menuGroupCreateValidator) {
        this.menuRepository = menuRepository;
        this.menuPriceCreateValidator = menuPriceCreateValidator;
        this.menuGroupCreateValidator = menuGroupCreateValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        final Menu menu = Menu.create(request, Arrays.asList(menuGroupCreateValidator, menuPriceCreateValidator));
        return MenuResponse.of(menuRepository.save(menu));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }
}

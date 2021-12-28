package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.validator.MenuGroupCreateValidator;
import kitchenpos.menu.domain.validator.MenuPriceValidator;
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
    private final MenuPriceValidator menuPriceValidator;
    private final MenuGroupCreateValidator menuGroupValidator;

    public MenuService(MenuRepository menuRepository, MenuPriceValidator menuPriceValidator,
                       MenuGroupCreateValidator menuGroupValidator) {
        this.menuRepository = menuRepository;
        this.menuPriceValidator = menuPriceValidator;
        this.menuGroupValidator = menuGroupValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        final Menu menu = Menu.create(request, Arrays.asList(menuGroupValidator, menuPriceValidator));
        return MenuResponse.of(menuRepository.save(menu));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public int countByIdIn(List<Long> menuIds) {
        return menuRepository.countByIdIn(menuIds);
    }
}

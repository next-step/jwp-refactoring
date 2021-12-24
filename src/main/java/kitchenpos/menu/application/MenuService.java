package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuPrice;
import kitchenpos.menu.domain.MenuProductGroup;
import kitchenpos.menu.domain.validator.MenuGroupValidator;
import kitchenpos.menu.domain.validator.MenuPriceValidator;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.infra.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuPriceValidator menuPriceValidator;
    private final MenuGroupValidator menuGroupValidator;

    public MenuService(MenuRepository menuRepository, MenuPriceValidator menuPriceValidator,
                       MenuGroupValidator menuGroupValidator) {
        this.menuRepository = menuRepository;
        this.menuPriceValidator = menuPriceValidator;
        this.menuGroupValidator = menuGroupValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        menuGroupValidator.validate(request.getMenuGroupId());
        menuPriceValidator.validate(
                MenuPrice.of(request.getPrice()),
                MenuProductGroup.ofRequests(request.getMenuProductRequests())
        );
        return MenuResponse.of(menuRepository.save(request.toEntity()));
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

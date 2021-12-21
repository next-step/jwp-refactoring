package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuPriceValidator;
import kitchenpos.menu.domain.NotFoundMenuGroupValidator;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.infra.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuPriceValidator menuPriceValidator;
    private final NotFoundMenuGroupValidator notFoundMenuGroupValidator;

    public MenuService(MenuRepository menuRepository, MenuPriceValidator menuPriceValidator, NotFoundMenuGroupValidator notFoundMenuGroupValidator) {
        this.menuRepository = menuRepository;
        this.menuPriceValidator = menuPriceValidator;
        this.notFoundMenuGroupValidator = notFoundMenuGroupValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        notFoundMenuGroupValidator.validate(request.getMenuGroupId());
        menuPriceValidator.validate(BigDecimal.valueOf(request.getPrice()), request.getMenuProductRequests());
        return MenuResponse.of(menuRepository.save(request.toEntity()));
    }


    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }
}

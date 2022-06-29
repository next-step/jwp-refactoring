package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.MenuValidator;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(MenuRepository menuRepository, MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    public MenuResponse create(final MenuRequest menuRequest) {
        menuValidator.validate(menuRequest);
        return MenuResponse.from(menuRepository.save(menuRequest.toEntity()));
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}

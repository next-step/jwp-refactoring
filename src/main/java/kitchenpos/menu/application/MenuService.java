package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuCreatingValidator menuCreatingValidator;
    private final MenuRepository menuRepository;

    public MenuService(
            MenuCreatingValidator menuCreatingValidator,
            MenuRepository menuRepository
            ) {
        this.menuRepository = menuRepository;
        this.menuCreatingValidator = menuCreatingValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        menuCreatingValidator.validate(request);
        return MenuResponse.of(createMenu(request));
    }

    private Menu createMenu(MenuRequest request) {
        Menu menu = menuRepository.save(request.toMenu());
        menu.addMenuProducts(toMenuProducts(request));
        return menu;
    }

    private List<MenuProduct> toMenuProducts(MenuRequest request) {
        return request.getMenuProducts()
                      .stream()
                      .map(MenuProductRequest::toMenuProduct)
                      .collect(Collectors.toList());
    }

    public List<MenuResponse> list() {
        return MenuResponse.of(menuRepository.findAll());
    }
}

package kitchenpos.menu.application;

import kitchenpos.core.domain.Price;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

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
    public MenuResponse create(MenuRequest request) {
        menuCreatingValidator.validate(request.getMenuGroupId(), new Price(request.getPrice()), request.getMenuProducts());
        return MenuResponse.of(createMenu(request));
    }

    private Menu createMenu(MenuRequest request) {
        Menu menu = menuRepository.save(request.toMenu());
        menu.addMenuProducts(request.getMenuProducts());
        return menu;
    }
    public List<MenuResponse> list() {
        return MenuResponse.of(menuRepository.findAll());
    }
}

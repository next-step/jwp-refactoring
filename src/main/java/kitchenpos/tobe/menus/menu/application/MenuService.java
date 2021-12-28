package kitchenpos.tobe.menus.menu.application;

import java.util.List;
import kitchenpos.tobe.common.domain.Validator;
import kitchenpos.tobe.menus.menu.domain.Menu;
import kitchenpos.tobe.menus.menu.domain.MenuRepository;
import kitchenpos.tobe.menus.menu.dto.MenuRequest;
import kitchenpos.tobe.menus.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final Validator<Menu> menuValidator;

    public MenuService(
        final MenuRepository menuRepository,
        final Validator<Menu> menuValidator
    ) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse register(final MenuRequest menuRequest) {
        final Menu menu = menuRepository.save(menuRequest.toMenu(menuValidator));
        return MenuResponse.of(menu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return MenuResponse.ofList(menuRepository.findAll());
    }
}

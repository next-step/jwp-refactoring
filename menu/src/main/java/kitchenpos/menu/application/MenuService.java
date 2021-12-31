package kitchenpos.menu.application;

import kitchenpos.domain.Validator;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
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

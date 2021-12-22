package kitchenpos.tobe.menu.application;

import java.util.List;
import kitchenpos.tobe.menu.domain.Menu;
import kitchenpos.tobe.menu.domain.MenuRepository;
import kitchenpos.tobe.menu.domain.MenuValidator;
import kitchenpos.tobe.menu.dto.MenuRequest;
import kitchenpos.tobe.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuValidator menuValidator;
    private final MenuRepository menuRepository;

    public MenuService(
        final MenuValidator menuValidator,
        final MenuRepository menuRepository
    ) {
        this.menuValidator = menuValidator;
        this.menuRepository = menuRepository;
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

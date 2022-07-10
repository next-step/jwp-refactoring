package menu.application;

import menu.domain.Menu;
import menu.dto.MenuRequest;
import menu.dto.MenuResponse;
import menu.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuValidator menuValidator
    ) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(MenuRequest request) {
        menuValidator.validate(request);
        final Menu menu = menuRepository.save(request.toMenu());
        return MenuResponse.of(menu);
    }

    public List<MenuResponse> list() {
        return MenuResponse.of(menuRepository.findAll());
    }
}

package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.dto.CreateMenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.validator.MenuValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;

    public MenuService(final MenuRepository menuRepository, final MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponse create(final CreateMenuRequest createMenuRequest) {
        menuValidator.validate(createMenuRequest);
        final Menu savedMenu = menuRepository.save(createMenuRequest.toEntity());
        savedMenu.addMenuProducts(createMenuRequest.toMenuProducts());

        return MenuResponse.of(savedMenu);
    }


    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return MenuResponse.ofList(menus);
    }
}

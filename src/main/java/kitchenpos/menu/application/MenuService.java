package kitchenpos.menu.application;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.mapper.MenuMapper;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.validator.MenuValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuValidator menuValidator;
    private final MenuMapper menuMapper;

    public MenuService(MenuRepository menuRepository, MenuValidator menuValidator,
                       MenuMapper menuMapper) {
        this.menuRepository = menuRepository;
        this.menuValidator = menuValidator;
        this.menuMapper = menuMapper;
    }

    @Transactional
    public Menu create(final MenuRequest menuRequest) {
        menuValidator.validateCreation(menuRequest.getMenuGroupId());
        Menu menu = menuMapper.mapFrom(menuRequest);
        menuValidator.validateProductsPrice(menu);
        return menuRepository.save(menu);
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuRepository.findAllWithMenuProducts();
    }
}

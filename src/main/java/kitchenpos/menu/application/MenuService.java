package kitchenpos.menu.application;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.mapper.MenuMapper;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.validator.MenuValidators;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuValidators menuValidators;
    private final MenuMapper menuMapper;

    public MenuService(MenuRepository menuRepository, MenuValidators menuValidators,
                       MenuMapper menuMapper) {
        this.menuRepository = menuRepository;
        this.menuValidators = menuValidators;
        this.menuMapper = menuMapper;
    }

    @Transactional
    public Menu create(final MenuRequest menuRequest) {
        menuValidators.validateCreation(menuRequest.getMenuGroupId());
        Menu menu = menuMapper.mapFrom(menuRequest);
        menuValidators.validateProductsPrice(menu);
        return menuRepository.save(menu);
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuRepository.findAllWithMenuProducts();
    }
}

package kitchenpos.menu.infrastructure.adapter;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.infrastructure.repository.MenuJpaRepository;
import kitchenpos.menu.port.MenuPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kitchenpos.constants.ErrorCodeType.MENU_NOT_FOUND;

@Service
@Transactional
public class MenuJpaAdapter implements MenuPort {

    private final MenuJpaRepository menuJpaRepository;

    public MenuJpaAdapter(MenuJpaRepository menuJpaRepository) {
        this.menuJpaRepository = menuJpaRepository;
    }

    @Override
    public Menu save(Menu entity) {
        return menuJpaRepository.save(entity);
    }

    @Override
    public Menu findById(Long id) {
        return menuJpaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(MENU_NOT_FOUND.getMessage()));
    }

    @Override
    public List<Menu> findAll() {
        return menuJpaRepository.findAll();
    }

    @Override
    public List<Menu> findAllByMenuId(List<Long> menuIds) {
        return menuJpaRepository.findAllByIdIn(menuIds);
    }
}

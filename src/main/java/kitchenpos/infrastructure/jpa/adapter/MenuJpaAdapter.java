package kitchenpos.infrastructure.jpa.adapter;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.infrastructure.jpa.repository.MenuJpaRepository;
import kitchenpos.port.MenuPort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

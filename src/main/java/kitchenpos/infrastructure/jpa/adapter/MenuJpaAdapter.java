package kitchenpos.infrastructure.jpa.adapter;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.infrastructure.jpa.repository.MenuJpaRepository;
import kitchenpos.port.MenuPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
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
        return menuJpaRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public List<Menu> findAll() {
        return menuJpaRepository.findAll();
    }

    @Override
    public List<Menu> findAllByMenuId(List<Long> id) {
        return menuJpaRepository.findAllByIdIn(id);
    }

    @Override
    public long countByIdIn(List<Long> ids) {
        return menuJpaRepository.countByIdIn(ids);
    }
}

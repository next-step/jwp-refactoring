package kitchenpos.infrastructure.jpa.adapter;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.infrastructure.jpa.repository.MenuGroupJpaRepository;
import kitchenpos.infrastructure.jpa.repository.MenuProductJpaRepository;
import kitchenpos.port.MenuGroupPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MenuGroupJpaAdapter implements MenuGroupPort {

    private final MenuGroupJpaRepository menuGroupJpaRepository;

    public MenuGroupJpaAdapter(MenuGroupJpaRepository menuGroupJpaRepository) {
        this.menuGroupJpaRepository = menuGroupJpaRepository;
    }

    @Override
    public MenuGroup save(MenuGroup entity) {
        return null;
    }

    @Override
    public MenuGroup findById(Long id) {
        return menuGroupJpaRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public List<MenuGroup> findAll() {
        return menuGroupJpaRepository.findAll();
    }

    @Override
    public boolean existsById(Long id) {
        return menuGroupJpaRepository.existsById(id);
    }
}

package kitchenpos.infrastructure.jpa.adapter;

import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.infrastructure.jpa.repository.MenuGroupJpaRepository;
import kitchenpos.infrastructure.jpa.repository.MenuProductJpaRepository;
import kitchenpos.port.MenuGroupPort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MenuGroupJpaAdapter implements MenuGroupPort {

    private final MenuGroupJpaRepository menuGroupJpaRepository;

    public MenuGroupJpaAdapter(MenuGroupJpaRepository menuGroupJpaRepository) {
        this.menuGroupJpaRepository = menuGroupJpaRepository;
    }

    @Override
    @Transactional
    public MenuGroup save(MenuGroup entity) {
        return menuGroupJpaRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public MenuGroup findById(Long id) {
        return menuGroupJpaRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuGroup> findAll() {
        return menuGroupJpaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return menuGroupJpaRepository.existsById(id);
    }
}

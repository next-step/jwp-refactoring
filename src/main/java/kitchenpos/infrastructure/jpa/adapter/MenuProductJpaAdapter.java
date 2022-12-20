package kitchenpos.infrastructure.jpa.adapter;


import kitchenpos.domain.MenuProduct;
import kitchenpos.infrastructure.jpa.repository.MenuProductJpaRepository;
import kitchenpos.port.MenuProductPort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MenuProductJpaAdapter implements MenuProductPort {

    private final MenuProductJpaRepository menuProductJpaRepository;

    public MenuProductJpaAdapter(MenuProductJpaRepository menuProductJpaRepository) {
        this.menuProductJpaRepository = menuProductJpaRepository;
    }

    @Override
    public MenuProduct save(MenuProduct entity) {
        return menuProductJpaRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MenuProduct> findById(Long id) {
        return Optional.empty();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuProduct> findAll() {
        return menuProductJpaRepository.findAll();
    }
}

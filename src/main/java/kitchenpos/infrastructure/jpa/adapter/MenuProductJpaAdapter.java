package kitchenpos.infrastructure.jpa.adapter;


import kitchenpos.domain.MenuProduct;
import kitchenpos.infrastructure.jpa.repository.MenuProductJpaRepository;
import kitchenpos.port.MenuProductPort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MenuProductJpaAdapter implements MenuProductPort {


    private final MenuProductJpaRepository menuProductJpaRepository;

    public MenuProductJpaAdapter(MenuProductJpaRepository menuProductJpaRepository) {
        this.menuProductJpaRepository = menuProductJpaRepository;
    }

    @Override
    public MenuProduct save(MenuProduct entity) {
        return null;
    }

    @Override
    public Optional<MenuProduct> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<MenuProduct> findAll() {
        return null;
    }

    @Override
    public List<MenuProduct> findAllByMenuId(Long menuId) {
        return null;
    }
}

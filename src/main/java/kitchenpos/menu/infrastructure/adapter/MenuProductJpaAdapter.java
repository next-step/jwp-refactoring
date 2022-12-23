package kitchenpos.menu.infrastructure.adapter;


import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.infrastructure.repository.MenuProductJpaRepository;
import kitchenpos.menu.port.MenuProductPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static kitchenpos.common.constants.ErrorCodeType.NOT_FOUND_PRODUCT;

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
        return menuProductJpaRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuProduct> findAll() {
        return menuProductJpaRepository.findAll();
    }
}

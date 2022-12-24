package kitchenpos.menugroup.infrastructure.adapter;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.infrastructure.repository.MenuGroupJpaRepository;
import kitchenpos.menugroup.port.MenuGroupPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kitchenpos.menugroup.exceptions.MenuGroupErrorCode.MENU_GROUP_NOT_FOUND;

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
        return menuGroupJpaRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException(MENU_GROUP_NOT_FOUND.getMessage()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuGroup> findAll() {
        return menuGroupJpaRepository.findAll();
    }
}

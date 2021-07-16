package kitchenpos.menugroup.application;

import java.util.Optional;

import org.springframework.stereotype.Service;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;

@Service
public class MenuGroupMenuServiceImpl implements MenuGroupMenuService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupMenuServiceImpl(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public Optional<MenuGroup> findMenuGroupById(Long menuGroupId) {
        return this.menuGroupRepository.findById(menuGroupId);
    }

}

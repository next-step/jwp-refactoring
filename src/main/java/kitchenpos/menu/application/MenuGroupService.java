package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroups;

    public MenuGroupService(final MenuGroupRepository menuGroups) {
        this.menuGroups = menuGroups;
    }

    @Transactional
    public MenuGroup create(final MenuGroup menuGroup) {
        return menuGroups.save(menuGroup);
    }

    public List<MenuGroup> list() {
        return menuGroups.findAll();
    }
}

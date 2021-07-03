package kitchenpos.application;

import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupCreate;
import kitchenpos.repository.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroup create(final MenuGroupCreate create) {
        return menuGroupRepository.save(new MenuGroup(create.getName()));
    }

    public List<MenuGroup> list() {
        return menuGroupRepository.findAll();
    }
}

package kitchenpos.application.command;

import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupCreate;
import kitchenpos.repository.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public MenuGroup create(final MenuGroupCreate create) {
        return menuGroupRepository.save(MenuGroup.from(create));
    }
}

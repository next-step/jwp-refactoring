package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.MenuGroup2;
import kitchenpos.domain.MenuGroupRepository;

@Service
@Transactional(readOnly = true)
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroup2 create(final MenuGroup2 menuGroup) {
        return menuGroupRepository.save(menuGroup);
    }

    public List<MenuGroup2> list() {
        return menuGroupRepository.findAll();
    }

}

package kitchenpos.menugroup.application;

import kitchenpos.common.domain.Name;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public MenuGroup findById(Long id) {
        return menuGroupRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    public List<MenuGroup> list() {
        return menuGroupRepository.findAll();
    }

    @Transactional
    public MenuGroup create(MenuGroupRequest menuGroupRequest) {
        Name name = Name.of(menuGroupRequest.getName());
        MenuGroup menuGroup = MenuGroup.of(name);

        return menuGroupRepository.save(menuGroup);
    }
}

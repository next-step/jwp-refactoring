package kitchenpos.application.menugroup;

import kitchenpos.exception.InvalidEntityException;
import kitchenpos.repository.menu.MenuGroupRepository;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.dto.menu.MenuGroupRequest;
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
    public MenuGroup create(final MenuGroupRequest menuGroupRequest) {
        MenuGroup menuGroup = menuGroupRequest.toEntity();
        return menuGroupRepository.save(menuGroup);
    }

    public List<MenuGroup> list() {
        return menuGroupRepository.findAll();
    }

    public boolean isExists(Long menuGroupId) {
        return menuGroupRepository.existsById(menuGroupId);
    }

    public MenuGroup findById(Long id) {
        return menuGroupRepository.findById(id)
                .orElseThrow(() -> new InvalidEntityException("Not found MenuGroup " + id));
    }
}

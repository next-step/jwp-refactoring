package kitchenpos.application.menu;

import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.dto.menu.MenuGroupRequest;
import kitchenpos.dto.menu.MenuGroupResponse;
import kitchenpos.repository.menu.MenuGroupRepository;
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
    public MenuGroupResponse create(final MenuGroupRequest menuGroupRequest) {
        MenuGroup menuGroup = menuGroupRepository.save(menuGroupRequest.toMenuGroup());
        return MenuGroupResponse.from(menuGroup);
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        List<MenuGroup> menuGroups = menuGroupRepository.findAll();
        return MenuGroupResponse.toMenuGroupResponses(menuGroups);
    }
}

package kitchenpos.menu.application;


import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MenuGroupService {
    private final MenuGroupRepository repository;

    public MenuGroupService(final MenuGroupRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest request) {
        MenuGroup menuGroup = repository.save(request.toMenuGroup());
        return MenuGroupResponse.of(menuGroup);
    }

    public List<MenuGroupResponse> searchAllMenuGroups() {
        return MenuGroupResponse.of(repository.findAll());
    }
}

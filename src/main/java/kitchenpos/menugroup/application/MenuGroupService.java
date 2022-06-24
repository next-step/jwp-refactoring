package kitchenpos.menugroup.application;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
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

    public List<MenuGroupResponse> list() {
        return MenuGroupResponse.of(repository.findAll());
    }
}

package kitchenpos.application;

import kitchenpos.domain.MenuGroupEntity;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuGroupService {
    private final MenuGroupRepository repository;

    public MenuGroupService(final MenuGroupRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest request) {
        MenuGroupEntity menuGroup = repository.save(request.toMenuGroup());
        return MenuGroupResponse.of(menuGroup);
    }

    public List<MenuGroupResponse> list() {
        return MenuGroupResponse.of(repository.findAll());
    }
}

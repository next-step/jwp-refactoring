package kitchenpos.application;

import java.util.List;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.domain.MenuGroup;
import kitchenpos.request.MenuGroupRequest;
import kitchenpos.response.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest request) {
        MenuGroup menuGroup = menuGroupRepository.save(request.toMenuGroup());
        return MenuGroupResponse.of(menuGroup);
    }

    public List<MenuGroupResponse> list() {
        return MenuGroupResponse.of(menuGroupRepository.findAll());
    }

    public MenuGroup findMenuGroupById(final Long id) {
        return menuGroupRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }
}

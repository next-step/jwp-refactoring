package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(MenuGroupRequest request) {
        return MenuGroupResponse.of(menuGroupRepository.save(new MenuGroup(request.getName())));
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        return MenuGroupResponse.list(menuGroupRepository.findAll());
    }
}

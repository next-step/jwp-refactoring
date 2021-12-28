package kitchenpos.tobe.menus.menugroup.application;

import java.util.List;
import kitchenpos.tobe.menus.menugroup.domain.MenuGroup;
import kitchenpos.tobe.menus.menugroup.domain.MenuGroupRepository;
import kitchenpos.tobe.menus.menugroup.dto.MenuGroupRequest;
import kitchenpos.tobe.menus.menugroup.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse register(final MenuGroupRequest request) {
        final MenuGroup menuGroup = menuGroupRepository.save(request.toMenuGroup());
        return MenuGroupResponse.of(menuGroup);
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        return MenuGroupResponse.ofList(menuGroupRepository.findAll());
    }
}

package kitchenpos.api.application.menugroup;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.common.domain.menugroup.MenuGroup;
import kitchenpos.common.domain.menugroup.MenuGroupRepository;
import kitchenpos.common.dto.menugroup.MenuGroupRequest;
import kitchenpos.common.dto.menugroup.MenuGroupResponse;
import kitchenpos.common.utils.StreamUtils;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest menuGroupRequest) {
        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroupRequest.toMenuGroup());
        return MenuGroupResponse.from(savedMenuGroup);
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        List<MenuGroup> menuGroups = menuGroupRepository.findAll();
        return StreamUtils.mapToList(menuGroups, MenuGroupResponse::from);
    }
}

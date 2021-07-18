package kitchenpos.menugroup.application;

import kitchenpos.menugroup.MenuGroup;
import kitchenpos.menugroup.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public MenuGroupResponse create(final MenuGroupRequest menuGroupRequest) {
        MenuGroup savedMenuGroup = menuGroupRepository.save(new MenuGroup(menuGroupRequest.getName()));
        return new MenuGroupResponse(savedMenuGroup.getId(), savedMenuGroup.getName());
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        return convertListToMenuGroupResponse(menuGroupRepository.findAll());
    }

    private List<MenuGroupResponse> convertListToMenuGroupResponse(List<MenuGroup> list) {
        return list.stream()
                .map(menuGroup -> new MenuGroupResponse(menuGroup.getId(), menuGroup.getName()))
                .collect(Collectors.toList());
    }
}

package kitchenpos.menugroup.application;

import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
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
    public MenuGroupResponse create(final MenuGroupRequest request) {
        MenuGroup savedMenuGroup = menuGroupRepository.save(request.toMenuGroup());
        return MenuGroupResponse.of(savedMenuGroup);
    }

    @Transactional
    public List<MenuGroupResponse> findAll() {
        return MenuGroupResponse.ofList(menuGroupRepository.findAll());
    }
}
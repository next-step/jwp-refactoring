package ktichenpos.menu.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.ui.request.MenuGroupRequest;
import kitchenpos.menu.ui.response.MenuGroupResponse;

@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest menuGroup) {
        MenuGroup entity = menuGroup.toEntity();
        return MenuGroupResponse.from(menuGroupRepository.save(entity));
    }

    public List<MenuGroupResponse> list() {
        return MenuGroupResponse.listFrom(menuGroupRepository.findAll());
    }
}

package kitchenpos.menugroup.application;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;

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
    public MenuGroup create(final MenuGroupRequest menuGroupRequest) {
        MenuGroup menuGroup = menuGroupRequest.toMenuGroup();
        return menuGroupRepository.save(menuGroup);
    }

    @Transactional(readOnly = true)
    public List<MenuGroup> list() {
        return menuGroupRepository.findAll();
    }

    @Transactional(readOnly = true)
    public MenuGroup findById(long id) {
        return menuGroupRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("메뉴그룹이 존재하지 않습니다."));
    }
}

package kitchenpos.menugroup.application;

import java.util.List;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest request) {
        MenuGroup persistMenuGroup = menuGroupRepository.save(request.toEntity());
        return MenuGroupResponse.from(persistMenuGroup);
    }

    public List<MenuGroupResponse> list() {
        List<MenuGroup> foundMenuGroups = menuGroupRepository.findAll();
        return MenuGroupResponse.from(foundMenuGroups);
    }

    public MenuGroup findById(Long id) {
        return menuGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(String.format("메뉴 그룹(%d)를 찾을 수 없습니다.", id)));
    }
}

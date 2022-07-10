package kitchenpos.menuGroup.application;

import kitchenpos.menuGroup.repository.MenuGroupRepository;
import kitchenpos.menuGroup.domain.MenuGroup;
import kitchenpos.menuGroup.domain.MenuGroups;
import kitchenpos.menuGroup.dto.MenuGroupCreateRequest;
import kitchenpos.menuGroup.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupCreateRequest request) {
        return MenuGroupResponse.from(
                menuGroupRepository.save(request.of())
        );
    }

    public List<MenuGroupResponse> listResponse() {
        return new MenuGroups(menuGroupRepository.findAll()).toResponse();
    }

    public MenuGroup getMenuGroup(final long id) {
        return menuGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + " 에 해당하는 메뉴 그룹을 찾을 수 없습니다."));
    }

    public MenuGroupResponse getMenuResponseGroup(final Long id) {
        return MenuGroupResponse.from(getMenuGroup(id));
    }
}

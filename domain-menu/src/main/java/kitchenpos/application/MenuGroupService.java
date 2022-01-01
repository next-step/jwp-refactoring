package kitchenpos.application;

import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import kitchenpos.domain.MenuGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest menuGroupRequest) {
        return MenuGroupResponse.from(menuGroupRepository.save(MenuGroup.from(menuGroupRequest.getName())));
    }

    public MenuGroup findMenuGroupById(Long id) {
        return menuGroupRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다."));
    }

    public List<MenuGroupResponse> list() {
        return menuGroupRepository.findAll()
                .stream()
                .map(MenuGroupResponse::from)
                .collect(Collectors.toList());
    }
}

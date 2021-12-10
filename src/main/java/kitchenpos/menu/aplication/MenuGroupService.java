package kitchenpos.menu.aplication;

import kitchenpos.menu.domain.menugroup.MenuGroup;
import kitchenpos.menu.domain.menugroup.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public MenuGroup findById(Long id) {
        return menuGroupRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다."));
    }

    public MenuGroupResponse saveMenuGroup(MenuGroupRequest menuGroupRequest) {
        return MenuGroupResponse.of(menuGroupRepository.save(menuGroupRequest.toMenuGroup()));
    }

    public List<MenuGroupResponse> findAllMenuGroup() {
        return menuGroupRepository.findAll()
                .stream()
                .map(MenuGroupResponse::of)
                .collect(Collectors.toList());
    }
}

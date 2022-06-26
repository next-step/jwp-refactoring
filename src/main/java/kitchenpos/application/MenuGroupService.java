package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest request) {
        MenuGroup persistedMenuGroup = menuGroupRepository.save(new MenuGroup(request.getName()));
        return MenuGroupResponse.of(persistedMenuGroup);
    }

    public List<MenuGroupResponse> list() {
        return menuGroupRepository.findAll().stream()
                .map(menuGroup -> MenuGroupResponse.of(menuGroup))
                .collect(Collectors.toList());
    }

    public MenuGroup findMenuGroupById(final Long id) {
        return menuGroupRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("메뉴 그룹을 찾을 수 없습니다. id: " + id));
    }
}

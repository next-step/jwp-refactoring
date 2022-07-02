package kitchenpos.menugroup.application;

import kitchenpos.common.domain.Name;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public MenuGroup findById(Long id) {
        return menuGroupRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    public List<MenuGroupResponse> list() {
        List<MenuGroup> groups = menuGroupRepository.findAll();

        return groups.stream().map(MenuGroupResponse::of).collect(Collectors.toList());
    }

    @Transactional
    public MenuGroupResponse create(MenuGroupRequest menuGroupRequest) {
        Name name = Name.of(menuGroupRequest.getName());
        MenuGroup menuGroup = MenuGroup.of(name);

        return MenuGroupResponse.of(menuGroupRepository.save(menuGroup));
    }
}

package menu.application;

import menu.repository.MenuGroupRepository;
import menu.domain.MenuGroup;
import menu.dto.MenuGroupRequest;
import menu.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest request) {
        MenuGroup menuGroup = menuGroupRepository.save(request.toMenuGroup());;
        return MenuGroupResponse.of(menuGroup);
    }

    public List<MenuGroupResponse> list() {
        return MenuGroupResponse.of(menuGroupRepository.findAll());
    }
}

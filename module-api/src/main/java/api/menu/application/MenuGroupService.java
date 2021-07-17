package api.menu.application;

import api.menu.application.exception.NotExistMenuGroupException;
import api.menu.dto.MenuGroupRequest;
import api.menu.dto.MenuGroupResponse;
import domain.menu.MenuGroup;
import domain.menu.MenuGroupRepository;
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
    public MenuGroupResponse create(final MenuGroupRequest menuGroupRequest) {
        MenuGroup menuGroup = menuGroupRepository.save(MenuGroup.of(menuGroupRequest.getName()));
        return MenuGroupResponse.of(menuGroup);
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> findMenuGroupResponses() {
        return MenuGroupResponse.ofList(menuGroupRepository.findAll());
    }

    @Transactional(readOnly = true)
    public MenuGroup findById(Long id) {
        return menuGroupRepository.findById(id).orElseThrow(NotExistMenuGroupException::new);
    }
}

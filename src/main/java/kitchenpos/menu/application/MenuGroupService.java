package kitchenpos.menu.application;

import kitchenpos.menu.application.exception.NotExistMenuGroupException;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.presentation.dto.MenuGroupRequest;
import kitchenpos.menu.presentation.dto.MenuGroupResponse;
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
    public List<MenuGroupResponse> list() {
        return MenuGroupResponse.ofList(menuGroupRepository.findAll());
    }

    @Transactional(readOnly = true)
    public MenuGroup findById(Long id){
        return menuGroupRepository.findById(id).orElseThrow(NotExistMenuGroupException::new);
    }
}

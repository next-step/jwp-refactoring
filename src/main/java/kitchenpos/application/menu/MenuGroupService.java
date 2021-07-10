package kitchenpos.application.menu;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.ui.dto.menu.MenuGroupRequest;
import kitchenpos.ui.dto.menu.MenuGroupResponse;
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

    public List<MenuGroupResponse> list() {
        return MenuGroupResponse.ofList(menuGroupRepository.findAll());
    }
}

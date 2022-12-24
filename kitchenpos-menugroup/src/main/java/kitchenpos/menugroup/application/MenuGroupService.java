package kitchenpos.menugroup.application;

import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.menugroup.port.MenuGroupPort;
import kitchenpos.menugroup.domain.MenuGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MenuGroupService {
    private final MenuGroupPort menuGroupPort;

    public MenuGroupService(final MenuGroupPort menuGroupPort) {
        this.menuGroupPort = menuGroupPort;
    }

    public MenuGroupResponse create(final MenuGroupRequest request) {
        MenuGroup menuGroup = menuGroupPort.save(new MenuGroup(request.getName()));

        return MenuGroupResponse.from(menuGroup);
    }

    @Transactional(readOnly = true)
    public List<MenuGroup> list() {
        return menuGroupPort.findAll();
    }
}

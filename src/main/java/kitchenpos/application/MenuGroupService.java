package kitchenpos.application;

import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import kitchenpos.port.MenuGroupPort;
import kitchenpos.domain.MenuGroup;
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
        MenuGroup menuGroup = menuGroupPort.save(MenuGroup.from(request.getName()));

        return MenuGroupResponse.from(menuGroup);
    }

    @Transactional(readOnly = true)
    public List<MenuGroup> list() {
        return menuGroupPort.findAll();
    }
}

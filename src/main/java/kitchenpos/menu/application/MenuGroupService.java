package kitchenpos.menu.application;

import java.util.List;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.ui.request.MenuGroupRequest;
import kitchenpos.menu.ui.response.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(MenuGroupRequest request) {
        return MenuGroupResponse.from(menuGroupRepository.save(request.toEntity()));
    }

    public List<MenuGroupResponse> list() {
        return MenuGroupResponse.listFrom(menuGroupRepository.findAll());
    }

    public MenuGroup findById(long id) {
        return menuGroupRepository.findById(id)
            .orElseThrow(
                () -> new NotFoundException(String.format("menu group id(%d) is not exist", id)));
    }
}

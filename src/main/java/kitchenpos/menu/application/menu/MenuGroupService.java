package kitchenpos.menu.application.menu;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.menu.MenuGroupRepository;
import kitchenpos.menu.dto.menu.MenuGroupRequest;
import kitchenpos.menu.dto.menu.MenuGroupResponse;

@Transactional
@Service
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public MenuGroupResponse create(final MenuGroupRequest menuGroupRequest) {
        return MenuGroupResponse.of(menuGroupRepository.save(menuGroupRequest.toMenuGroup()));
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> findAll() {
        return MenuGroupResponse.ofList(menuGroupRepository.findAll());
    }

}

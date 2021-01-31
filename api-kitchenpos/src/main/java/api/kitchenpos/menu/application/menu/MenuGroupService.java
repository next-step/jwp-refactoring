package api.kitchenpos.menu.application.menu;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import api.kitchenpos.menu.dto.menu.MenuGroupRequest;
import api.kitchenpos.menu.dto.menu.MenuGroupResponse;
import domain.kitchenpos.menu.menu.MenuGroupRepository;

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

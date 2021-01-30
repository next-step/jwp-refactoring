package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public MenuGroupResponse create(final MenuGroupRequest menuGroupRequest) {
        return MenuGroupResponse.of(this.menuGroupRepository.save(menuGroupRequest.toMenuGroup()));
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        return this.menuGroupRepository.findAll().stream()
                .map(MenuGroupResponse::of).collect(Collectors.toList());
    }
}

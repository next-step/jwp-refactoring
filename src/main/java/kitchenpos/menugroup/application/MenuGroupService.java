package kitchenpos.menugroup.application;

import java.util.stream.Collectors;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public MenuGroupResponse create(final MenuGroupRequest menuGroupRequest) {
        return MenuGroupResponse.of(menuGroupRepository.save(menuGroupRequest.toMenuGroup()));
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        return menuGroupRepository.findAll()
                                  .stream()
                                  .map(MenuGroupResponse::of)
                                  .collect(Collectors.toList());
    }
}

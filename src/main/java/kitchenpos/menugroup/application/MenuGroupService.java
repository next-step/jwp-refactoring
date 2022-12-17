package kitchenpos.menugroup.application;

import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupCreateRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse createMenuGroup(final MenuGroupCreateRequest request) {
        return MenuGroupResponse.of(menuGroupRepository.save(request.toMenuGroup()));
    }

    public List<MenuGroupResponse> findAllMenuGroups() {
        return MenuGroupResponse.list(menuGroupRepository.findAll());
    }
}

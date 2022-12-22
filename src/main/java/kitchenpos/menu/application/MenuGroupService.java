package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.response.MenuGroupResponse;
import kitchenpos.exception.ErrorCode;
import kitchenpos.exception.KitchenposException;
import kitchenpos.menu.domain.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuGroupService {
    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public MenuGroupResponse create(final MenuGroup entity) {
        MenuGroup menuGroup = menuGroupRepository.save(entity);
        return MenuGroupResponse.of(menuGroup.getId(), menuGroup.getName());
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        return menuGroupRepository.findAll().stream()
                .map(MenuGroupResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MenuGroup findById(Long menuGroupId){
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new KitchenposException(ErrorCode.NOT_EXISTS_MENU_GROUP));
    }
}

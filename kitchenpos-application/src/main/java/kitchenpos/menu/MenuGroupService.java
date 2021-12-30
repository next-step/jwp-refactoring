package kitchenpos.menu;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.dto.menu.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroup menuGroup) {
        return MenuGroupResponse.from(menuGroupRepository.save(menuGroup));
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponse> list() {
        return menuGroupRepository.findAll()
            .stream()
            .map(MenuGroupResponse::from)
            .collect(toList());
    }
}

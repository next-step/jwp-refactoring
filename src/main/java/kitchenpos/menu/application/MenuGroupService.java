package kitchenpos.menu.application;

import java.util.*;
import java.util.stream.*;

import org.springframework.stereotype.*;

import kitchenpos.common.*;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.*;
import kitchenpos.menu.repository.*;

@Service
public class MenuGroupService {
    public static final String MENU_GROUP = "메뉴그룹";

    public final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public MenuGroup findById(Long id) {
        return menuGroupRepository.findById(id).orElseThrow(() -> new NotFoundException(MENU_GROUP));
    }

    public MenuGroupResponse save(MenuGroupRequest menuGroupRequest) {
        MenuGroup menuGroup = MenuGroup.from(menuGroupRequest.getName());
        return MenuGroupResponse.from(menuGroupRepository.save(menuGroup));
    }

    public List<MenuGroupResponse> findAll() {
        return menuGroupRepository.findAll()
            .stream()
            .map(MenuGroupResponse::from)
            .collect(Collectors.toList());
    }
}

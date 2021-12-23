package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.dao.MenuGroupDao;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MenuGroupService {

    private static final String ERROR_MESSAGE_NOT_EXIST_MENU_GROUP = "메뉴 그룹이 존재하지 않습니다.";

    private final MenuGroupDao menuGroupDao;


    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroupResponse create(final MenuGroupRequest menuGroup) {
        MenuGroup saveMenuGroup = menuGroupDao.save(menuGroup.toMenuGroup());
        return MenuGroupResponse.from(saveMenuGroup);
    }

    public List<MenuGroupResponse> list() {
        return menuGroupDao.findAll()
            .stream()
            .map(MenuGroupResponse::from)
            .collect(Collectors.toList());
    }

    public MenuGroup findMenuGroupById(Long id) {
        return menuGroupDao.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(ERROR_MESSAGE_NOT_EXIST_MENU_GROUP));
    }
}

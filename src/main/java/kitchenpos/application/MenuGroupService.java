package kitchenpos.application;

import kitchenpos.advice.exception.MenuGroupBadRequestException;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuGroupService {
    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroup create(final MenuGroup menuGroup) {
        return menuGroupDao.save(menuGroup);
    }

    public List<MenuGroup> list() {
        return menuGroupDao.findAll();
    }

    public MenuGroup findMenuGroupById(Long id) {
        return menuGroupDao.findById(id).orElseThrow(() -> new MenuGroupBadRequestException("존재하는 메뉴그룹이 없습니다",id));
    }
}

package kitchenpos.application;

import java.util.stream.Collectors;
import kitchenpos.domain.model.MenuGroup;
import kitchenpos.dto.MenuGroupCreateRequest;
import kitchenpos.dto.MenuGroupDto;
import kitchenpos.domain.repository.MenuGroupDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MenuGroupService {

    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    public MenuGroupDto create(final MenuGroupCreateRequest menuGroup) {
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup.toEntity());
        return MenuGroupDto.of(savedMenuGroup);
    }

    @Transactional(readOnly = true)
    public List<MenuGroupDto> list() {
        List<MenuGroup> menuGroups = menuGroupDao.findAll();
        return menuGroups.stream()
                .map(MenuGroupDto::of)
                .collect(Collectors.toList());
    }
}

package kitchenpos.menugroup.application;

import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponseDto;
import kitchenpos.menugroup.repository.MenuGroupDao;
import kitchenpos.menugroup.domain.MenuGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class MenuGroupService {
    private final MenuGroupDao menuGroupDao;

    public MenuGroupService(final MenuGroupDao menuGroupDao) {
        this.menuGroupDao = menuGroupDao;
    }

    @Transactional
    public MenuGroupResponseDto create(final MenuGroupRequest menuGroupRequest) {
        MenuGroup menuGroup = menuGroupDao.save(new MenuGroup(menuGroupRequest.getName()));
        return MenuGroupResponseDto.of(menuGroup);
    }

    public MenuGroupResponse list() {
        return MenuGroupResponse.of(menuGroupDao.findAll().stream()
                .map(MenuGroupResponseDto::of)
                .collect(Collectors.toList()));
    }
}

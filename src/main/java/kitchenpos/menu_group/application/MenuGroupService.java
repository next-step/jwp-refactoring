package kitchenpos.menu_group.application;

import kitchenpos.menu_group.domain.MenuGroup;
import kitchenpos.menu_group.domain.MenuGroupRepository;
import kitchenpos.menu_group.dto.MenuGroupRequestDto;
import kitchenpos.menu_group.dto.MenuGroupResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroupResponseDto create(final MenuGroupRequestDto request) {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup(request.getName()));
        return new MenuGroupResponseDto(menuGroup);
    }

    @Transactional(readOnly = true)
    public List<MenuGroupResponseDto> list() {
        List<MenuGroup> list = menuGroupRepository.findAll();
        return list.stream()
                .map(MenuGroupResponseDto::new)
                .collect(Collectors.toList());
    }
}

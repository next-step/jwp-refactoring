package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupRequestDto;
import kitchenpos.menu.dto.MenuGroupResponseDto;
import kitchenpos.menu.repository.MenuGroupRepository;
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

package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupRequestDto;
import kitchenpos.dto.MenuGroupResponseDto;
import kitchenpos.repository.MenuGroupRepository;
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

    public List<MenuGroupResponseDto> list() {
        List<MenuGroup> list = menuGroupRepository.findAll();
        return list.stream()
                .map(MenuGroupResponseDto::new)
                .collect(Collectors.toList());
    }
}

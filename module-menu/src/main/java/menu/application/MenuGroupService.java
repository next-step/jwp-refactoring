package menu.application;

import java.util.List;
import java.util.stream.Collectors;
import menu.domain.MenuGroup;
import menu.dto.MenuGroupRequestDto;
import menu.dto.MenuGroupResponseDto;
import menu.repository.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

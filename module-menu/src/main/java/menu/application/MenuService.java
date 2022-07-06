package menu.application;

import java.util.List;
import java.util.stream.Collectors;
import menu.domain.Menu;
import menu.domain.MenuGroup;
import menu.dto.MenuRequestDto;
import menu.dto.MenuResponseDto;
import menu.repository.MenuGroupRepository;
import menu.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuValidator menuValidator;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository, MenuValidator menuValidator) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuValidator = menuValidator;
    }

    @Transactional
    public MenuResponseDto create(final MenuRequestDto request) {
        MenuGroup menuGroup = getMenuGroup(request.getMenuGroupId());
        menuValidator.validate(request);
        Menu menu = menuRepository.save(request.toEntity(menuGroup));
        return new MenuResponseDto(menu);
    }

    private MenuGroup getMenuGroup(Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId).orElseThrow(IllegalArgumentException::new);
    }

    @Transactional(readOnly = true)
    public List<MenuResponseDto> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuResponseDto::new)
                .collect(Collectors.toList());
    }
}

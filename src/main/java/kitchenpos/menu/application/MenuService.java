package kitchenpos.menu.application;

import kitchenpos.dto.MenuProductRequestDto;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.dto.MenuRequestDto;
import kitchenpos.menu.dto.MenuResponseDto;
import kitchenpos.menu_group.domain.MenuGroup;
import kitchenpos.menu_group.repository.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    private MenuProduct getMenuProduct(MenuProductRequestDto menuProductRequest) {
        return new MenuProduct(menuProductRequest.getProductId(), menuProductRequest.getQuantity());
    }

    @Transactional(readOnly = true)
    public List<MenuResponseDto> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuResponseDto::new)
                .collect(Collectors.toList());
    }
}

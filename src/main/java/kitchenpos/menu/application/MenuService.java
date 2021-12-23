package kitchenpos.menu.application;

import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final MenuProductService menuProductService;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupService menuGroupService,
            final MenuProductService menuProductService
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.menuProductService = menuProductService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        Menu savedMenu = menuRepository.save(menuRequest.toMenu(getMenuGroup(menuRequest)));
        MenuProducts menuProducts = menuProductService.allSave(menuRequest.getMenuProducts(), savedMenu);
        return MenuResponse.of(savedMenu, menuProducts.getMenuProducts());
    }

    private MenuGroup getMenuGroup(MenuRequest menuRequest) {
        return menuGroupService.findById(menuRequest.getMenuGroupId());
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(this::getMenuResponse)
                .collect(Collectors.toList());
    }

    private MenuResponse getMenuResponse(Menu menu) {
        List<MenuProduct> menuProducts = menuProductService.findAllByMenu(menu);
        return MenuResponse.of(menu, menuProducts);
    }
}

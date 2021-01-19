package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menuGroup.domain.MenuGroup;
import kitchenpos.menuGroup.domain.MenuGroupRepository;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;

    public MenuService(MenuRepository menuRepository,
                       MenuGroupRepository menuGroupRepository,
                       MenuProductRepository menuProductRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
    }

    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId()).orElseThrow(IllegalArgumentException::new);
        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup);
        menu.validationCheck();

        final Menu savedMenu = menuRepository.save(menu);

        for (final MenuProduct menuProduct : menuRequest.getMenuProducts()) {
            menuProduct.addMenu(savedMenu);
            menuProductRepository.save(menuProduct);
        }
        savedMenu.checkPrice();

        return MenuResponse.of(savedMenu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream().map(menu ->MenuResponse.of(menu))
                .collect(Collectors.toList());
    }
}

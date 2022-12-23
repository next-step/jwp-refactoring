package kitchenpos.menu.application;

import kitchenpos.ExceptionMessage;
import kitchenpos.common.Price;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.application.MenuProductService;
import kitchenpos.product.domain.*;
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
        final MenuGroup menuGroup = menuGroupService.findById(menuRequest.getMenuGroupId());
        final Price price = new Price(menuRequest.getPrice());
        final Menu menu = new Menu(menuRequest.getName(), price, menuGroup);
        final Menu savedMenu = menuRepository.save(menu);
        final MenuProducts menuProducts = menuProductService.createMenuProducts(menuRequest.getMenuProducts(),
                price, savedMenu.getId());
        return MenuResponse.of(savedMenu, menuProducts);
    }

    public List<MenuResponse> findAll() {
        return menuRepository.findAll()
                .stream()
                .map(menu -> MenuResponse.of(menu, menuProductService.getMenuProductByMenuId(menu.getId())))
                .collect(Collectors.toList());
    }

    public Menu findById(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.NOT_EXIST_MENU.getMessage()));
    }
}

package kitchenpos.menu.application;

import kitchenpos.ExceptionMessage;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final MenuProductValidator menuProductValidator;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupService menuGroupService,
            final MenuProductValidator menuProductValidator
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.menuProductValidator = menuProductValidator;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        final MenuGroup menuGroup = menuGroupService.findById(menuRequest.getMenuGroupId());
        final BigDecimal price = menuRequest.getPrice();
        final Menu menu = new Menu(menuRequest.getName(), new Price(price), menuGroup);
        final Menu savedMenu = menuRepository.save(menu);
        final MenuProducts menuProducts = menuProductValidator
                .createMenuProducts(savedMenu.getId(), savedMenu.getPrice(), menuRequest.getMenuProducts());
        return MenuResponse.of(savedMenu, menuProducts);
    }

    public List<MenuResponse> findAll() {
        return menuRepository.findAll()
                .stream()
                .map(menu -> MenuResponse.of(menu, menuProductValidator.getMenuProductByMenuId(menu.getId())))
                .collect(Collectors.toList());
    }

    public Menu findById(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.NOT_EXIST_MENU.getMessage()));
    }
}

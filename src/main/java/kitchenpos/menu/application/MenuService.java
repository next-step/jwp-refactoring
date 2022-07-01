package kitchenpos.menu.application;

import kitchenpos.menu.dao.*;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.Menus;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.product.application.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;

    private final MenuProductRepository menuProductRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupService menuGroupService,
            final MenuProductRepository menuProductRepository,
            final ProductService productService
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.menuProductRepository = menuProductRepository;
        this.productService = productService;
    }

    @Transactional
    public Menu create(final MenuCreateRequest request) {
        MenuGroup menuGroup = menuGroupService.getMenuGroup(request.getMenuGroupId());
        Menu menu = request.of(menuGroup);
        MenuProducts menuProducts = request.convertMenuProducts(menuProductRequest -> {
            return menuProductRequest.of(menu, productService.getProduct(menuProductRequest.getProductId()));
        });
        menu.addMenuProducts(menuProducts);

        menu.validateMenuAndProductTotalPrice();

        return menuRepository.save(menu);
    }

    public Menus list() {
        return new Menus(menuRepository.findAll());
    }

    public Menus findMenusInIds(final List<Long> ids) {
        return new Menus(menuRepository.findAllById(ids));
    }
}

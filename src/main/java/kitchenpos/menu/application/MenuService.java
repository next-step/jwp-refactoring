package kitchenpos.menu.application;

import kitchenpos.menu.dao.MenuRepository;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.Menus;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupService menuGroupService,
            final ProductService productService
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    @Transactional
    public Menu create(final MenuCreateRequest request) {
        MenuGroup menuGroup = menuGroupService.getMenuGroup(request.getMenuGroupId());
        Menu menu = request.of(menuGroup);
        MenuProducts menuProducts = request.convertMenuProducts(menuProductRequest -> {
            Product product = productService.getProduct(menuProductRequest.getProductId());
            return menuProductRequest.of(menu, product);
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

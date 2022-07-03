package kitchenpos.menu.application;

import kitchenpos.menu.dao.MenuRepository;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
        MenuProducts menuProducts = convertMenuProductsByRequest(request);
        Menu menu = request.of(menuGroup, menuProducts);

        return menuRepository.save(menu);
    }

    public Menus list() {
        return new Menus(menuRepository.findAll());
    }

    public Menus findMenusInIds(final List<Long> ids) {
        return new Menus(menuRepository.findAllById(ids));
    }


    private MenuProducts convertMenuProductsByRequest(final MenuCreateRequest menuProductRequests) {
        List<MenuProduct> menuProducts = menuProductRequests.getMenuProducts()
                .stream()
                .map(request -> {
                    Product product = productService.getProduct(request.getProductId());
                    return new MenuProduct(product, new Quantity(request.getQuantity()));
                })
                .collect(Collectors.toList());

        return new MenuProducts(menuProducts);
    }
}

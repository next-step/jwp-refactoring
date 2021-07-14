package kitchenpos.menu.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.exception.MenuNotFoundException;
import kitchenpos.product.service.ProductService;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final ProductService productService;
    private final MenuGroupService menuGroupService;

    public MenuService(MenuRepository menuRepository, ProductService productService,
        MenuGroupService menuGroupService) {
        this.menuRepository = menuRepository;
        this.productService = productService;
        this.menuGroupService = menuGroupService;
    }

    @Transactional
    public Menu create(final MenuRequest request) {
        MenuGroup menuGroup = menuGroupService.findById(request.getMenuGroupId());
        MenuProducts menuProducts = MenuProducts.of(request.getMenuProducts()
            .stream()
            .map(this::newMenuProduct)
            .collect(Collectors.toList()));

        Menu menu = new Menu(request.getName(), request.price(), menuProducts);
        menuGroup.add(menu);
        return menuRepository.save(menu);
    }

    private MenuProduct newMenuProduct(MenuProductRequest menuProductRequest) {
        Product product = productService.findById(menuProductRequest.getProductId());
        return new MenuProduct(product, menuProductRequest.quantity());
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }

    public Menu findById(Long id) {
        return menuRepository.findById(id)
            .orElseThrow(() -> new MenuNotFoundException("해당 ID의 메뉴가 존재하지 않습니다."));
    }
}

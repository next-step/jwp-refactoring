package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;

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
        List<MenuProduct> menuProducts = request.getMenuProducts()
            .stream()
            .map(this::getMenuProduct)
            .collect(Collectors.toList());

        return menuRepository.save(new Menu(request.getName(), request.getPrice(), menuGroup, menuProducts));
    }

    private MenuProduct getMenuProduct(MenuProductRequest menuProductRequest) {
        Product product = productService.findById(menuProductRequest.getProductId());
        return new MenuProduct(product, menuProductRequest.getQuantity());
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}

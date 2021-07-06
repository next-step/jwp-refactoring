package kitchenpos.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
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
        Menu menu = new Menu(request.getName(), menuGroup);
        request.getMenuProducts()
            .forEach(menuProductRequest -> {
                Product product = productService.findById(menuProductRequest.getProductId());
                menu.addMenuProduct(new MenuProduct(product, menuProductRequest.getQuantity()));
            });

        return menuRepository.save(menu.withPrice(request.getPrice()));
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}

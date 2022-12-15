package kitchenpos.menu.application;

import java.util.stream.Collectors;
import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {
    private final MenuGroupService menuGroupService;
    private final MenuRepository menuRepository;
    private final ProductService productService;

    public MenuService(
            MenuRepository menuRepository,
            ProductService productService,
            MenuGroupService menuGroupService
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupService.findMenuGroupById(menuRequest.getMenuGroupId());
        List<MenuProduct> menuProducts = menuRequest.getMenuProductRequests()
                .stream()
                .map(this::createMenuProduct)
                .collect(Collectors.toList());
        Menu menu = Menu.of(menuRequest.getName(), menuRequest.getPrice(), menuGroup, MenuProducts.of(menuProducts));
        return MenuResponse.of(menuRepository.save(menu));
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                .stream()
                .map(MenuResponse::of)
                .collect(Collectors.toList());
    }

    private MenuProduct createMenuProduct(MenuProductRequest menuProductRequest){
        Product product = productService.findProductById(menuProductRequest.getProductId());
        return MenuProduct.of(product, menuProductRequest.getQuantity());
    }
}

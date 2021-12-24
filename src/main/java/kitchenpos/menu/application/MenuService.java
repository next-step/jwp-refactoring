package kitchenpos.menu.application;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Quantity;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;

    public MenuService(
            MenuRepository menuRepository
            , MenuGroupService menuGroupService
            , ProductService productService
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        Name name = Name.of(request.getName());
        Price price = Price.of(request.getPrice());
        MenuGroup menuGroup = menuGroupService.findById(request.getMenuGroupId());
        MenuProducts menuProducts = MenuProducts.of(toMenuProducts(request.getMenuProducts()));

        Menu menu = Menu.of(name, price, menuGroup, menuProducts);
        Menu persistMenu = menuRepository.save(menu);
        return MenuResponse.of(persistMenu);
    }

    public List<MenuResponse> list() {
        final List<Menu> persistMenus = menuRepository.findAll();

        return MenuResponse.fromList(persistMenus);

    }

    public Menu findById(Long menuId) {
        return menuRepository.findById(menuId)
                .orElseThrow(NoSuchElementException::new);
    }

    private List<MenuProduct> toMenuProducts(List<MenuProductRequest> requests) {
        Map<Long, Long> menuProductRequests = requests.stream().collect(
                Collectors.toMap(MenuProductRequest::getProductId, MenuProductRequest::getQuantity));
        List<Long> productIds = new ArrayList<>(menuProductRequests.keySet());

        List<Product> products = productService.findByIdIn(productIds);
        return products.stream()
                .map(product -> {
                    Long quantity = menuProductRequests.get(product.getId());
                    return MenuProduct.of(product, Quantity.of(quantity));
                })
                .collect(Collectors.toList());
    }
}

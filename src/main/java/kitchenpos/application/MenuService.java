package kitchenpos.application;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu2;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.MenuRequest;
import kitchenpos.ui.dto.MenuResponse;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupService menuGroupService;
    private final ProductService productService;


    public MenuService(MenuRepository menuRepository, MenuGroupService menuGroupService, ProductService productService) {
        this.menuRepository = menuRepository;
        this.menuGroupService = menuGroupService;
        this.productService = productService;
    }

    @Transactional
    public MenuResponse create(MenuRequest menuRequest) {
        Menu2 menu = toMenu(menuRequest);
        return create(menu);
    }

    @Transactional
    public MenuResponse create(Menu2 menu) {
        menu.validatePrice();
        return MenuResponse.of(menuRepository.save(menu));
    }

    public List<MenuResponse> list() {
        return MenuResponse.of(findAll());
    }

    public List<Menu2> findAll() {
        return menuRepository.findAll();
    }

    private Menu2 toMenu(MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupService.findById(menuRequest.getMenuGroupId());

        List<Product> products = productService.findAllById(menuRequest.getProductsId());

        return menuRequest.toMenu(menuGroup, multiplyQuantity(menuRequest, products));
    }

    private Map<Product, Integer> multiplyQuantity(MenuRequest menuRequest, List<Product> products) {
        Map<Long, Integer> productsCount = menuRequest.getProducts();

        return products.stream()
            .collect(Collectors.toMap(
                Function.identity(),
                product -> productsCount.get(product.getId()),
                Integer::sum
            ));
    }


}

package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
public class MenuService {
    private static final int FIRST_INDEX = 0;
    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;
    private final MenuGroupService menuGroupService;

    public MenuService(
            final MenuRepository menuRepository,
            final ProductRepository productRepository,
            final MenuGroupService menuGroupService
    ) {
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
        this.menuGroupService = menuGroupService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        MenuGroup menuGroup = menuGroupService.findById(request.getMenuGroupId());

        List<Product> products = getProducts(request);

        Map<Long, Product> productMap = listToMap(products);

        List<MenuProduct> menuProducts = request.getMenuProductRequests().stream()
                .map(it -> MenuProduct.of(productMap.get(it.getProductId()), it.getQuantity()))
                .collect(Collectors.toList());

        Menu menu = Menu.of(request.getName(), request.getPrice(), menuGroup, menuProducts);
        Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.of(savedMenu);
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::of)
                .collect(toList());
    }

    private List<Product> getProducts(MenuRequest request) {
        List<Long> productIds = request.getMenuProductRequests().stream()
                .map(MenuProductRequest::getProductId)
                .collect(toList());

        List<Product> products = productRepository.findByIdIn(productIds);
        if (products.size() != request.getMenuProductRequests().size()) {
            throw new IllegalArgumentException("등록되지 않은 상품이 있습니다.");
        }

        return products;
    }

    private Map<Long, Product> listToMap(List<Product> products) {
        return products.stream().collect(
                Collectors.toMap(Product::getId, Function.identity()));
    }
}

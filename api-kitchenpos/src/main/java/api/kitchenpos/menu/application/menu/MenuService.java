package api.kitchenpos.menu.application.menu;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import api.kitchenpos.menu.dto.menu.MenuProductRequest;
import api.kitchenpos.menu.dto.menu.MenuRequest;
import api.kitchenpos.menu.dto.menu.MenuResponse;
import domain.kitchenpos.menu.menu.Menu;
import domain.kitchenpos.menu.menu.MenuGroup;
import domain.kitchenpos.menu.menu.MenuGroupRepository;
import domain.kitchenpos.menu.menu.MenuProduct;
import domain.kitchenpos.menu.menu.MenuProducts;
import domain.kitchenpos.menu.menu.MenuRepository;
import domain.kitchenpos.menu.product.Product;
import domain.kitchenpos.menu.product.ProductRepository;

@Transactional
@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public MenuResponse create(final MenuRequest menuRequest) {
        final MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
            .orElseThrow(IllegalArgumentException::new);
        final List<Product> products = productRepository.findAllById(menuRequest.getProductIds());

        final Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup);
        menuRepository.save(menu);

        menu.addMenuProducts(new MenuProducts(toMenuProducts(menu, products,
            menuRequest.getMenuProductRequests())));

        return MenuResponse.of(menu);
    }

    private List<MenuProduct> toMenuProducts(final Menu menu, final List<Product> products,
        final List<MenuProductRequest> menuProductRequests) {
            Map<Long, Product> productById = getProductMap(products);

            validateProductsRequestExists(productById, menuProductRequests);

            return menuProductRequests.stream()
                .filter(menuProduct -> productById.containsKey(menuProduct.getProductId()))
                .map(menuProduct -> {
                    Product product = productById.get(menuProduct.getProductId());
                    return new MenuProduct(menu, product, menuProduct.getQuantity());
                })
                .collect(Collectors.toList());
    }

    private Map<Long, Product> getProductMap(final List<Product> products) {
        return products.stream()
            .collect(Collectors.toMap(Product::getId, Function.identity()));
    }

    private void validateProductsRequestExists(final Map<Long, Product> productById,
        final List<MenuProductRequest> menuProductRequests) {
        menuProductRequests.stream()
            .filter(menuProductRequest -> !productById.containsKey(menuProductRequest.getProductId()))
            .findAny()
            .ifPresent(menuProductRequest -> {
                throw new IllegalArgumentException(String.format("요청한 %d에 대한 상품 정보가 없습니다.",
                    menuProductRequest.getProductId()));
            });
    }

    public List<MenuResponse> findAll() {
        return MenuResponse.ofList(menuRepository.findAll());
    }

}

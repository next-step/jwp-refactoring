package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MenuMapper {
    private ProductRepository productRepository;

    public MenuMapper(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Menu mapFrom(MenuRequest request) {
        return new Menu(
                request.getName(),
                request.getPrice(),
                request.getMenuGroupId(),
                getMenuProducts(request));
    }

    private MenuProducts getMenuProducts(MenuRequest request) {
        List<Product> products = getProducts(getProductIds(request));
        List<MenuProduct> menuProducts = request.getMenuProducts().stream()
                .map(p -> new MenuProduct(
                        getProduct(products, p.getProductId()),
                        getQuantity(request.getMenuProducts(), p.getProductId())))
                .collect(Collectors.toList());

        return new MenuProducts(menuProducts);

    }

    private List<Long> getProductIds(MenuRequest request) {
        return request.getMenuProducts()
                .stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());
    }


    private List<Product> getProducts(List<Long> productIds) {
        return productRepository.findByIdIn(productIds);
    }


    private Product getProduct(List<Product> products, Long productId) {
        return products.stream()
                .filter(p -> p.getId().equals(productId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private long getQuantity(List<MenuProductRequest> requests, Long productId) {
        return requests.stream()
                .filter(r -> r.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .getQuantity();
    }


}

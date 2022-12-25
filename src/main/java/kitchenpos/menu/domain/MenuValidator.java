package kitchenpos.menu.domain;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MenuValidator {

    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuValidator(ProductRepository productRepository, MenuGroupRepository menuGroupRepository) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    public void validProductsAndPrice(MenuRequest request) {
        validProduct(request);
        List<Long> productIds = mapToProductIds(request);
        List<Product> products = mapToProducts(productIds);
        BigDecimal total = totalSumPrice(products);
        validateSumOfPrice(request.getPrice(), total);
    }

    private static void validateSumOfPrice(BigDecimal menuPrice, BigDecimal total) {
        if (menuPrice.compareTo(total) > 0) {
            throw new IllegalArgumentException("상품들 금액의 합이 메뉴 가격보다 클 수 없습니다.");
        }
    }

    private BigDecimal totalSumPrice(List<Product> products) {
        BigDecimal total = new BigDecimal(0);
        for (Product product : products) {
            total = total.add(product.getPrice());
        }
        return total;
    }

    private List<Long> mapToProductIds(MenuRequest menuRequest) {
        return menuRequest.getMenuProducts()
            .stream()
            .map(MenuProductRequest::getProductId)
            .collect(Collectors.toList());
    }

    private List<Product> mapToProducts(List<Long> productsIds) {
        return productRepository.findAllByIdIn(productsIds);
    }

    private void validProduct(MenuRequest request) {
        for (MenuProductRequest menuProductRequest : request.getMenuProducts()) {
            productRepository.findById(menuProductRequest.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다. id=" + menuProductRequest.getProductId()));
        }
    }

    public void validateMenuGroup(MenuRequest request) {
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
    }
}

package kitchenpos.menus.menu.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menus.menu.domain.Price;
import kitchenpos.menus.menu.dto.MenuProductRequest;
import kitchenpos.menus.menu.dto.MenuRequest;
import kitchenpos.menus.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {
    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuValidator(ProductRepository productRepository, MenuGroupRepository menuGroupRepository) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    public void validate(MenuRequest request) {
        validateExistingMenuGroup(request);
        validateExistingProduct(request);
        validateAmount(request);

    }

    private void validateExistingProduct(MenuRequest request) {
        List<MenuProductRequest> menuProducts = request.getMenuProductRequests();
        List<Product> findProducts = findAllProductsInMenuProduct(menuProducts);

        if (menuProducts.size() != findProducts.size()) {
            throw new IllegalArgumentException("시스템에 등록되어 있지 않은 메뉴 상품이 있습니다.");
        }
    }

    private void validateExistingMenuGroup(MenuRequest request) {
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new NoSuchElementException("메뉴그룹이 시스템에 존재 하지 않습니다");
        }
    }

    private void validateAmount(MenuRequest request) {
        List<MenuProductRequest> menuProducts = request.getMenuProductRequests();

        List<Product> findProducts = findAllProductsInMenuProduct(menuProducts);
        Map<Long, Price> productIdPrices = makeProductIdPriceMap(findProducts);

        Price total = calculateTotalAmount(menuProducts, productIdPrices);

        if (new Price(request.getPrice())
                .greaterThan(total)) {
            throw new IllegalArgumentException("상품 가격의 합계 보다 비싼 메뉴 가격을 추가 할 수 업습니다.");
        }
    }

    private List<Product> findAllProductsInMenuProduct(List<MenuProductRequest> menuProducts) {
        List<Long> productIds = getProductIds(menuProducts);
        return findAllByProductsIn(productIds);
    }

    private List<Product> findAllByProductsIn(List<Long> productIds) {
        return productRepository.findAllByIdIsIn(productIds);
    }

    private Price calculateTotalAmount(List<MenuProductRequest> menuProducts, Map<Long, Price> productIdPrices) {
        return menuProducts.stream()
                .map(menuProduct -> calculateAmount(productIdPrices.get(menuProduct.getProductId()),
                        menuProduct.getQuantity()))
                .reduce(new Price(BigDecimal.ZERO), Price::add);
    }

    private List<Long> getProductIds(List<MenuProductRequest> menuProducts) {
        return menuProducts.stream()
                .map(MenuProductRequest::getProductId)
                .collect(Collectors.toList());
    }

    private Map<Long, Price> makeProductIdPriceMap(List<Product> findProducts) {
        return findProducts.
                stream().
                collect(Collectors.toMap(Product::getId, product -> new Price(product.getPriceLong())));
    }

    public Price calculateAmount(Price price, Long quantity) {
        return price.multiply(new Quantity(quantity));
    }
}

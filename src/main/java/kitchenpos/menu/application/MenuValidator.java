package kitchenpos.menu.application;

import kitchenpos.common.domain.Price;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Product;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

import static java.util.stream.Collectors.toList;

@Service
public class MenuValidator {

    private final ProductRepository productRepository;

    public MenuValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validate(MenuRequest request) {
        List<MenuProduct> menuProducts = request.getMenuProducts();

        List<Product> products = getProducts(menuProducts);

        BigDecimal sum = products.stream()
                .map(product -> product.getPrice().multiply(BigDecimal.valueOf(getQuantity(menuProducts, product))))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Price price = new Price(request.getPrice());
        if (price.isGatherThan(sum)) {
            throw new IllegalArgumentException("메뉴의 가격은 전체 상품의 가격의 합보다 작거나 같아야합니다.");
        }
    }

    private List<Product> getProducts(List<MenuProduct> menuProducts) {
        List<Long> productIds = menuProducts.stream()
                .map(MenuProduct::getProductId)
                .collect(toList());

        List<Product> products = productRepository.findByIdIn(productIds);
        if (products.size() != menuProducts.size()) {
            throw new IllegalArgumentException("존재하지 않는 메뉴상품이 있습니다.");
        }
        return products;
    }

    private long getQuantity(List<MenuProduct> menuProducts, Product product) {
        return menuProducts.stream()
                .filter(menuProduct -> menuProduct.getProductId().equals(product.getId()))
                .findFirst()
                .orElseThrow(NoSuchElementException::new)
                .getQuantity();
    }


}

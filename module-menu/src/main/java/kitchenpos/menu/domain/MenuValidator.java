package kitchenpos.menu.domain;

import java.util.List;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.Products;
import kitchenpos.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class MenuValidator {

    private final ProductRepository productRepository;

    public MenuValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validate(Menu menu) {
        validateOverPrice(menu.getPrice(), menu.getMenuProducts());
    }

    private void validateOverPrice(Price price, MenuProducts menuProducts) {
        List<Product> products = productRepository.findAllById(menuProducts.getProductIds());
        Price totalPrice = menuProducts.getTotalPrice(new Products(products));
        if (price.overTo(totalPrice)) {
            throw new IllegalArgumentException("[ERROR] 메뉴 가격은 메뉴 상품들의 총 금액보다 클 수 없습니다.");
        }
    }
}

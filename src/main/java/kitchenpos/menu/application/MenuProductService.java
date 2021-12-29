package kitchenpos.menu.application;

import kitchenpos.common.domain.Price;
import kitchenpos.common.exception.IllegalArgumentException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class MenuProductService {
    private final ProductRepository productRepository;

    public MenuProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }




}

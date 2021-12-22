package kitchenpos.product.testfixtures;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductDao;

public class ProductTestFixtures {

    public static void 상품_조회시_응답_모킹(ProductService productService, Product product) {
        given(productService.findProduct(product.getId()))
            .willReturn(product);
    }

    public static void 상품_생성_결과_모킹(ProductDao productDao, Product product) {
        given(productDao.save(any()))
            .willReturn(product);
    }

    public static void 상품_전체_조회_모킹(ProductDao productDao, List<Product> products) {
        given(productDao.findAll())
            .willReturn(products);
    }
}

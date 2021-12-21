package kitchenpos.application.testfixtures;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

public class ProductTestFixtures {

    public static void 상품_조회시_응답_모킹(ProductDao productDao, Product product) {
        given(productDao.findById(product.getId()))
            .willReturn(Optional.ofNullable(product));
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

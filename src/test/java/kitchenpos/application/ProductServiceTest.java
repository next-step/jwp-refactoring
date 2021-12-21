package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.application.testfixtures.ProductTestFixtures;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void create() {
        // given
        String name = "타코야끼";
        BigDecimal price = BigDecimal.valueOf(12000);
        Product product = new Product(name, price);
        ProductTestFixtures.상품_생성_결과_모킹(productDao, product);

        //when
        Product savedProduct = productService.create(product);

        //then
        assertThat(product.getName()).isEqualTo(savedProduct.getName());
    }

    @DisplayName("상품 가격은 0원 이상이어야 한다.")
    @Test
    void create_exception() {
        //given
        String name = "타코야끼";
        BigDecimal price = BigDecimal.valueOf(-1);
        Product product = new Product(name, price);

        //when, then
        assertThatThrownBy(() -> productService.create(product))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void list() {
        //given
        List<Product> products = Arrays.asList(
            new Product("타코야끼", BigDecimal.valueOf(12000)),
            new Product("뿌링클", BigDecimal.valueOf(22000)));
        ProductTestFixtures.상품_전체_조회_모킹(productDao, products);

        //when
        List<Product> findProducts = productService.list();

        //then
        assertThat(findProducts.size()).isEqualTo(products.size());
        assertThat(findProducts).containsAll(products);
    }
}

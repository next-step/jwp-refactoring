package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productDao);
    }

    @DisplayName("주어진 상품을 저장하고, 저장된 객체를 리턴한다.")
    @Test
    void create_product() {
        Product givenProduct = new Product("후라이드", BigDecimal.valueOf(16000));
        when(productDao.save(any(Product.class)))
                .thenReturn(givenProduct);

        Product actual = productService.create(givenProduct);

        assertThat(actual).isEqualTo(givenProduct);
    }

    @Test
    @DisplayName("상품저장시 가격이 없으면 예외를 던진다.")
    void create_product_with_no_price() {
        Product givenProduct = new Product("반반치킨", null);

        assertThatThrownBy(() -> productService.create(givenProduct))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품저장시 음수인 가격이 주어지면 예외를 던진다.")
    void create_product_with_negative_price() {
        Product givenProduct = new Product("반반치킨", BigDecimal.valueOf(-1000));

        assertThatThrownBy(() -> productService.create(givenProduct))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("모든 메뉴 그룹을 조회한다")
    void list() {
        Product givenProduct1 = new Product("후라이드", BigDecimal.valueOf(16000));
        Product givenProduct2 = new Product("양념치킨", BigDecimal.valueOf(16000));

        when(productDao.findAll())
                .thenReturn(Arrays.asList(givenProduct1, givenProduct2));
        List<Product> products = productService.list();

        assertThat(products).containsAll(Arrays.asList(givenProduct1, givenProduct2));
    }

}

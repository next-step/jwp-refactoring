package kitchenpos.domain.product;

import kitchenpos.application.ProductService;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("상품 관련 기능")
class ProductTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductDao productDao;

    @Test
    @DisplayName("상품의 이름이 비어있을 경우 예외가 발생한다.")
    void emptyProductName() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            productService.create(new Product(" ", BigDecimal.valueOf(1000)));
        });
    }

    @Test
    @DisplayName("상품의 가격이 0원 이상이 아닐 경우 예외가 발생한다.")
    void productPriceLessThanZero() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            productService.create(new Product("후라이드", BigDecimal.valueOf(-1000)));
        });
    }

    @Test
    @DisplayName("상품을 등록할 수 있다.")
    void createProduct() {
        // given
        when(productDao.save(any())).thenReturn(new Product(1L, "후라이드", BigDecimal.valueOf(17000)));

        // when
        Product savedProduct = productService.create(new Product("후라이드", BigDecimal.valueOf(17000)));

        // then
        assertAll(
                () -> assertThat(savedProduct.getName()).isEqualTo("후라이드"),
                () -> assertThat(savedProduct.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(17000))
        );
    }

    @Test
    @DisplayName("상품 목록을 조회할 수 있다.")
    void findProduct() {
        // given
        List<Product> products = Arrays.asList(new Product(1L, "후라이드", BigDecimal.valueOf(17000)),
                new Product(2L, "양념치킨", BigDecimal.valueOf(17000)));
        when(productDao.findAll()).thenReturn(products);

        // when
        List<Product> findByProducts = productService.list();

        // then
        assertAll(
                () -> assertThat(findByProducts).extracting("id").isNotNull(),
                () -> assertThat(findByProducts.size()).isEqualTo(2)
        );
    }
}

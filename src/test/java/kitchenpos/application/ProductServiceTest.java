package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductDao productDao;

    private ProductService productService;

    @BeforeEach
    void setUp() {
        this.productService = new ProductService(productDao);
    }

    @Test
    @DisplayName("create - 가격이 비어있거나, 0보다 적을경우 IllegalArgumentException이 발생한다.")
    void 가격이_비어있거나_0보다_적을경우_IllegalArgumentException이_발생한다() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> productService.create(new Product(null, null, null)));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> productService.create(new Product(null, null, BigDecimal.valueOf(-1))));
    }

    @Test
    @DisplayName("create - 정상적인 상품 등록")
    void 정상적인_상품_등록() {
        // given
        Product product = new Product(null, null, BigDecimal.valueOf(0));

        // when
        when(productDao.save(product))
                .thenReturn(product);
        Product savedProduct = productService.create(product);

        // then
        assertThat(savedProduct)
                .isEqualTo(product);

        verify(productDao, VerificationModeFactory.only())
                .save(product);
    }

    @Test
    @DisplayName("list - 정상적인 상품 전체 조회")
    void 정상적인_상품_전체_조회() {
        // given
        Product product1 = new Product(1L, "A", BigDecimal.valueOf(0));
        Product product2 = new Product(2L, "B", BigDecimal.valueOf(1));

        // when
        when(productDao.findAll())
                .thenReturn(Arrays.asList(product1, product2));

        List<Product> list = productService.list();

        // then
        assertThat(list)
                .containsExactly(product1, product2);

        verify(productDao, VerificationModeFactory.only())
                .findAll();
    }
}
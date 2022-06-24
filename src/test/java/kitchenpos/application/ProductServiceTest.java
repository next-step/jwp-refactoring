package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    private ProductService productService;

    @Mock
    private ProductDao productDao;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productDao);
    }

    @Test
    @DisplayName("상품(제품)을 만든다.")
    void createProduct() {
        // given
        Product 강정치킨 = new Product("강정치킨", BigDecimal.valueOf(17_000));
        when(productDao.save(any())).thenReturn(강정치킨);
        // when
        final ProductResponse actual = productService.create(new ProductRequest("강정치킨", BigDecimal.valueOf(17_000)));
        // given
        assertAll(
                () -> assertThat(actual.getName()).isEqualTo(강정치킨.getName()),
                () -> assertThat(actual.getPrice()).isEqualTo(강정치킨.getPrice())
        );
    }

    @Test
    @DisplayName("상품(제품)의 가격이 0보다 작으면 예외가 발생한다.")
    void createNegativePriceProduct() {
        // when && given
        assertThatIllegalArgumentException()
                .isThrownBy(() -> productService.create(new ProductRequest("음수치킨", BigDecimal.valueOf(-100))));
    }

    @ParameterizedTest(name = "상품(제품)의 가격이 비어있다면 예외가 발생한다.")
    @NullSource
    void createEmptyPriceProduct(BigDecimal empty) {
        // when && given
        assertThatIllegalArgumentException()
                .isThrownBy(() -> productService.create(new ProductRequest("비어있는치킨", empty)));
    }

    @Test
    @DisplayName("상품(제품)들을 조회한다.")
    void searchProducts() {
        // given
        when(productDao.findAll()).thenReturn(Arrays.asList(new Product(), new Product()));
        // when
        final List<ProductResponse> actual = productService.list();
        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).hasSize(2)
        );
    }
}

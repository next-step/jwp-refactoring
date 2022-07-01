package kitchenpos.product;

import kitchenpos.application.ProductService;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.util.testFixture.양념치킨_상품_생성;
import static kitchenpos.util.testFixture.후라이드_상품_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @InjectMocks
    ProductService productService;

    @Mock
    ProductDao productDao;

    private Product 후라이드;
    private Product 양념치킨;

    @BeforeEach
    void setUp() {
        후라이드 = 후라이드_상품_생성();
        양념치킨 = 양념치킨_상품_생성();
    }

    @DisplayName("상품 등록")
    @Test
    void createProduct() {
        // given
        when(productDao.save(후라이드))
                .thenReturn(후라이드);

        // when
        Product result = productService.create(후라이드);

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(후라이드.getId()),
                () -> assertThat(result.getName()).isEqualTo(후라이드.getName()),
                () -> assertThat(result.getPrice()).isEqualTo(후라이드.getPrice())
        );
    }

    @DisplayName("상품 가격이 0보다 작은 경우 등록 불가")
    @Test
    void createProductAndPriceZero() {
        // given
        후라이드.setPrice(new BigDecimal(-1));

        // then
        assertThatThrownBy(() -> {
            productService.create(후라이드);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 가격이 `null`인 경우 등록 불가")
    @Test
    void createProductAndPriceNull() {
        // given
        후라이드.setPrice(null);

        // then
        assertThatThrownBy(() -> {
            productService.create(후라이드);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 전체 조회")
    @Test
    void findProducts() {
        // given
        when(productDao.findAll())
                .thenReturn(Arrays.asList(후라이드, 양념치킨));

        // when
        List<Product> list = productService.list();

        // then
        assertAll(
                () -> assertThat(list.size()).isEqualTo(2),
                () -> assertThat(list).containsExactly(후라이드, 양념치킨)
        );
    }
}

package kitchenpos.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
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

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    ProductDao productDao;

    Product 후라이드 = new Product();
    Product 양념치킨 = new Product();

    @BeforeEach
    void setUp() {
        createProduct();
    }

    void createProduct() {
        후라이드.setId(1L);
        후라이드.setName("후라이드");
        후라이드.setPrice(new BigDecimal(15000));

        양념치킨.setId(2L);
        양념치킨.setName("양념치킨");
        양념치킨.setPrice(new BigDecimal(15000));
    }

    @Test
    @DisplayName("상품을 저장한다")
    void create() {
        // given
        given(productDao.save(any())).willReturn(후라이드);

        // when
        Product actual = productService.create(후라이드);

        // then
        assertThat(actual).isEqualTo(후라이드);
    }

    @Test
    @DisplayName("상품 정보 저장시 상품의 금액은 0원 이상이다")
    void create_priceError() {
        // given
        후라이드.setPrice(new BigDecimal(-1));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> productService.create(후라이드)
        );
    }

    @Test
    @DisplayName("상품 리스트를 조회한다")
    void list() {
        // given
        given(productDao.findAll()).willReturn(Arrays.asList(후라이드, 양념치킨));

        // when
        List<Product> actual = productService.list();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual).containsExactlyInAnyOrder(후라이드, 양념치킨)
        );
    }
}

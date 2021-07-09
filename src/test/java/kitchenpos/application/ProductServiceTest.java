package kitchenpos.application;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private Product 불고기버거;

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        불고기버거 = new Product();
        불고기버거.setId(1L);
        불고기버거.setName("불고기버거");
        불고기버거.setPrice(BigDecimal.valueOf(4000));
    }

    @DisplayName("상품명과 가격 정보를 입력해 상품을 등록한다")
    @Test
    void createProduct() {
        // given
        given(productDao.save(불고기버거)).willReturn(불고기버거);

        // when
        Product actual = productService.create(불고기버거);

        // then
        assertThat(actual).isEqualTo(불고기버거);
    }

    @DisplayName("상품 등록 - 가격은 0 이상의 숫자를 입력해야 한다")
    @Test
    void createProduct_illegalMinPrice() {
        // given
        불고기버거.setPrice(BigDecimal.valueOf(-1));

        // when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> productService.create(불고기버거));
    }
}
package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품 생성 테스트")
    @Test
    void createTest() {
        // given
        Product 불고기 = new Product("불고기", new BigDecimal(1000));
        Mockito.when(productDao.save(any())).thenReturn(불고기);

        // when
        Product actual = productService.create(불고기);

        // then
        assertThat(actual).isNotNull()
                          .extracting(product -> product.getName())
                          .isEqualTo(불고기.getName());
    }

    @DisplayName("가격이 0 이하인 상품 등록 테스트")
    @Test
    void createTestWithWrongPrice() {
        // given
        Product 불고기 = new Product("불고기", new BigDecimal(-1));

        // when
        assertThatThrownBy(() -> productService.create(불고기))
            .isInstanceOf(IllegalArgumentException.class);
    }
}

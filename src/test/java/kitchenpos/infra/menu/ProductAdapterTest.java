package kitchenpos.infra.menu;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.menu.exceptions.ProductEntityNotFoundException;
import kitchenpos.domain.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductAdapterTest {
    private ProductAdapter productAdapter;

    @Mock
    private ProductDao productDao;

    @BeforeEach
    void setup() {
        productAdapter = new ProductAdapter(productDao);
    }

    @DisplayName("안전하게 존재하는 상품의 가격을 받아올 수 있다.")
    @Test
    void getProductPriceTest() {
        // given
        Long productId = 1L;
        BigDecimal price = BigDecimal.ONE;

        given(productDao.findById(productId)).willReturn(Optional.of(new Product("product", price)));

        // when
        BigDecimal productPrice = productAdapter.getProductPrice(productId);

        // then
        assertThat(productPrice).isEqualTo(price);
    }

    @DisplayName("존재하지 않는 상품의 가격을 찾으려 할 경우 예외 발생")
    @Test
    void getProductPriceFailTest() {
        // given
        Long notExistId = 4L;

        // when, then
        assertThatThrownBy(() -> productAdapter.getProductPrice(notExistId))
                .isInstanceOf(ProductEntityNotFoundException.class)
                .hasMessage("존재하지 않는 상품입니다.");
    }
}
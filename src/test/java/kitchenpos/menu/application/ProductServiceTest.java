package kitchenpos.menu.application;

import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService service;

    @Test
    @DisplayName("상품의 가격이 null이면 exception이 발생함")
    void throwExceptionWhenProductPriceIsNull() {
        Product product = Product.of(1L, "상품", null);

        assertThatThrownBy(() -> service.create(product)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품의 가격이 음수이면 exception이 발생함")
    void throwExceptionWhenProductPriceIsNegative() {
        Product product = Product.of(1L, "상품", BigDecimal.valueOf(-1));

        assertThatThrownBy(() -> service.create(product)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품을 생성한다")
    void createProduct() {
        Product product = Product.of(1L, "상품", BigDecimal.valueOf(10000));
        given(productRepository.save(any())).willReturn(product);

        Product saved = service.create(product);

        assertThat(saved).isEqualTo(product);
    }

    @Test
    @DisplayName("저장된 상품을 모두 조회할 수 있다")
    void queryAllProducts() {
        List<Product> products = Arrays.asList(
                Product.of(1L, "상품1", BigDecimal.valueOf(10000)),
                Product.of(2L, "상품2", BigDecimal.valueOf(10000))
        );
        given(productRepository.findAll()).willReturn(products);

        List<Product> list = service.list();

        assertThat(list).isEqualTo(products);
    }
}
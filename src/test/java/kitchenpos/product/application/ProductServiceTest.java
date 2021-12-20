package kitchenpos.product.application;

import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static kitchenpos.product.domain.ProductTest.후라이드_상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("상품 관리 테스트")
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("상품 등록 성공")
    void createProductTest() {
        // given
        given(productRepository.save(any())).willReturn(후라이드_상품);
        // when
        Product actual = productService.create(후라이드_상품);
        // then
        assertThat(actual).isEqualTo(후라이드_상품);
    }

    @ParameterizedTest
    @ValueSource(ints = {
            1, 0
    })
    @DisplayName("상품 가격이 0원 이상인 상품 등록")
    void productPriceOverZero(int price) {
        // given
        Product product = new Product("양념치킨", new Price(BigDecimal.valueOf(price)));
        given(productRepository.save(any())).willReturn(product);
        // when
        Product actual = productService.create(product);
        // then
        assertThat(actual).isEqualTo(product);
    }

    @Test
    @DisplayName("상품 가격은 0원 이상 이어야 한다.")
    void productPriceException() {
        // given
        Product product = new Product("양념치킨", new Price(BigDecimal.valueOf(-1)));
        // when
        // then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("상품 목록 조회")
    void findAllProduct() {
        // given
        given(productRepository.findAll())
                .willReturn(Collections.singletonList(후라이드_상품));
        // when
        List<Product> actual = productService.list();
        // then
        assertThat(actual).hasSize(1);
        assertThat(actual).containsExactly(후라이드_상품);
    }
}

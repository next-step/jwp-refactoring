package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
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

import static kitchenpos.domain.ProductTest.통새우와퍼_상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("상품 관리 테스트")
public class ProductServiceTest {
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("상품 등록 성공")
    void createProductTest() {
        // given
        given(productDao.save(any())).willReturn(통새우와퍼_상품);
        // when
        Product actual = productService.create(통새우와퍼_상품);
        // then
        assertThat(actual).isEqualTo(통새우와퍼_상품);
    }

    @ParameterizedTest
    @ValueSource(ints = {
            1, 0
    })
    @DisplayName("상품 가격이 0원 이상인 상품 등록")
    void productPriceOverZero(int price) {
        // given
        Product product = new Product("양념치킨", BigDecimal.valueOf(price));
        given(productDao.save(any())).willReturn(product);
        // when
        Product actual = productService.create(product);
        // then
        assertThat(actual).isEqualTo(product);
    }

    @Test
    @DisplayName("상품 가격은 0원 이상 이어야 한다.")
    void productPriceException() {
        // given
        Product product = new Product("양념치킨", BigDecimal.valueOf(-1));
        // when
        // then
        assertThatThrownBy(() -> productService.create(product))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("상품 목록 조회")
    void findAllProduct() {
        // given
        given(productDao.findAll())
                .willReturn(Collections.singletonList(통새우와퍼_상품));
        // when
        List<Product> actual = productService.list();
        // then
        assertThat(actual).hasSize(1);
        assertThat(actual).containsExactly(통새우와퍼_상품);
    }
}

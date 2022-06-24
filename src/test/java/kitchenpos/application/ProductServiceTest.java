package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.assertj.core.api.Assertions;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductDao productDao;

    private Product 피자;
    private Product 치킨;

    @BeforeEach
    void setUp() {
        피자 = new Product();
        피자.setName("피자");
        피자.setPrice(BigDecimal.valueOf(26_000));

        치킨 = new Product();
        치킨.setName("치킨");
        치킨.setPrice(BigDecimal.valueOf(10_000));
    }

    @Test
    @DisplayName("상품 등록시 정상 테스트")
    void create() {
        when(productDao.save(피자)).thenReturn(피자);

        Product 피자_상품_등록_결과 = productService.create(피자);

        Assertions.assertThat(피자).isEqualTo(피자_상품_등록_결과);
    }

    @Test
    @DisplayName("상품 등록시 가격이 0원인 경우 실패 테스트")
    void createFail() {
        치킨.setPrice(BigDecimal.valueOf(-100));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> productService.create(치킨));
    }

    @Test
    @DisplayName("상품 목록 조회 테스트")
    void find() {
        when(productService.list()).thenReturn(Arrays.asList(피자, 치킨));

        List<Product> 상품_목록_조회_결과 = productService.list();

        assertAll(
                () -> Assertions.assertThat(상품_목록_조회_결과).hasSize(2),
                () -> Assertions.assertThat(상품_목록_조회_결과).containsExactly(피자, 치킨)
        );
    }
}

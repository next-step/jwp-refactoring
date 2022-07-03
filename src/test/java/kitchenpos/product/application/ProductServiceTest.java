package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
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

import static kitchenpos.testfixture.CommonTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository);
    }

    @DisplayName("상품을 등록한다.")
    @Test
    void create_success() {
        // given
        ProductRequest 후라이드싸이순살 = createProductRequest("후라이드싸이순살", BigDecimal.valueOf(20_000));
        given(productRepository.save(any(Product.class))).willReturn(후라이드싸이순살.toProduct());

        // when
        ProductResponse saved = productService.create(후라이드싸이순살);

        // then
        assertAll(
                () -> assertThat(saved).isNotNull(),
                () -> assertThat(saved.getName()).isEqualTo(후라이드싸이순살.getName()),
                () -> assertThat(saved.getPrice()).isEqualTo(후라이드싸이순살.getPrice())
        );
    }

    @DisplayName("상품 등록에 실패한다. (상품 가격이 0 원 미만인 경우)")
    @Test
    void create_fail() {
        // when
        ProductRequest 후라이드싸이순살 = createProductRequest("후라이드싸이순살", BigDecimal.valueOf(-1));

        // then
        assertThatThrownBy(() -> {
            productService.create(후라이드싸이순살);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        // given
        ProductRequest 후라이드싸이순살 = createProductRequest("후라이드싸이순살", BigDecimal.valueOf(20_000));
        ProductRequest 블랙쏘이치킨 = createProductRequest("블랙쏘이치킨", BigDecimal.valueOf(18_000));
        given(productRepository.findAll()).willReturn(Arrays.asList(후라이드싸이순살.toProduct(), 블랙쏘이치킨.toProduct()));

        // when
        List<ProductResponse> products = productService.list();

        // then
        assertThat(products).hasSize(2);
    }
}

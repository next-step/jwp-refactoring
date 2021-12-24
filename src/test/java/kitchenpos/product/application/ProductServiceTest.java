package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    ProductRequest 후라이드치킨;
    ProductRequest 양념치킨;

    @BeforeEach
    void setUp() {
        후라이드치킨 = ProductRequest.of("후라이드치킨", BigDecimal.valueOf(16000));
        양념치킨 = ProductRequest.of("양념치킨", BigDecimal.valueOf(17000));
    }

    @Test
    void 상품_등록() {
        // given
        given(productRepository.save(any())).willReturn(Product.of(후라이드치킨.getName(), 후라이드치킨.getPrice()));

        // when
        ProductResponse actual = productService.create(후라이드치킨);

        // then
        Assertions.assertThat(actual.getName()).isEqualTo("후라이드치킨");
    }

    @Test
    void 상품_조회() {
        // given
        List<Product> products = Arrays.asList(
                Product.of(후라이드치킨.getName(), 후라이드치킨.getPrice()),
                Product.of(양념치킨.getName(), 양념치킨.getPrice()));
        given(productRepository.findAll()).willReturn(products);

        // when
        List<ProductResponse> actual = productService.list();

        // then
        assertAll(() -> {
            assertThat(actual).hasSize(2);
            assertThat(actual).extracting("name")
                    .containsExactly("후라이드치킨", "양념치킨");
        });
    }
}

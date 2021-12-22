package kitchenpos.tobe.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.tobe.fixture.ProductFixture;
import kitchenpos.tobe.product.domain.Product;
import kitchenpos.tobe.product.domain.ProductRepository;
import kitchenpos.tobe.product.dto.ProductRequest;
import kitchenpos.tobe.product.dto.ProductResponse;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("상품 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void register() {
        // given
        final Product expected = ProductFixture.of(
            1L,
            "후라이드",
            BigDecimal.valueOf(16_000L)
        );
        given(productRepository.save(any(Product.class))).willReturn(expected);

        final ProductRequest request = ProductFixture.ofRequest(
            expected.getName(),
            expected.getPrice()
        );

        // when
        final ProductResponse response = productService.register(request);

        // then
        assertAll(
            () -> assertThat(response.getId()).isNotNull(),
            () -> assertThat(response.getName()).isEqualTo(expected.getName()),
            () -> assertThat(response.getPrice()).isEqualTo(expected.getPrice())
        );
    }

    @DisplayName("상품을 등록할 수 없다.")
    @Nested
    class RegisterFailTest {

        @DisplayName("상품 이름이 null 또는 \"\"일 경우")
        @ParameterizedTest(name = "{displayName} [{index}] {argumentsWithNames}")
        @NullAndEmptySource
        void invalidName(final String name) {
            // given
            final ProductRequest request = ProductFixture.ofRequest(
                name,
                BigDecimal.valueOf(16_000L)
            );

            // when
            ThrowableAssert.ThrowingCallable response = () -> productService.register(request);

            // then
            assertThatThrownBy(response).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("상품 가격이 null 또는 0보다 작을 경우")
        @ParameterizedTest(name = "{displayName} [{index}] {argumentsWithNames}")
        @NullSource
        @ValueSource(strings = {"-16000"})
        void invalidPrice(final BigDecimal price) {
            // given
            final ProductRequest request = ProductFixture.ofRequest("후라이드", price);

            // when
            ThrowableAssert.ThrowingCallable response = () -> productService.register(request);

            // then
            assertThatThrownBy(response).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("상품 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        final List<Product> expected = Arrays.asList(
            ProductFixture.of(1L, "후라이드", BigDecimal.valueOf(16_000)),
            ProductFixture.of(2L, "양념치킨", BigDecimal.valueOf(16_000))
        );
        given(productRepository.findAll()).willReturn(expected);

        // when
        final List<ProductResponse> response = productService.list();

        // then
        final List<Long> expectedIds = expected.stream()
            .map(Product::getId)
            .collect(Collectors.toList());
        final List<Long> actualIds = response.stream()
            .map(ProductResponse::getId)
            .collect(Collectors.toList());
        assertAll(
            () -> assertThat(actualIds).containsExactlyElementsOf(expectedIds),
            () -> assertThat(response.size()).isEqualTo(2)
        );
    }
}

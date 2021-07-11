package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.menu.dto.ProductRequest;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void given_Product_when_CreateProduct_thenSaveExecuted() {
        // given
        final ProductRequest productRequest = new ProductRequest();
        productRequest.setPrice(BigDecimal.ZERO);

        // when
        productService.create(productRequest);

        // then
        verify(productRepository).save(any(Product.class));
    }

    @ParameterizedTest
    @MethodSource("providePrice")
    void given_InvalidPrice_when_CreateProduct_thenThrownException(final BigDecimal price) {
        // given
        final ProductRequest productRequest = new ProductRequest();
        productRequest.setPrice(price);

        // when
        final Throwable throwable = catchThrowable(() -> productService.create(productRequest));

        // then
        assertThat(throwable).isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> providePrice() {
        return Stream.of(
            Arguments.of((Object)null),
            Arguments.of(new BigDecimal(-1))
        );
    }

    @Test
    void when_List_then_FindAllExecuted() {
        // when
        productService.list();

        // then
        verify(productRepository).findAll();
    }
}

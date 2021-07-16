package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static java.math.BigDecimal.valueOf;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@TestInstance(PER_CLASS)
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        // given
        productService = new ProductService(productDao);
    }

    @MethodSource("methodSource_create_성공")
    @ParameterizedTest
    void create_성공(String name, BigDecimal price) {
        // given
        Product product = new Product(name, price);

        // when
        when(productDao.save(product)).thenReturn(product);
        Product createdProduct = productService.create(product);

        // then
        assertThat(createdProduct).isEqualTo(product);
        verify(productDao, times(1)).save(product);
    }

    Stream<Arguments> methodSource_create_성공() {
        return Stream.of(
                Arguments.of("후라이드치킨", valueOf(0)),
                Arguments.of("후라이드치킨", valueOf(18_000))
        );
    }

    @MethodSource("methodSource_create_예외")
    @ParameterizedTest
    void create_예외(String name, BigDecimal price) {
        // given
        Product product = new Product(name, price);

        // when, then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> productService.create(product));
    }

    Stream<Arguments> methodSource_create_예외() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of("후라이드치킨", null),
                Arguments.of("후라이드치킨", valueOf(-100))
        );
    }

    @Test
    void list_성공() {
        // given
        List<Product> products = asList(new Product("순살지코바양념치킨", valueOf(20_000)),
                new Product("콜라 1.5L", valueOf(3_000)));

        // when
        when(productDao.findAll()).thenReturn(products);
        List<Product> foundProducts = productService.list();

        // then
        assertThat(foundProducts).containsExactlyElementsOf(products);
        verify(productDao, times(1)).findAll();
    }
}
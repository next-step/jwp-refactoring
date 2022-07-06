package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import kitchenpos.product.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("상품을 관리한다.")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;


    @Test
    @DisplayName("상품을 등록한다.")
    void menuCreate() {
        //given
        Product product = new Product(1L,"양념치킨", BigDecimal.ONE);
        given(productRepository.save(product)).willReturn(product);

        //when
        final ProductResponse createProduct = productService.create(new ProductRequest("양념치킨", BigDecimal.ONE));

        //then
        assertAll(
                () -> assertThat(createProduct).isNotNull(),
                () -> assertThat(product.getName()).isEqualTo(createProduct.getName())
        );
    }


    @DisplayName("상품의 가격이 올바르지 않으면 등록할 수 없다. (EX) 상품 가격 -1원)")
    @ParameterizedTest
    @MethodSource("잘못된_상품가격")
    void isNotValidatePriceCreateProduct(BigDecimal price) {
        //given
        ProductRequest request = new ProductRequest("양념치킨", price);

        //when & then
        assertThatIllegalArgumentException().isThrownBy(() ->
            productService.create(request)
        );
    }

    private static Stream<Arguments> 잘못된_상품가격() {
        return Stream.of(
                null,
                Arguments.of(BigDecimal.valueOf(-1)),
                Arguments.of(BigDecimal.ZERO)
        );
    }

    @Test
    @DisplayName("상품목록들을 조회한다.")
    void searchProducts() {
        //givne
        final Product 양념치킨 = new Product(1L, "양념치킨", BigDecimal.valueOf(1000));
        final Product 후라이드치킨 = new Product(2L, "후라이드치킨", BigDecimal.valueOf(2000));
        given(productRepository.findAll()).willReturn(Arrays.asList(양념치킨, 후라이드치킨));

        //when
        final List<ProductResponse> products = productService.list();

        //then
        assertAll(
                () -> assertThat(products).hasSize(2),
                () -> assertThat(products).extracting("name").contains("양념치킨", "후라이드치킨")
        );
    }




}

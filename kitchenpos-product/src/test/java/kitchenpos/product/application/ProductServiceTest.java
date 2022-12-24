package kitchenpos.product.application;

import kitchenpos.common.Price;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    private Product 참치김밥;
    private Product 치즈김밥;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        참치김밥 = new Product(1L, "참치김밥", new Price(new BigDecimal(3000)));
        치즈김밥 = new Product(2L, "치즈김밥", new Price(new BigDecimal(2500)));
    }

    @DisplayName("상품등록 테스트")
    @Test
    void createProductTest() {
        //given
        final String productName = 참치김밥.getName();
        final BigDecimal productPrice = 참치김밥.getPrice().getValue();
        when(productRepository.save(any(Product.class)))
                .thenReturn(참치김밥);

        //when
        ProductResponse product = productService.create(new ProductRequest(productName, productPrice));

        //then
        assertAll(
                () -> assertThat(product.getName()).isEqualTo(productName),
                () -> assertThat(product.getPrice()).isEqualTo(productPrice)
        );
    }

    @DisplayName("상품목록 조회 테스트")
    @Test
    void retrieveProductListTest() {
        //given
        when(productRepository.findAll())
                .thenReturn(Arrays.asList(참치김밥, 치즈김밥));

        //when
        List<ProductResponse> products = productService.findAll();

        //then
        assertAll(
                () -> assertThat(productsToNames(products)).contains(참치김밥.getName(), 치즈김밥.getName()),
                () -> assertThat(productsToPrices(products)).contains(참치김밥.getPrice(), 치즈김밥.getPrice())
        );
    }

    private List<String> productsToNames(List<ProductResponse> products) {
        return products.stream()
                .map(ProductResponse::getName)
                .collect(Collectors.toList());
    }

    private List<Price> productsToPrices(List<ProductResponse> products) {
        return products.stream()
                .map(ProductResponse::getPrice)
                .map(Price::new)
                .collect(Collectors.toList());
    }

    @DisplayName("상품등록 가격 데이터가 없을 경우 오류 테스트")
    @Test
    void createProductPriceNullExceptionTest() {
        //given
        final ProductRequest request = new ProductRequest(참치김밥.getName(), null);

        //when
        //then
        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품등록 가격 데이터가 0보다 작은 경우 오류 테스트")
    @Test
    void createProductPriceUnderZeroExceptionTest() {
        //given
        final ProductRequest request = new ProductRequest(참치김밥.getName(), new BigDecimal(-1));

        //when
        //then
        assertThatThrownBy(() -> productService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

}

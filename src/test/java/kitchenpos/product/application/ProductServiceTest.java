package kitchenpos.product.application;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
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
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    private static final Product 참치김밥 = new Product(1L, "참치김밥", new BigDecimal(3000));
    private static final Product 치즈김밥 = new Product(2L, "치즈김밥", new BigDecimal(2500));

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @DisplayName("상품등록 테스트")
    @Test
    void createProductTest() {
        //given
        given(productRepository.save(any(Product.class))).willReturn(참치김밥);

        //when
        ProductResponse product = productService.create(
                new ProductRequest(참치김밥.getName(), 참치김밥.getPrice()));

        //then
        assertAll(
                () -> assertThat(product.getName()).isEqualTo(참치김밥.getName()),
                () -> assertThat(product.getPrice()).isEqualTo(참치김밥.getPrice())
        );
    }

    @DisplayName("상품목록 조회 테스트")
    @Test
    void retrieveProductListTest() {
        //given
        given(productRepository.findAll())
                .willReturn(Arrays.asList(참치김밥, 치즈김밥));

        //when
        List<ProductResponse> products = productService.list();

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

    private List<BigDecimal> productsToPrices(List<ProductResponse> products) {
        return products.stream()
                .map(ProductResponse::getPrice)
                .collect(Collectors.toList());
    }

    @DisplayName("상품등록 가격 데이터가 없을 경우 오류 테스트")
    @Test
    void createProductPriceNullExceptionTest() {
        assertThatThrownBy(() -> productService.create(
                new ProductRequest(참치김밥.getName(), null)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품등록 가격 데이터가 0보다 작은 경우 오류 테스트")
    @Test
    void createProductPriceUnderZeroExceptionTest() {
        assertThatThrownBy(() -> productService.create(
                new ProductRequest(참치김밥.getName(), new BigDecimal(-1))))
                .isInstanceOf(IllegalArgumentException.class);
    }



}

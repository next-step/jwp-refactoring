package kitchenpos.application;

import static kitchenpos.helper.ReflectionHelper.SetProductId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.ProductRequest;
import kitchenpos.dto.response.ProductResponse;
import kitchenpos.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    private ProductService productService;
    private Product product;
    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    public void init() {
        productService = new ProductService(productRepository);
        product = new Product("chicken", BigDecimal.valueOf(18000));

        SetProductId(1L, product);

    }

    @Test
    @DisplayName("상품 저장 정상로직")
    void create() {
        //given
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName(product.getName());
        productRequest.setPrice(product.getPrice());

        when(productRepository.save(any())).thenReturn(product);

        //when
        ProductResponse productResponse = productService.create(productRequest);

        assertAll(
            () -> assertThat(productResponse.getName()).isEqualTo(productRequest.getName()),
            () -> assertThat(productResponse.getPrice()).isEqualTo(productRequest.getPrice()),
            () -> assertThat(productResponse.getId()).isNotNull()
        );
    }

    @Test
    @DisplayName("가격 음수일때 상품 저장 불가")
    void createProductWithMinusPriceThrowError() {
        //given
        ProductRequest minusProduct = new ProductRequest();
        minusProduct.setPrice(BigDecimal.valueOf(-1));
        minusProduct.setName("마이너스상품");

        //when
        assertThatThrownBy(() -> productService.create(minusProduct)).isInstanceOf(
            IllegalArgumentException.class);
    }

    @Test
    @DisplayName("가격이 NULL일때 상품 저장 불가")
    void createProductWithNullPriceThrowError() {
        //given
        ProductRequest minusProduct = new ProductRequest();
        minusProduct.setPrice(null);
        minusProduct.setName("널상품");

        //when
        assertThatThrownBy(() -> productService.create(minusProduct)).isInstanceOf(
            IllegalArgumentException.class);
    }

}
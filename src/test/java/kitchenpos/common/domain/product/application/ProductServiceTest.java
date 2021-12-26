package kitchenpos.common.domain.product.application;

import kitchenpos.common.domain.Name;
import kitchenpos.common.exception.InputDataErrorCode;
import kitchenpos.common.exception.InputDataException;
import kitchenpos.product.application.ProductService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@DisplayName("상품 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("상품을 등록한다.")
    void createProductTest() {
        //given
        ProductRequest productRequest = mock(ProductRequest.class);
        Product product = mock(Product.class);

        given(product.getName())
                .willReturn(new Name("양파치킨"));

        when(productRequest.toEntity()).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        //when
        productService.create(productRequest);
        //then
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @DisplayName("상품을 조회한다.")
    void findAllTest() {
        //given
        Product product = mock(Product.class);

        when(product.getId())
                .thenReturn(1L);
        when(product.getName())
                .thenReturn(new Name("양파치킨"));

        when(productRepository.findAll())
                .thenReturn(Arrays.asList(product));

        //when
        List<ProductResponse> productResponses = productService.findAll();

        //then
        assertThat(productResponses.size()).isEqualTo(1);
        assertThat(productResponses).isNotEmpty();
    }

    @Test
    @DisplayName("상품 가격이 0원 미만일 경우 테스트")
    void priceLessThanZeroTest() {
        assertThatThrownBy(
                () -> productService.create(new ProductRequest("대파치킨", new BigDecimal(-1000)))
        ).isInstanceOf(InputDataException.class)
                .hasMessageContaining(InputDataErrorCode.THE_PRICE_CAN_NOT_INPUT_LESS_THAN_ZERO.errorMessage());
    }

    @Test
    @DisplayName("상품 가격을 넣지 않을 경우 테스트")
    void notInputPriceTest() {
        assertThatThrownBy(
                () -> productService.create(new ProductRequest("대파치킨", null))
        ).isInstanceOf(InputDataException.class)
                .hasMessageContaining(InputDataErrorCode.THE_PRICE_MUST_INPUT.errorMessage());
    }
}

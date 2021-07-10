package kitchenpos.Product.application;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductEntity;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;


    private ProductRequest productRequest;

    @BeforeEach
    void setup() {
        productRequest = new ProductRequest("테스트상품",BigDecimal.valueOf(1000));
    }

    @DisplayName("상품을 생성할 수 있다.")
    @Test
    void createTest() {
        //given
        given(productRepository.save(any())).willReturn(productRequest.toEntity());

        //when
        ProductResponse productResponse = productService.createTemp(productRequest);

        //then
        assertThat(productRequest.getName()).isEqualTo(productResponse.getName());
        assertThat(productRequest.getPrice()).isEqualTo(productResponse.getPrice());
    }



}
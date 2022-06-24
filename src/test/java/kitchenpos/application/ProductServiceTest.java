package kitchenpos.application;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.application.fixture.ProductFixtureFactory;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

   private Product 짬뽕;
   private Product 짜장;

    @BeforeEach
    void before() {
        짬뽕 = ProductFixtureFactory.create("짬뽕", BigDecimal.valueOf(1000));
        짜장 = ProductFixtureFactory.create("짜장", BigDecimal.valueOf(1000));
    }

    @Test
    @DisplayName("상품을 생성 할 수 있다.")
    void createTest() {
        //given
        ProductRequest 저장할_상품 = ProductRequest.of("짬뽕", BigDecimal.valueOf(1000));
        given(productRepository.save(any(Product.class))).willReturn(짬뽕);

        //when
        ProductResponse productResponse = productService.create(저장할_상품);
        //then
        assertThat(productResponse).isEqualTo(ProductResponse.of(짬뽕));
    }

    @Test
    @DisplayName("상품 목록을 조회 한다.")
    void listTest() {
        //given
        given(productRepository.findAll()).willReturn(Arrays.asList(짬뽕, 짜장));

        //when
        List<ProductResponse> products = productService.list();

        //then
        assertThat(products).containsExactly(ProductResponse.of(짬뽕), ProductResponse.of(짜장));
    }
}

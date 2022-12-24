package kitchenpos.product.application;

import kitchenpos.product.domain.Price;
import kitchenpos.product.dto.ProductCreateRequest;
import kitchenpos.product.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

@DisplayName("상품 서비스")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;
//
//    @DisplayName("상품 생성")
//    @Test
//    void create() {
//
//        Product product = productA();
//
//        BDDMockito.given(productRepository.save(ArgumentMatchers.any())).willReturn(product);
//
//        ProductResponse response = productService.create(new ProductCreateRequest(product.getName().getName(), product.getPrice()));
//
//        assertAll(
//                () -> assertEquals(0, response.getPrice().compareTo(product.getPrice())),
//                () -> assertThat(response.getName()).isEqualTo(product.getName())
//        );
//    }

    @DisplayName("상품 생성 / 가격을 필수로 갖는다.")
    @Test
    void create_fail_priceNull() {
        Assertions.assertThatThrownBy(() -> productService.create(new ProductCreateRequest("상품A", null)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(Price.PRICE_NOT_NULL_EXCEPTION_MESSAGE);
    }

    @DisplayName("상품 생성 / 가격은 0원보다 작을 수 없다.")
    @Test
    void create_fail_minimumPrice() {
        Assertions.assertThatThrownBy(() -> productService.create(new ProductCreateRequest("상품A", BigDecimal.valueOf(-1))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(Price.PRICE_MINIMUM_EXCEPTION_MESSAGE);
    }

//    @DisplayName("상품 목록 조회")
//    @Test
//    void list() {
//        BDDMockito.given(productRepository.findAll()).willReturn(Collections.singletonList(productA()));
//        assertThat(productService.list()).hasSize(1);
//    }
}


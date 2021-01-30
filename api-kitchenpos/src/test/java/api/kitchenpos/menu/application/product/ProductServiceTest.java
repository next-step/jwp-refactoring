package api.kitchenpos.menu.application.product;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import api.kitchenpos.menu.dto.product.ProductRequest;
import api.kitchenpos.menu.dto.product.ProductResponse;
import domain.kitchenpos.menu.product.Product;
import domain.kitchenpos.menu.product.ProductRepository;

@DisplayName("애플리케이션 테스트 보호 - 상품 서비스")
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    private ProductRequest 후라이드치킨_요청;
    private Product 후라이드치킨;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setup() {
        후라이드치킨_요청 = new ProductRequest("후라이드", new BigDecimal(16000));
        후라이드치킨 = 후라이드치킨_요청.toProduct();
    }

    @DisplayName("상품 등록")
    @Test
    void create() {
        given(productRepository.save(후라이드치킨)).willReturn(후라이드치킨);

        ProductResponse productResponse = productService.create(후라이드치킨_요청);

        assertThat(productResponse).isNotNull();
        assertThat(productResponse.getName()).isEqualTo(후라이드치킨.getName());
        assertThat(productResponse.getPrice()).isEqualTo(후라이드치킨.getPrice());
    }

    @DisplayName("상품 등록 예외: 가격이 없을 경우")
    @Test
    void createThrowExceptionWhenNoPrice() {
        후라이드치킨_요청.setPrice(null);
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(후라이드치킨_요청));
    }

    @DisplayName("상품 등록 예외: 가격이 0보다 작을 경우")
    @Test
    void createThrowExceptionWhenPriceLessThanZero() {
        후라이드치킨_요청.setPrice(BigDecimal.valueOf(-1));
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(후라이드치킨_요청));
    }

    @DisplayName("상품 목록 조회")
    @Test
    void findAll() {
        given(productRepository.findAll()).willReturn(Collections.singletonList(후라이드치킨));

        List<ProductResponse> productResponses = productService.list();

        assertThat(productResponses).isNotEmpty();
        assertThat(productResponses.get(0).getName()).isEqualTo(후라이드치킨.getName());
        assertThat(productResponses.get(0).getPrice()).isEqualTo(후라이드치킨.getPrice());

    }

}

package kitchenpos.product.application;

import static kitchenpos.product.domain.ProductTest.상품_생성;
import static kitchenpos.product.dto.ProductRequestTest.상품_생성_요청_객체_생성;
import static kitchenpos.product.dto.ProductResponseTest.상품_응답_객체들_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
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

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product 미역국;
    private Product 소머리국밥;
    private Product 순대국밥;

    @BeforeEach
    public void setUp() {
        미역국 = 상품_생성(1L, "미역국", BigDecimal.valueOf(6000));
        소머리국밥 = 상품_생성(2L, "소머리국밥", BigDecimal.valueOf(8000));
        순대국밥 = 상품_생성(3L, "순대국밥", BigDecimal.valueOf(7000));
    }


    @Test
    @DisplayName("상품 등록")
    void create() {
        // given
        when(productRepository.save(any(Product.class))).thenReturn(미역국);
        ProductRequest 미역국_생성_요청_객체 = 상품_생성_요청_객체_생성(미역국.getNameValue(), 미역국.getPriceVale());

        // when
        ProductResponse 등록된_미역국 = productService.create(미역국_생성_요청_객체);

        // then
        assertThat(등록된_미역국.getId()).isEqualTo(미역국.getId());
        assertThat(등록된_미역국.getName()).isEqualTo(미역국.getNameValue());
        assertThat(등록된_미역국.getPrice()).isEqualTo(미역국.getPriceVale());
    }

    @Test
    @DisplayName("상품 목록 조회")
    void list() {
        // given
        when(productRepository.findAll()).thenReturn(Arrays.asList(미역국, 소머리국밥, 순대국밥));

        // when
        List<ProductResponse> products = productService.list();

        // then
        assertThat(products).hasSize(3);
        assertThat(products).containsAll(상품_응답_객체들_생성(미역국, 소머리국밥, 순대국밥));
    }
}
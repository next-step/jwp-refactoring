package kitchenpos.menu.application;

import kitchenpos.menu.domain.Product;
import kitchenpos.menu.domain.ProductRepository;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("상품 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product 후라이드치킨_상품;
    private Product 양념치킨_상품;
    private Product 간장치킨_상품;

    @BeforeEach
    void setUp() {
        후라이드치킨_상품 = new Product("후라이드치킨", 16000);
        양념치킨_상품 = new Product("양념치킨", 16000);
        간장치킨_상품 = new Product("간장치킨", 17_000);

        ReflectionTestUtils.setField(후라이드치킨_상품, "id", 1L);
        ReflectionTestUtils.setField(양념치킨_상품, "id", 2L);
        ReflectionTestUtils.setField(간장치킨_상품, "id", 3L);
    }

    @DisplayName("상품의 가격은 반드시 존재해야 한다.")
    @Test
    void 상품의_가격은_반드시_존재해야_한다() {
        // given
        ProductRequest 햄버거 = new ProductRequest("햄버거", null);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(햄버거));
    }

    @DisplayName("상품의 가격은 0원 이상이어야 한다.")
    @Test
    void 상품의_가격은_0원_이상이어야_한다() {
        // given
        ProductRequest 햄버거 = new ProductRequest("햄버거", -1);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(햄버거));
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void 상품을_생성한다() {
        // given
        when(productRepository.save(any(Product.class))).thenReturn(후라이드치킨_상품);

        // when
        ProductResponse productResponse = productService.create(new ProductRequest("후라이드치킨", 16000));

        // then
        assertThat(productResponse).satisfies(response -> {
            assertEquals(response.getId(), 후라이드치킨_상품.getId());
            assertEquals(response.getName(), 후라이드치킨_상품.getName());
            assertEquals(response.getPrice(), 후라이드치킨_상품.getPrice().value());
        });
    }

    @DisplayName("상품을 조회한다.")
    @Test
    void 상품을_조회한다() {
        // given
        when(productRepository.findAll()).thenReturn(Arrays.asList(양념치킨_상품, 간장치킨_상품));

        // when
        List<ProductResponse> products = productService.list();

        // then
        assertThat(products).hasSize(2);
    }
}

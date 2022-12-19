package kitchenpos.product.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.product.application.ProductService;
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

@DisplayName("상품 Business Object 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private ProductRequest 떡볶이;
    private ProductRequest 튀김;
    private ProductRequest 순대;

    @BeforeEach
    void setUp() {
        떡볶이 = new ProductRequest("떡볶이", BigDecimal.valueOf(4500));
        튀김 = new ProductRequest("튀김", BigDecimal.valueOf(2500));
        순대 = new ProductRequest("순대", BigDecimal.valueOf(4000));
    }


    @DisplayName("상품 생성")
    @Test
    void 상품_생성() {
        when(productRepository.save(떡볶이.toProduct())).thenReturn(떡볶이.toProduct());

        ProductResponse 생성된_상품 = productService.create(떡볶이);

        assertAll(
                () -> assertThat(생성된_상품.getName()).isEqualTo("떡볶이"),
                () -> assertThat(생성된_상품.getPrice()).isEqualTo(BigDecimal.valueOf(4500))
        );
    }

    @DisplayName("가격이 0원 보다 작은 상품 생성 요청 시 예외처리")
    @Test
    void 올바르지_않은_가격_예외처리() {
        ProductRequest 잘못된_가격_상품 = new ProductRequest("잘못된_가격_상품", BigDecimal.valueOf(-2500));

        assertThatThrownBy(() -> productService.create(잘못된_가격_상품)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("비어있는 이름으로 상품 생성 요청 시 예외처리")
    @Test
    void 비어있는_이름_예외처리() {
        ProductRequest 비어있는_이름_상품 = new ProductRequest("", BigDecimal.valueOf(4000));

        assertThatThrownBy(() -> productService.create(비어있는_이름_상품)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 조회")
    @Test
    void 상품_조회() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(떡볶이.toProduct(), 튀김.toProduct(), 순대.toProduct()));
        List<ProductResponse> 조회된_상품_목록 = productService.list();

        assertAll(
                () -> assertThat(조회된_상품_목록.get(0).getName()).isEqualTo(떡볶이.getName()),
                () -> assertThat(조회된_상품_목록.get(1).getName()).isEqualTo(튀김.getName()),
                () -> assertThat(조회된_상품_목록.get(2).getName()).isEqualTo(순대.getName())
        );
    }
}
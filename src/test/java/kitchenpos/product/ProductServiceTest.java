package kitchenpos.product;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.ProductDao;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    private Product 후라이드;
    private Product 양념치킨;

    @BeforeEach
    void setUp() {
        후라이드 = new Product();
        후라이드.setId(1L);
        후라이드.setName("후라이드");

        양념치킨 = new Product();
        양념치킨.setId(2L);
        양념치킨.setName("양념치킨");
    }

    @DisplayName("0원 이상의 가격으로 상품을 등록한다")
    @Test
    void 상품_등록() {
        //Given
        후라이드.setPrice(BigDecimal.valueOf(16000));
        when(productDao.save(후라이드)).thenReturn(후라이드);

        //When
        Product 생성된_상품 = productService.create(후라이드);

        //Then
        assertThat(생성된_상품.getId()).isNotNull();
        assertThat(생성된_상품.getName()).isEqualTo(후라이드.getName());
    }

    @DisplayName("가격이 입력되지 않은 경우, 상품 등록시 예외가 발생한다")
    @Test
    void 가격_입력되지_않음_예외발생() {
        //Given
        후라이드.setPrice(null);

        //When + Then
        assertThatThrownBy(() -> productService.create(후라이드))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("가격이 0원 미만인 경우, 상품 등록시 예외가 발생한다")
    @Test
    void 가격_0원미만_예외발생() {
        //Given
        후라이드.setPrice(BigDecimal.valueOf(-1));

        //When + Then
        assertThatThrownBy(() -> productService.create(후라이드))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 목록을 조회한다")
    @Test
    void 상품_목록_조회() {
        //Given
        List<Product> 입력한_상품_목록 = new ArrayList<>(Arrays.asList(후라이드));
        when(productDao.findAll()).thenReturn(입력한_상품_목록);

        //When
        List<Product> 조회된_상품_목록 = productService.list();

        //Then
        assertThat(조회된_상품_목록).isNotNull()
                .hasSize(조회된_상품_목록.size())
                .containsExactly(후라이드);
    }
}

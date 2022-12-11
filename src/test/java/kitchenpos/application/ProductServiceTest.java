package kitchenpos.application;

import static kitchenpos.domain.ProductTest.상품_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
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
    private ProductDao productDao;

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
        when(productDao.save(미역국)).thenReturn(미역국);

        // when
        Product 등록된_미역국 = productService.create(미역국);

        // then
        assertThat(등록된_미역국).isEqualTo(미역국);
    }

    @Test
    @DisplayName("상품 목록 조회")
    void list() {
        // given
        when(productDao.findAll()).thenReturn(Arrays.asList(미역국, 소머리국밥, 순대국밥));

        // when
        List<Product> products = productService.list();

        // then
        assertThat(products).hasSize(3);
        assertThat(products).contains(미역국, 소머리국밥, 순대국밥);
    }
}
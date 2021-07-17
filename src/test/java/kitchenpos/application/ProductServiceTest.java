package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;

@SpringBootTest
class ProductServiceTest {

    @MockBean
    private ProductDao productDao;

    private ProductService productService;
    private Product 짜장면, 짬뽕;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productDao);

        짜장면 = new Product();
        짜장면.setId(1L);
        짜장면.setName("짜장면");
        짜장면.setPrice(new BigDecimal(50L));

        짬뽕 = new Product();
        짬뽕.setId(2L);
        짬뽕.setName("짬뽕");
        짬뽕.setPrice(new BigDecimal(60L));
    }

    @DisplayName("상품 생성")
    @Test
    void create() {
        when(productDao.save(짜장면)).thenReturn(짜장면);
        assertThat(productService.create(짜장면)).isEqualTo(짜장면);
    }

    @DisplayName("가격으로 인한 상품 생성 실패")
    @Test
    void createFailCausePrice() {
        짜장면.setPrice(new BigDecimal(-1));
        assertThatThrownBy(() -> productService.create(짜장면)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품 조회")
    @Test
    void list() {
        when(productDao.findAll()).thenReturn(Arrays.asList(짜장면, 짬뽕));
        assertThat(productService.list()).contains(짜장면, 짬뽕);
    }
}
package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
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
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;
    
    private Product 강정치킨;

    @BeforeEach
    void setUp() {
        강정치킨 = new Product();
        강정치킨.setName("강정치킨");
        강정치킨.setPrice(new BigDecimal("7500"));
    }

    @DisplayName("상품을 이름, 가격으로 등록")
    @Test
    void create() {
        given(productDao.save(강정치킨)).willReturn(강정치킨);

        Product createProduct = productService.create(강정치킨);

        assertThat(createProduct).isNotNull();
    }

    @DisplayName("상품 목록 조회")
    @Test
    void list() {
        given(productDao.findAll()).willReturn(Arrays.asList(강정치킨));

        List<Product> products = productService.list();

        assertThat(products.size()).isEqualTo(1);
    }
}

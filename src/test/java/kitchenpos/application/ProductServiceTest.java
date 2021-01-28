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
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;

@DisplayName("애플리케이션 테스트 보호 - 상품 서비스")
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    private Product product;

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setup() {
        product = new Product();
        product.setId(1L);
        product.setName("강정치킨");
        product.setPrice(BigDecimal.valueOf(17000));
    }

    @DisplayName("상품 등록")
    @Test
    void create() {
        given(productDao.save(product)).willReturn(product);

        Product savedProduct = productService.create(product);

        assertThat(savedProduct).isEqualTo(product);
    }

    @DisplayName("상품 등록 예외: 가격이 없을 경우")
    @Test
    void createThrowExceptionWhenNoPrice() {
        product.setPrice(null);
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(product));
    }

    @DisplayName("상품 등록 예외: 가격이 0보다 작을 경우")
    @Test
    void createThrowExceptionWhenPriceLessThanZero() {
        product.setPrice(BigDecimal.valueOf(-1));
        assertThatIllegalArgumentException().isThrownBy(() -> productService.create(product));
    }

    @DisplayName("상품 목록 조회")
    @Test
    void findAll() {
        given(productDao.findAll()).willReturn(Collections.singletonList(product));

        List<Product> products = productService.list();

        assertThat(products).containsExactly(product);
    }

}

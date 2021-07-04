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

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    private Product 강정치킨;
    private Product 후라이드;

    @BeforeEach
    void setUp() {
        강정치킨 = new Product(1L, "강정치킨", BigDecimal.valueOf(17000));
        후라이드 = new Product(2L, "후라이드", BigDecimal.valueOf(16000));
    }

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void createTest() {
        // given
        given(productDao.save(강정치킨)).willReturn(강정치킨);

        // when
        Product createdProduct = productService.create(강정치킨);

        // then
        assertThat(createdProduct.getId()).isEqualTo(강정치킨.getId());
        assertThat(createdProduct.getName()).isEqualTo(강정치킨.getName());
        assertThat(createdProduct.getPrice()).isEqualTo(강정치킨.getPrice());
    }

    @DisplayName("상품의 가격이 올바르지 않으면 등록할 수 없다 : 상품의 가격은 0 원 이상이어야 한다.")
    @Test
    void createTest_wrongPrice() {
        // given
        강정치킨.setPrice(BigDecimal.valueOf(-1000));

        // when & then
        assertThatThrownBy(() -> productService.create(강정치킨))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품의 목록을 조회할 수 있다.")
    @Test
    void listTest() {
        // given
        given(productDao.findAll()).willReturn(Arrays.asList(강정치킨, 후라이드));

        // when
        List<Product> products = productService.list();

        // then
        assertThat(products).containsExactly(강정치킨, 후라이드);
        assertThat(products.get(0).getId()).isEqualTo(강정치킨.getId());
        assertThat(products.get(0).getName()).isEqualTo(강정치킨.getName());
        assertThat(products.get(0).getPrice()).isEqualTo(강정치킨.getPrice());
    }

}

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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@DisplayName("상품 기능 테스트")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    private Product product1;
    private Product product2;

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        product1 = new Product();
        product1.setId(1L);
        product1.setName("후라이드");
        product1.setPrice(new BigDecimal(16000));

        product2 = new Product();
        product2.setId(2L);
        product2.setName("양념치킨");
        product2.setPrice(new BigDecimal(16000));
    }

    @Test
    @DisplayName("상품을 등록할 수 있다.")
    public void create() throws Exception {
        // given
        given(productDao.save(product1)).willReturn(product1);

        // when
        Product createdProduct = productService.create(this.product1);

        // then
        assertThat(createdProduct.getId()).isEqualTo(this.product1.getId());
        assertThat(createdProduct.getName()).isEqualTo(this.product1.getName());
        assertThat(createdProduct.getPrice()).isEqualTo(this.product1.getPrice());
    }

    @Test
    @DisplayName("상품의 가격이 올바르지 않으면 등록할 수 없다 : 상품의 가격은 0 원 이상이어야 한다.")
    public void createFail() throws Exception {
        // given


        product1.setPrice(new BigDecimal(-1));

        // when then
        assertThatThrownBy(() -> productService.create(this.product1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품의 목록을 조회할 수 있다.")
    public void list() throws Exception {
        // given
        given(productDao.findAll()).willReturn(Arrays.asList(product1, product2));

        // when
        List<Product> products = productService.list();

        // then
        assertThat(products).containsExactly(product1, product2);
        assertThat(products.get(0).getId()).isEqualTo(product1.getId());
        assertThat(products.get(0).getName()).isEqualTo(product1.getName());
        assertThat(products.get(0).getPrice()).isEqualTo(product1.getPrice());
    }
}

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductDao productDao;

    @InjectMocks
    ProductService productService;

    private Product 볶음밥;
    private Product 김치찌개;

    @BeforeEach
    void setUp() {

        볶음밥 = new Product();
        볶음밥.setId(1L);
        볶음밥.setName("볶음밥");
        볶음밥.setPrice(new BigDecimal(7000));

        김치찌개 = new Product();
        김치찌개.setId(2L);
        김치찌개.setName("김치찌개");
        김치찌개.setPrice(new BigDecimal(6000));
    }

    @DisplayName("상품을 등록")
    @Test
    void create1() {
        //given
        Product 저장전_김치찌개 = new Product();
        저장전_김치찌개.setName("김치찌개");
        저장전_김치찌개.setPrice(new BigDecimal("6000"));

        given(productDao.save(any())).willReturn(김치찌개);

        //when
        Product createProduct = productService.create(저장전_김치찌개);

        //then
        assertThat(createProduct.getId()).isEqualTo(2L);
        assertThat(createProduct.getName()).isEqualTo("김치찌개");
        assertThat(createProduct.getPrice()).isEqualTo(new BigDecimal("6000"));
    }

    @DisplayName("상품을 등록 - 상품의 가격은 0 원 이상이어야 한다.")
    @Test
    void create2() {
        //given
        Product 저장전_김치찌개 = new Product();
        저장전_김치찌개.setName("김치찌개");
        저장전_김치찌개.setPrice(new BigDecimal("-1"));

        //when
        //then
        assertThatThrownBy(() -> {
            productService.create(저장전_김치찌개);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품을 등록 - 상품의 가격이 지정되어 있어야 한다.")
    @Test
    void create3() {
        //given
        Product 저장전_김치찌개 = new Product();
        저장전_김치찌개.setName("김치찌개");

        //when
        //then
        assertThatThrownBy(() -> {
            productService.create(저장전_김치찌개);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void list() {
        //given
        given(productDao.findAll()).willReturn(Arrays.asList(볶음밥, 김치찌개));
        //when
        List<Product> products = productService.list();

        //then
        assertThat(products.size()).isEqualTo(2);
        assertThat(products.get(0).getName()).isEqualTo("볶음밥");
        assertThat(products.get(1).getName()).isEqualTo("김치찌개");
    }
}
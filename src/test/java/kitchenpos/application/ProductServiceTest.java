package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
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

    private Product 치킨;
    private Product 피자;

    @Test
    @DisplayName("치킨 - 상품을 생성한다 (Happy Path)")
    void create() {
        //given
        치킨 = new Product(1L, "치킨", new BigDecimal(15000));
        given(productDao.save(any(Product.class))).willReturn(치킨);

        //when
        Product savedProduct = productService.create(치킨);

        //then
        assertThat(savedProduct).isNotNull()
                                .satisfies(product1 -> {
                                    product1.getId().equals(1L);
                                    product1.getName().equals("치킨");
                                    product1.getPrice().equals(new BigDecimal(15000));
                                    }
                                );
    }

    @Test
    @DisplayName("가격이 0원 보다 적은 상품은 생성할 수 없다")
    void 상품_가격0원_설정시_생성불가 () {
        //given
        치킨 = new Product(1L, "치킨", new BigDecimal(-1));

        //then
        assertThatThrownBy(() -> productService.create(치킨))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품 전체 리스트를 조회한다")
    void list() {
        //given
        치킨 = new Product(1L, "치킨", new BigDecimal(15000));
        피자 = new Product(2L, "피자", new BigDecimal(20000));
        given(productDao.findAll()).willReturn(Arrays.asList(치킨, 피자));

        //when
        List<Product> products = productService.list();

        //then
        assertThat(products).containsExactlyInAnyOrderElementsOf(Arrays.asList(치킨, 피자));
    }
}

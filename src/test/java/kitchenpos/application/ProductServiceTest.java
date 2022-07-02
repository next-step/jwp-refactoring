package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService service;

    @DisplayName("상품 등록 성공")
    @Test
    void create() {
        //given
        Product 치킨까스 = new Product("치킨까스", BigDecimal.valueOf(4000));
        when(productDao.save(치킨까스)).thenReturn(치킨까스);

        //when
        Product result = service.create(치킨까스);

        //then
        assertAll(
                () -> assertThat(result.getName()).isEqualTo(치킨까스.getName()),
                () -> assertThat(result.getPrice()).isEqualTo(치킨까스.getPrice())
        );
    }

    @DisplayName("상품 가격이 음수인 상품 등록")
    @Test
    void createWithNegativePrice() {
        //given
        Product 치킨까스 = new Product("치킨까스", BigDecimal.valueOf(-3000));

        //when & then
        assertThatThrownBy(() -> service.create(치킨까스)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전체 상품 조회")
    @Test
    void list() {
        //given
        Product 치킨까스 = new Product("치킨까스", BigDecimal.valueOf(4000));
        Product 돈까스 = new Product("돈까스", BigDecimal.valueOf(3500));
        when(productDao.findAll()).thenReturn(Arrays.asList(치킨까스, 돈까스));

        //when
        List<Product> products = service.list();

        //then
        assertThat(products).contains(치킨까스, 돈까스);
    }
}

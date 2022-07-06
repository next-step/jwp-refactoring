package kitchenpos.application;

import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private ProductService productService;

    @Test
    void 상품_등록() {
        //given
        Product 빅맥 = new Product(1L, "빅맥", new BigDecimal(4000));
        given(productDao.save(any())).willReturn(빅맥);

        //when
        Product saved_빅맥 = productService.create(빅맥);

        //then
        assertAll(
                () -> assertThat(saved_빅맥.getName()).isEqualTo("빅맥"),
                () -> assertThat(saved_빅맥.getPrice()).isEqualTo(new BigDecimal(4000))
        );

    }

    @Test
    void 상품의_가격이_0보다_작을_경우_상품_등록_에러() {
        //given
        Product 빅맥 = new Product(1L, "빅맥", new BigDecimal(-1));

        //when + then
        assertThrows(IllegalArgumentException.class, () -> productService.create(빅맥));
    }

    @Test
    void 상품_목록_조회() {
        //given
        Product 빅맥 = new Product(1L, "빅맥", new BigDecimal(4000));
        Product 불고기버거 = new Product(2L, "불고기버거", new BigDecimal(3000));
        given(productDao.findAll()).willReturn(Arrays.asList(빅맥, 불고기버거));

        //when
        List<Product> productList = productService.list();

        //then
        assertThat(productList.size()).isEqualTo(2);
    }
}

package kitchenpos.application;

import static common.ProductFixture.가격이없는_반반치킨;
import static common.ProductFixture.양념치킨;
import static common.ProductFixture.후라이드;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Product;
import org.assertj.core.api.Assertions;
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
    private ProductService productService;

    @Test
    void 상품을_생성할수_있다() {
        // given
        Product 후라이드 = 후라이드();

        // when
        when(productDao.save(후라이드)).thenReturn(후라이드);
        Product savedProduct = productService.create(후라이드);

        // then
        assertThat(savedProduct).isEqualTo(후라이드);
    }

    @Test
    void 가격이_없는경우_생성할수_없다() {
        // given
        Product 가격이없는_반반치킨 = 가격이없는_반반치킨();

        // then
        assertThatThrownBy(() -> {
            productService.create(가격이없는_반반치킨);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 상품_전체를_조회한다() {
        // given
        Product 후라이드 = 후라이드();
        Product 양념치킨 = 양념치킨();

        when(productDao.findAll()).thenReturn(Arrays.asList(후라이드,양념치킨));
        List<Product> list = productService.list();

        // then
        Assertions.assertThat(list).containsExactly(후라이드, 양념치킨);
    }
}
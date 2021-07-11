package kitchenpos.manu.domain;

import kitchenposNew.menu.domain.Menu;
import kitchenposNew.menu.domain.MenuProduct;
import kitchenposNew.menu.domain.MenuProducts;
import kitchenposNew.menu.domain.Product;
import kitchenposNew.wrap.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 기능 관련 테스트")
public class MenuTest {
    @Test
    void 메뉴_생성() {
        Product 떡볶이 = new Product("떡볶이", new Price(BigDecimal.valueOf(17000)));
        MenuProduct 분식_떡볶이 = new MenuProduct(떡볶이, 1L);

        Menu 분식 = new Menu("분식", new Price(BigDecimal.valueOf(17000)), 1L, new MenuProducts(Arrays.asList(분식_떡볶이)));
        assertThat(분식).isEqualTo(new Menu("분식", new Price(BigDecimal.valueOf(17000)), 1L, new MenuProducts(Arrays.asList(분식_떡볶이))));
    }
}

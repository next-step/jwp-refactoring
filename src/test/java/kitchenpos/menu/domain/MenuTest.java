package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.common.ServiceTestFactory.메뉴그룹생성;
import static kitchenpos.common.ServiceTestFactory.메뉴상품생성;
import static kitchenpos.common.ServiceTestFactory.메뉴생성;
import static kitchenpos.common.ServiceTestFactory.상품생성;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class MenuTest {
    @Test
    void 상품들의_가격의_합보다_메뉴price가_클_수_없다() {
        MenuGroup menuGroup = 메뉴그룹생성(1L, "name");
        Product 상품1 = 상품생성(1L, "name", BigDecimal.valueOf(3000));
        MenuProduct 메뉴상품1 = 메뉴상품생성(1L, 상품1, 1L);
        Product 상품2 = 상품생성(2L, "name", BigDecimal.valueOf(4000));
        MenuProduct 메뉴상품2 = 메뉴상품생성(1L, 상품2, 1L);
        List<MenuProduct> menuProducts = Arrays.asList(메뉴상품1, 메뉴상품2);

        Menu menu = 메뉴생성(1L,"name", BigDecimal.valueOf(10000), menuGroup,menuProducts);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> menu.addMenuProducts(menuProducts));
    }

}

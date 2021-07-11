package kitchenpos.manu.domain;

import kitchenpos.domain.MenuProduct;
import kitchenposNew.menu.domain.Menu;
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
        MenuProduct 중식_포함_메뉴 = new MenuProduct();
        중식_포함_메뉴.setSeq(1L);
        중식_포함_메뉴.setMenuId(1L);
        중식_포함_메뉴.setProductId(1L);
        중식_포함_메뉴.setQuantity(1);

        Menu 분식 = new Menu("분식", new Price(BigDecimal.valueOf(17000)), 1L, Arrays.asList(중식_포함_메뉴));
        assertThat(분식).isEqualTo(new Menu("분식", new Price(BigDecimal.valueOf(17000)), 1L, Arrays.asList(중식_포함_메뉴)));
    }
}

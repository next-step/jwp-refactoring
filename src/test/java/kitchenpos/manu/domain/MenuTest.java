package kitchenpos.manu.domain;

import kitchenpos.menu.domain.*;
import kitchenpos.wrap.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 기능 관련 테스트")
public class MenuTest {
    @Test
    void 메뉴_생성() {
        Product 떡볶이 = new Product(1L, "떡볶이", new Price(BigDecimal.valueOf(17000)));
        MenuProduct 분식_떡볶이 = new MenuProduct(1L, 1L);

        Menu 분식 = new Menu("분식", new Price(BigDecimal.valueOf(17000)), new MenuGroup(1L, "분식메뉴"), new MenuProducts(Arrays.asList(분식_떡볶이)));
        assertThat(분식).isEqualTo(new Menu("분식", new Price(BigDecimal.valueOf(17000)), new MenuGroup(1L, "분식메뉴"), new MenuProducts(Arrays.asList(분식_떡볶이))));
    }
}

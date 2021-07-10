package kitchenpos.menu.domain;

import kitchenpos.menu.exception.InvalidPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    @DisplayName("메뉴의 가격이 올바르지 않으면 등록할 수 없다 : 메뉴의 가격은 0 원 이상이어야 한다.")
    @Test
    void createTest_wrongPrice() {
        // given
        MenuGroup 추천메뉴 = new MenuGroup(1L, "추천메뉴");

        // when & then
        assertThatThrownBy(() -> new Menu(1L, "강정치킨plus강정치킨", BigDecimal.valueOf(-1000), 추천메뉴, new ArrayList<>()))
                .isInstanceOf(InvalidPriceException.class);
    }

    @DisplayName("메뉴의 가격이 올바르지 않으면 등록할 수 없다 : 메뉴의 가격은 메뉴 상품 목록 가격의 합보다 작거나 같아야 한다.")
    @Test
    void createTest_wrongPrice2() {
        // given
        MenuGroup 추천메뉴 = new MenuGroup(1L, "추천메뉴");
        Product 강정치킨 = new Product(1L, "강정치킨", BigDecimal.valueOf(17000));
        MenuProduct 강정치킨양두배 = new MenuProduct(1L, 강정치킨, 2);

        // when & then
        assertThatThrownBy(() -> new Menu(1L, "강정치킨+강정치킨", BigDecimal.valueOf(34001), 추천메뉴, Arrays.asList(강정치킨양두배)))
                .isInstanceOf(InvalidPriceException.class);
    }
}

package kitchenpos.order.domain;

import kitchenpos.common.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OrderMenuTest {

    @Test
    void create() {
        Menu menu = new Menu("양식세트", new BigDecimal(50000), new MenuGroup("양식"));
        OrderMenu orderMenu = OrderMenu.of(menu);

        assertThat(orderMenu.getId()).isEqualTo(menu.getId());
        assertThat(orderMenu.getName()).isEqualTo(menu.getName());
        assertThat(orderMenu.getPrice()).isEqualTo(menu.getPrice());
    }

    @DisplayName("메뉴를 변경해도 OrderMenu는 변하지 않는다.")
    @Test
    void modifyMenu_isolationTest() {
        Menu menu = new Menu("양식세트", new BigDecimal(50000), new MenuGroup("양식"));
        OrderMenu orderMenu = OrderMenu.of(menu);

        ReflectionTestUtils.setField(menu, "name", "한식세트");
        ReflectionTestUtils.setField(menu, "price", new Price(new BigDecimal(40000)));

        assertThat(menu.getName()).isEqualTo("한식세트");
        assertThat(menu.getPriceToDecimal()).isEqualTo(new BigDecimal(40000));
        assertThat(orderMenu.getName()).isNotEqualTo(menu.getName());
        assertThat(orderMenu.getPrice()).isNotEqualTo(menu.getPrice());
    }
}

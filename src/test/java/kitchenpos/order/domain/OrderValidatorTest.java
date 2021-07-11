package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.service.OrderValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class OrderValidatorTest {
    @Autowired
    private MenuRepository menuRepository;

    @DisplayName("메뉴가 변경되었을 때 예외")
    @Test
    public void 메뉴변경시_예외_확인() throws Exception {
        //given
        OrderTable orderTable = new OrderTable(5, false);
        Menu menu = new Menu("후라이드치킨", BigDecimal.valueOf(15_000));
        Menu saveMenu = menuRepository.save(menu);
        OrderLineItem orderLineItem = new OrderLineItem(-1L, 1L);

        //when
        //then
        assertThatThrownBy(() -> new Order(orderTable, Arrays.asList(orderLineItem), new OrderValidator(menuRepository)))
                .hasMessage("메뉴가 변경되었습니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블이 빈테이블인 경우 예외")
    @Test
    public void 주문테이블이빈테이블인경우_예외_확인() throws Exception {
        //given
        OrderTable orderTable = new OrderTable(0, true);
        Menu menu = new Menu("후라이드치킨", BigDecimal.valueOf(15_000));
        Menu saveMenu = menuRepository.save(menu);
        OrderLineItem orderLineItem = new OrderLineItem(saveMenu.getId(), 1L);

        //when
        //then
        assertThatThrownBy(() -> new Order(orderTable, Arrays.asList(orderLineItem), new OrderValidator(menuRepository)))
                .hasMessage("주문테이블이 빈테이블입니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문항목이 없을 경우 예외")
    @Test
    public void 주문항목이없는경우_예외_확인() throws Exception {
        //given
        OrderTable orderTable = new OrderTable(5, false);

        //when
        //then
        assertThatThrownBy(() -> new Order(orderTable, Arrays.asList(), new OrderValidator(menuRepository)))
                .hasMessage("주문항목이 존재하지 않습니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }
}

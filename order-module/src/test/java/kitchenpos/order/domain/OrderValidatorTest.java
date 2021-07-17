package kitchenpos.order.domain;

import kitchenpos.OrderApplication;
import kitchenpos.OrderTestSupport;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ContextConfiguration(classes = OrderApplication.class)
@DataJpaTest
public class OrderValidatorTest extends OrderTestSupport {
    @Autowired
    private MenuRepository menuRepository;

    private OrderValidator orderValidator;

    @BeforeEach
    public void setUp() {
        orderValidator = new OrderValidator(menuRepository);
    }

    @DisplayName("메뉴가 변경되었을 때 예외")
    @Test
    public void 메뉴변경시_예외_확인() throws Exception {
        //given
        OrderTable orderTable = 테이블_등록되어있음(5, false);
        메뉴_등록되어있음("후라이드치킨", BigDecimal.valueOf(15_000));
        OrderLineItem orderLineItem = new OrderLineItem(-1L, 1L);

        //when
        //then
        assertThatThrownBy(() -> new Order(orderTable, Arrays.asList(orderLineItem), orderValidator))
                .hasMessage("메뉴가 변경되었습니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블이 빈테이블인 경우 예외")
    @Test
    public void 주문테이블이빈테이블인경우_예외_확인() throws Exception {
        //given
        OrderTable orderTable = 테이블_등록되어있음(0, true);
        Menu menu = 메뉴_등록되어있음("후라이드치킨", BigDecimal.valueOf(15_000));
        OrderLineItem orderLineItem = new OrderLineItem(menu.getId(), 1L);

        //when
        //then
        assertThatThrownBy(() -> new Order(orderTable, Arrays.asList(orderLineItem), orderValidator))
                .hasMessage("주문테이블이 빈테이블입니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문항목이 없을 경우 예외")
    @Test
    public void 주문항목이없는경우_예외_확인() throws Exception {
        //given
        OrderTable orderTable = 테이블_등록되어있음(5, false);

        //when
        //then
        assertThatThrownBy(() -> new Order(orderTable, Arrays.asList(), orderValidator))
                .hasMessage("주문항목이 존재하지 않습니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }
}

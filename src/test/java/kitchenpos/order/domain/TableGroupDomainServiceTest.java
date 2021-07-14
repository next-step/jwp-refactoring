package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.service.TableGroupDomainService;
import kitchenpos.util.TestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class TableGroupDomainServiceTest extends TestSupport {
    private TableGroupDomainService tableGroupDomainService = new TableGroupDomainService();

    @DisplayName("그룹 예외 - 테이블이 2개 미만인 경우")
    @Test
    public void 테이블이2개미만인경우_그룹_예외() throws Exception {
        //when
        //then
        assertThatThrownBy(() -> tableGroupDomainService.group(Arrays.asList(new OrderTable(5,
                true))))
                .hasMessage("주문테이블이 " + TableGroup.TABLE_COUNT_MIN + "개 미만입니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹 예외 - 테이블이 빈 테이블이 아닌 경우")
    @Test
    public void 빈테이블이아닌경우_그룹_예외() throws Exception {
        //given
        OrderTable orderTable1 = 테이블_등록되어있음(5, false);
        OrderTable orderTable2 = 테이블_등록되어있음(5, true);

        //when
        //then
        assertThatThrownBy(() -> tableGroupDomainService.group(Arrays.asList(orderTable1, orderTable2)))
                .hasMessage("주문테이블은 빈테이블이어야 합니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹 확인")
    @Test
    public void 그룹_확인() throws Exception {
        //given
        OrderTable orderTable1 = 테이블_등록되어있음(5, true);
        OrderTable orderTable2 = 테이블_등록되어있음(5, true);

        //when
        TableGroup tableGroup = 단체지정_등록되어있음(Arrays.asList(orderTable1, orderTable2));

        //then
        assertThat(orderTable1.getTableGroup()).isEqualTo(tableGroup);
        assertThat(orderTable2.getTableGroup()).isEqualTo(tableGroup);
    }

    @DisplayName("그룹해제 예외 - 주문상태가 계산완료가 아닌경우")
    @Test
    public void 주문상태가계산완료가아닌경우_그룹해제_예외() throws Exception {
        //given
        OrderTable orderTable1 = 테이블_등록되어있음(5, true);
        OrderTable orderTable2 = 테이블_등록되어있음(5, true);
        단체지정_등록되어있음(Arrays.asList(orderTable1, orderTable2));
        Menu menu = 메뉴_등록되어있음("메뉴", BigDecimal.valueOf(1000));
        Order order = 주문_등록되어있음(orderTable1, Arrays.asList(new OrderLineItem(menu.getId(), 1L)));

        //when
        //then
        assertThatThrownBy(() -> tableGroupDomainService.ungroup(Arrays.asList(orderTable1, orderTable2),
                Arrays.asList(order)))
                .hasMessage("주문테이블의 주문상태가 조리나 식사입니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹해제 확인")
    @Test
    public void 그룹해제_확인() throws Exception {
        //given
        OrderTable orderTable1 = 테이블_등록되어있음(5, true);
        OrderTable orderTable2 = 테이블_등록되어있음(5, true);
        단체지정_등록되어있음(Arrays.asList(orderTable1, orderTable2));
        Menu menu = 메뉴_등록되어있음("메뉴", BigDecimal.valueOf(1000));
        Order order = 주문_등록되어있음(orderTable1, Arrays.asList(new OrderLineItem(menu.getId(), 1L)));
        order.changeOrderStatus(OrderStatus.COMPLETION);

        //when
        tableGroupDomainService.ungroup(Arrays.asList(orderTable1, orderTable2), Arrays.asList(order));

        //then
        assertThat(orderTable1.getTableGroup()).isNull();
        assertThat(orderTable2.getTableGroup()).isNull();
    }
}

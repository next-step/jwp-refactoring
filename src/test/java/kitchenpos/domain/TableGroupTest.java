package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class TableGroupTest {
    @DisplayName("단체지정 등록 예외 - 주문테이블이 2개 미만인 경우")
    @Test
    public void 주문테이블이2개미만인경우_단체지정등록_예외() throws Exception {
        //given
        OrderTable orderTable = new OrderTable(5, true);

        //when
        //then
        assertThatThrownBy(() -> new TableGroup(Arrays.asList(orderTable)))
                .hasMessage("주문테이블이 2개 미만입니다.")
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new TableGroup(Arrays.asList()))
                .hasMessage("주문테이블이 2개 미만입니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체지정 등록 예외 - 주문테이블이 빈테이블인 경우")
    @Test
    public void 주문테이블이빈테이블인경우_단체지정등록_예외() throws Exception {
        //given
        OrderTable orderTable1 = new OrderTable(5, false);
        OrderTable orderTable2 = new OrderTable(5, false);

        //when
        //then
        assertThatThrownBy(() -> new TableGroup(Arrays.asList(orderTable1, orderTable2)))
                .hasMessage("주문테이블은 빈테이블이어야하고 단체지정이 되어있으면 안됩니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체지정 등록 예외 - 주문테이블이 단체지정되어있는 경우")
    @Test
    public void 주문테이블이단체지정되어있는경우_단체지정등록_예외() throws Exception {
        //given
        OrderTable orderTable1 = new OrderTable(5, true);
        OrderTable orderTable2 = new OrderTable( 5, true);
        new TableGroup(Arrays.asList(orderTable1, orderTable2));

        //when
        //then
        assertThatThrownBy(() -> new TableGroup(Arrays.asList(orderTable1, orderTable2)))
                .hasMessage("주문테이블은 빈테이블이어야하고 단체지정이 되어있으면 안됩니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹")
    @Test
    public void 그룹_확인() throws Exception {
        //given
        OrderTable orderTable1 = new OrderTable( 5, true);
        OrderTable orderTable2 = new OrderTable(5, true);

        //when
        TableGroup tableGroup = new TableGroup(Arrays.asList(orderTable1, orderTable2));

        //then
        assertThat(orderTable1.getTableGroup()).isEqualTo(tableGroup);
        assertThat(orderTable2.getTableGroup()).isEqualTo(tableGroup);
    }

    @DisplayName("그룹해제")
    @Test
    public void 그룹해제_확인() throws Exception {
        //given
        OrderTable orderTable1 = new OrderTable(5, true);
        OrderTable orderTable2 = new OrderTable(5, true);
        TableGroup tableGroup = new TableGroup(Arrays.asList(orderTable1, orderTable2));

        //when
        tableGroup.ungroup();

        //then
        assertThat(orderTable1.getTableGroup()).isNull();
        assertThat(orderTable2.getTableGroup()).isNull();
    }

    @DisplayName("그룹해제 예외 - 계산완료가 안된 테이블이 존재할 경우")
    @Test
    public void 계산완료가안된테이블이존재하는경우_그룹해제_예외() throws Exception {
        //given
        OrderTable orderTable1 = new OrderTable(5, true);
        OrderTable orderTable2 = new OrderTable(5, true);
        TableGroup tableGroup = new TableGroup(Arrays.asList(orderTable1, orderTable2));
        new Order(orderTable1, Arrays.asList(new OrderLineItem(1L, 1L)));

        assertThatThrownBy(() -> tableGroup.ungroup())
                .hasMessage("계산완료가 안된 테이블이 존재합니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }
}

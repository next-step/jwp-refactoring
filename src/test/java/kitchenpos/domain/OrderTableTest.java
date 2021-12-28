package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    @DisplayName("주문테이블 생성")
    public void createOrderTableTest() {
        //when
        OrderTable orderTable = new OrderTable();

        //then
        assertThat(orderTable).isNotNull();
    }

    @Test
    @DisplayName("테이블이 그룹화되어있는지 확인")
    public void isGrouped() {
        //given
        TableGroup tableGroup = new TableGroup();
        OrderTable orderTable = new OrderTable(1L, 0, tableGroup);

        //when
        boolean result = orderTable.isGrouped();

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("테이블이 빈테이블인지 확인")
    public void isNotUse() {
        //given
        OrderTable orderTable = new OrderTable(0, true);

        //when
        boolean result = orderTable.isEmpty();

        //then
        assertThat(result).isTrue();
    }

}

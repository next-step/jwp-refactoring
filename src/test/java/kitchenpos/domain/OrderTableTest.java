package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import org.aspectj.weaver.ast.Or;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    private OrderTable orderTable;

    @BeforeEach
    public void init(){
        orderTable = new OrderTable(0, true);
    }

    @Test
    @DisplayName("테이블을 비어있게 만든다")
    public void changeTableStatusEmptyTest(){
        //when
        orderTable.clearTable();

        //then
        assertThat(orderTable.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("테이블 그룹에 속해있으면 비어있는 유무를 설정할수 없다.")
    public void changeTableStatusWhileGroupThrowErrorTest(){
        //given
        orderTable.mapToTableGroup(1L);

        //when & then
        assertThatThrownBy(() -> orderTable.clearTable()).isInstanceOf(
            IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 그룹에 속해있을때 테이블 그룹에서 제외할 수 있다.")
    public void unGroupTest(){
        //given
        orderTable.mapToTableGroup(1L);

        //when
        orderTable.unGroupTable();

        //then
        assertAll(
            () -> assertThat(orderTable.isEmpty()).isTrue(),
            () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(0),
            () -> assertThat(orderTable.getTableGroupId()).isNull()
        );
    }

    @Test
    @DisplayName("테이블 그룹을 설정할수 있다.")
    public void mapTableGroupTest(){
        //when
        orderTable.mapToTableGroup(1L);

        //then
        assertAll(
            () -> assertThat(orderTable.getTableGroupId()).isNotNull(),
            () -> assertThat(orderTable.getTableGroupId()).isEqualTo(1L)
        );
    }

    @Test
    @DisplayName("손님 수를 설정할수 있다.")
    public void changeNumberOfGuestTest(){
        //given
        orderTable.useTable();

        //when
        orderTable.changeNumberOfGuests(3);

        //then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(3);
    }

    @Test
    @DisplayName("테이블이 비어있으면 손님수를 설정할수 없다.")
    public void cantChangeNumberOfGuestWhileEmptyTest(){
        //when & then
        assertThatThrownBy(() -> orderTable.changeNumberOfGuests(3)).isInstanceOf(
            IllegalArgumentException.class);
    }
}
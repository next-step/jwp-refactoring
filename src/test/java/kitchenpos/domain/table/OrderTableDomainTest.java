package kitchenpos.domain.table;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.Orders;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.exception.order.HasNotCompletionOrderException;
import kitchenpos.exception.table.EmptyOrderTableException;
import kitchenpos.exception.table.HasOtherTableGroupException;
import kitchenpos.exception.table.NegativeOfNumberOfGuestsException;

public class OrderTableDomainTest {
    @DisplayName("주문테이블이 그룹이 해제된다.")
    @Test
    void unGroup_ordertable() {
        // given
        OrderTable 주문테이블1 = OrderTable.of(0, true);
        OrderTable 주문테이블2 = OrderTable.of(0, true);
        TableGroup.of(OrderTables.of(List.of(주문테이블1, 주문테이블2)));
        
        Orders 주문 = Orders.of(OrderStatus.COMPLETION);
        
        // when
        주문테이블1.unGroupTable(주문);

        // then
        Assertions.assertThat(주문테이블1.getTableGroup()).isNull();
    }


    @DisplayName("주문테이블이 그룹화 된다.")
    @Test
    void group_ordertable() {
        // given
        OrderTable 주문테이블1 = OrderTable.of(0, true);
        OrderTable 주문테이블2 = OrderTable.of(0, true);

        // when
        TableGroup 치킨_주문_단체테이블 = TableGroup.of(OrderTables.of(List.of(주문테이블1, 주문테이블2)));       
        
        // then
        Assertions.assertThat(주문테이블1.getTableGroup()).isEqualTo(치킨_주문_단체테이블);
    }

    @DisplayName("주문테이블이 상태변경된다.")
    @Test
    void change_updateOrderTable_EmptyStatus() {
        // given
        OrderTable 주문테이블1 = OrderTable.of(10, false);
        
        Orders 주문 = Orders.of(OrderStatus.COMPLETION);
        
        // when
        주문테이블1.changeEmpty(true, 주문);

        // then
        Assertions.assertThat(주문테이블1.getEmpty()).isTrue();
    }

    @DisplayName("주문테이블의 고객수가 변경된다.")
    @Test
    void change_numberOfGuests() {
        // given
        OrderTable 치킨_주문_단체테이블 = OrderTable.of(10, false);

        // when
        치킨_주문_단체테이블.changeNumberOfGuests(3);

        // then
        Assertions.assertThat(치킨_주문_단체테이블.getNumberOfGuests()).isEqualTo(3);
        
    }

    @DisplayName("주문상태가 계산완료가 아닌 주문테이블의 빈테이블 상태변경시 예외가 발생된다.")
    @Test
    void exception_updateOrderTable_EmptyStatus_AboutNotCompletionOrderStatus() {
        // given
        OrderTable 주문테이블 = OrderTable.of(10, false);
        
        // when
        // then
        Assertions.assertThatExceptionOfType(HasNotCompletionOrderException.class)
                      .isThrownBy(() -> 주문테이블.changeEmpty(true, Orders.of(OrderStatus.COOKING)));
    }

    @DisplayName("주문상태가 계산완료가 아닌 주문테이블의 그룹해제시 예외가 발생된다.")
    @Test
    void exception_unGroup_notCompletionOrderStatus() {
        // given
        OrderTable 주문테이블 = OrderTable.of(10, false);
        
        // when
        // then
        Assertions.assertThatExceptionOfType(HasNotCompletionOrderException.class)
                    .isThrownBy(() -> 주문테이블.unGroupTable(Orders.of(OrderStatus.COOKING)));
    }

    @DisplayName("빈테이블여부 변경시 단체지정이 된 주문테이블일 경우 예외가 발생된다.")
    @Test
    void exception_chnageEmptyStable_existOrderTableInOtherTableGroup() {
        // given
        OrderTable 주문테이블1 = OrderTable.of(0, true);
        OrderTable 주문테이블2 = OrderTable.of(0, true);

        TableGroup.of(OrderTables.of(List.of(주문테이블1, 주문테이블2)));
   
        Orders 주문 = Orders.of(OrderStatus.COMPLETION);

        // when
        // then
        Assertions.assertThatExceptionOfType(HasOtherTableGroupException.class)
                    .isThrownBy(() -> 주문테이블1.changeEmpty(true, 주문));
    }

    @DisplayName("빈테이블에 방문한 손님수 변경시 예외가 발생된다.")
    @Test
    void exception_updateOrderTable_atEmptyTable() {
        // given
        OrderTable 주문테이블 = OrderTable.of(0, true);

        // when
        // then
        Assertions.assertThatExceptionOfType(EmptyOrderTableException.class)
                   .isThrownBy(() -> 주문테이블.changeNumberOfGuests(3));
    }

    @DisplayName("주문테이블의 방문한 손님수를 0이만으로 변경시 예외가 발생된다.")
    @ValueSource(ints = {-1, -9})
    @ParameterizedTest(name ="[{index}] 방문한 손님수는 [{0}]")
    void exception_updateOrderTable_underZeroCountAboutNumberOfGuest(int numberOfGuests) {
        // given
        OrderTable 주문테이블 = OrderTable.of(10, false);

        // when
        // then
        Assertions.assertThatExceptionOfType(NegativeOfNumberOfGuestsException.class)
                    .isThrownBy(() -> 주문테이블.changeNumberOfGuests(numberOfGuests));

    }
}

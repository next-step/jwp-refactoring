package kitchenpos.table;

import static kitchenpos.util.TestDataSet.산악회;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.constant.OrderStatus;
import kitchenpos.order.Order;

public class OrderTableTest {

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable(1L, 산악회.getId(), 10, false);
    }

    @Test
    @DisplayName("손님은 0명 미만일 경우 에러를 출력한다.")
    void underZero() {
        // then
        assertThrows(IllegalArgumentException.class, () -> {
            orderTable.updateGuests(-1);
        });
    }

    @Test
    @DisplayName("테이블이 비었는데 테이블 인원 수 수정시 에러를 출력한다.")
    void emptyTable() {
        //given
        OrderTable 빈_테이블 = new OrderTable(1L, 산악회.getId(), 10, true);
        // then
        assertThrows(IllegalArgumentException.class, () -> {
            빈_테이블.updateGuests(10);
        });
    }

    @Test
    @DisplayName("주문_테이블에 속한 주문들이 현재 조리중이거나, 식사 중일 시 에러를 출력한다.")
    void isEating() {
        //given
        Order 쿠킹_주문 = new Order(1L, OrderStatus.COOKING, 1L, null);
        Order 먹는상태_주문 = new Order(1L, OrderStatus.MEAL, 1L, null);
        OrderTable 쿠킹_테이블 = new OrderTable(1L, null, 10, false, Arrays.asList(쿠킹_주문));
        OrderTable 먹는상태_테이블 = new OrderTable(1L, null, 10, false, Arrays.asList(먹는상태_주문));

        // then
        assertThrows(IllegalArgumentException.class, () -> {
            쿠킹_테이블.updateEmpty(false);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            먹는상태_테이블.updateEmpty(false);
        });

    }
}

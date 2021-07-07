package kitchenpos.domain;

import static kitchenpos.domain.MenuTest.*;
import static kitchenpos.domain.OrderStatus.*;
import static kitchenpos.domain.TableGroupTest.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.exception.OrderNotCompletedException;

public class OrderTableTest {
    public static OrderTable 테이블1 = new OrderTable(1L, 그룹1, 0, true);
    public static OrderTable 테이블2 = new OrderTable(2L, 그룹1, 0, true);
    public static OrderTable 테이블3 = new OrderTable(3L, 0, true);
    public static OrderTable 테이블4 = new OrderTable(4L, 0, true);
    public static OrderTable 테이블5 = new OrderTable(5L, 0, true);
    public static OrderTable 테이블6 = new OrderTable(6L, 0, true);
    public static OrderTable 테이블7 = new OrderTable(7L, 0, true);
    public static OrderTable 테이블8 = new OrderTable(8L, 0, true);
    public static OrderTable 테이블9_사용중 = new OrderTable(9L, null, 4, false);
    public static OrderTable 테이블10_사용중 = new OrderTable(10L, null, 8, false);
    public static OrderTable 테이블11_사용중 = new OrderTable(11L, null, 2, false);
    public static OrderTable 테이블12_사용중_주문전 = new OrderTable(12L, 2, false);


    @Test
    @DisplayName("특정 테이블의 테이블 상태를 변경한다")
    void changeEmpty() {
        // when
        테이블8.changeEmpty(false);

        // then
        assertThat(테이블8.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("테이블 상태 변경 실패(테이블이 그룹에 포함되어 있음)")
    void changeEmpty_failed2() {
        // then
        assertThatThrownBy(() -> 테이블1.changeEmpty(false))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 상태 변경 실패(테이블이 조리/식사 상태)")
    void changeEmpty_failed3() {
        // given
        OrderLineItem 더미1 = new OrderLineItem(후라이드_메뉴, 1);
        OrderLineItem 더미2 = new OrderLineItem(양념치킨_메뉴, 1);
        테이블12_사용중_주문전.addOrder(new Order(테이블12_사용중_주문전, COOKING, Arrays.asList(더미1, 더미2)));

        // then
        assertThatThrownBy(() -> 테이블12_사용중_주문전.changeEmpty(true))
            .isInstanceOf(OrderNotCompletedException.class);
    }

}

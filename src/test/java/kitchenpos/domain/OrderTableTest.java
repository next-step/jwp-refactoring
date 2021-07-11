package kitchenpos.domain;

import static kitchenpos.domain.MenuTest.*;
import static kitchenpos.domain.OrderLineItemDetailTest.*;
import static kitchenpos.domain.OrderStatus.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.exception.IllegalOperationException;
import kitchenpos.exception.OrderNotCompletedException;

@DisplayName("주문 테이블 단위 테스트")
public class OrderTableTest {
    public static OrderTable 테이블1 = new OrderTable(1L, NumberOfGuests.of(0), false);
    public static OrderTable 테이블2 = new OrderTable(2L, NumberOfGuests.of(0), false);
    public static OrderTable 테이블3 = new OrderTable(3L, NumberOfGuests.of(0), true);
    public static OrderTable 테이블4 = new OrderTable(4L, NumberOfGuests.of(0), true);
    public static OrderTable 테이블5 = new OrderTable(5L, NumberOfGuests.of(0), true);
    public static OrderTable 테이블6 = new OrderTable(6L, NumberOfGuests.of(0), true);
    public static OrderTable 테이블7 = new OrderTable(7L, NumberOfGuests.of(0), true);
    public static OrderTable 테이블8 = new OrderTable(8L, NumberOfGuests.of(0), true);
    public static OrderTable 테이블9_사용중 = new OrderTable(9L, NumberOfGuests.of(4), false);
    public static OrderTable 테이블10_사용중 = new OrderTable(10L, NumberOfGuests.of(8), false);
    public static OrderTable 테이블11_사용중 = new OrderTable(11L, NumberOfGuests.of(2), false);
    public static OrderTable 테이블12_사용중_주문전 = new OrderTable(12L, NumberOfGuests.of(2), false);

    public static OrderTable orderTable(Long id, NumberOfGuests numberOfGuests, boolean empty) {
        return new OrderTable(id, numberOfGuests, empty);
    }

    @Test
    @DisplayName("테이블 그룹을 할당한다")
    void withTableGroup() {
        OrderTable 테이블1 = new OrderTable(1L, NumberOfGuests.of(0), true);
        OrderTable 테이블2 = new OrderTable(2L, NumberOfGuests.of(0), true);
        TableGroup 그룹 = new TableGroup(OrderTables.of(테이블1, 테이블2));

        assertThat(테이블1.getTableGroup().get()).isEqualTo(그룹);
        assertThat(테이블2.getTableGroup().get()).isEqualTo(그룹);
        assertThat(테이블1.isEmpty()).isFalse();
        assertThat(테이블2.isEmpty()).isFalse();

        assertThatThrownBy(() -> 테이블1.withTableGroup(그룹))
            .isInstanceOf(IllegalOperationException.class);
    }

    @Test
    void addOrder() {
        OrderTable 테이블 = new OrderTable(1L, NumberOfGuests.of(0), false);
        Order 주문 = new Order();
        테이블.addOrder(주문);

        assertThat(주문.getOrderTable()).isEqualTo(테이블);
    }

    @Test
    @DisplayName("테이블이 비어있을 경우 주문 추가 실패")
    void addOrder_failed() {
        OrderTable 테이블 = new OrderTable(1L, NumberOfGuests.of(0), true);
        assertThatThrownBy(() -> 테이블.addOrder(new Order()))
            .isInstanceOf(IllegalOperationException.class);
    }

    @Test
    @DisplayName("테이블 상태를 변경한다")
    void changeEmpty() {
        OrderTable 테이블A = new OrderTable(1L, NumberOfGuests.of(0), true);
        테이블A.changeEmpty(false);

        assertThat(테이블A.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("테이블이 그룹에 포함되어 있으면 상태 변경 실패")
    void changeEmpty_failed2() {
        OrderTable 테이블A = new OrderTable(1L, NumberOfGuests.of(0), true);
        OrderTable 테이블B = new OrderTable(2L, NumberOfGuests.of(0), true);
        TableGroup 그룹 = new TableGroup(1L, OrderTables.of(테이블A, 테이블B));
        assertThatThrownBy(() -> 테이블A.changeEmpty(false))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블이 조리/식사 상태이면 상태 변경 실패")
    void changeEmpty_failed3() {
        OrderLineItem 후라이드_주문내역 = new OrderLineItem(후라이드_메뉴, 후라이드_메뉴.getName(), 후라이드_메뉴.getPrice(), Quantity.valueOf(1),
            OrderLineItemDetails.of(후라이드_주문내역_상세));
        OrderLineItem 양념치킨_주문내역 = new OrderLineItem(양념치킨_메뉴, 양념치킨_메뉴.getName(), 양념치킨_메뉴.getPrice(), Quantity.valueOf(1),
            OrderLineItemDetails.of(양념치킨_주문내역_상세));
        테이블12_사용중_주문전.addOrder(new Order(COOKING, OrderLineItems.of(후라이드_주문내역, 양념치킨_주문내역)));

        assertThatThrownBy(() -> 테이블12_사용중_주문전.changeEmpty(true))
            .isInstanceOf(OrderNotCompletedException.class);
    }

    @Test
    @DisplayName("손님 숫자가 변경된다")
    void changeNumberOfGuests() {
        OrderTable 테이블 = new OrderTable(1L, NumberOfGuests.of(0), false);

        NumberOfGuests 백명 = NumberOfGuests.of(100);
        테이블.changeNumberOfGuests(백명);

        assertThat(테이블.getNumberOfGuests()).isEqualTo(백명);
    }

    @Test
    @DisplayName("테이블이 비어있다면 손님 숫자를 변경할 수 없다")
    void changeNumberOfGuests_failed() {
        OrderTable 테이블 = new OrderTable(1L, NumberOfGuests.of(0), true);

        NumberOfGuests 백명 = NumberOfGuests.of(100);
        assertThatThrownBy(() -> 테이블.changeNumberOfGuests(백명))
            .isInstanceOf(IllegalOperationException.class);
    }

    @Test
    @DisplayName("진행중인 주문이 있다면 그룹핑을 해제할 수 없다")
    void leaveTableGroup() {
        OrderLineItem 후라이드_주문내역 = new OrderLineItem(후라이드_메뉴, 후라이드_메뉴.getName(), 후라이드_메뉴.getPrice(), Quantity.valueOf(1),
            OrderLineItemDetails.of(후라이드_주문내역_상세));
        OrderLineItem 양념치킨_주문내역 = new OrderLineItem(양념치킨_메뉴, 양념치킨_메뉴.getName(), 양념치킨_메뉴.getPrice(), Quantity.valueOf(1),
            OrderLineItemDetails.of(양념치킨_주문내역_상세));
        테이블12_사용중_주문전.addOrder(new Order(COOKING, OrderLineItems.of(후라이드_주문내역, 양념치킨_주문내역)));

        assertThatThrownBy(() -> 테이블12_사용중_주문전.leaveTableGroup())
            .isInstanceOf(OrderNotCompletedException.class);
    }
}

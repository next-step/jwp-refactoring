package kitchenpos.ordertable.domain;

import kitchenpos.order.domain.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("테이블 컬렉션 테스트")
class OrderTablesTest {

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        orderTable1 = new OrderTable(true);
        orderTable2 = new OrderTable(true);
    }

    @Test
    @DisplayName("테이블 컬렉션을 생성한다.")
    void create() {
        // when
        OrderTables orderTables = new OrderTables(Arrays.asList(orderTable1, orderTable2));

        // then
        assertThat(orderTables).isEqualTo(new OrderTables(Arrays.asList(orderTable1, orderTable2)));
    }

    @Test
    @DisplayName("1개 이하의 테이블로 테이블 컬렉션을 생성하면 예외가 발생한다.")
    void createThrowException1() {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new OrderTables(Collections.singletonList(orderTable1)))
                .withMessageMatching(OrderTables.MESSAGE_VALIDATE_MIN_COUNT);
    }

    @Test
    @DisplayName("비어있지 않은 테이블로 테이블 그룹을 등록하면 예외가 발생한다.")
    void groupThrowException1() {
        // given
        OrderTable orderTable3 = new OrderTable(false);
        OrderTables orderTables = new OrderTables(Arrays.asList(orderTable1, orderTable3));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderTables.group(1L))
                .withMessageMatching(OrderTables.MESSAGE_VALIDATE_GROUPABLE);
    }

    @Test
    @DisplayName("이미 테이블 그룹에 등록된 테이블로 테이블 그룹을 등록하면 예외가 발생한다.")
    void groupThrowException2() {
        // given
        orderTable1.group(2L);
        OrderTables orderTables = new OrderTables(Arrays.asList(orderTable1, orderTable2));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderTables.group(1L))
                .withMessageMatching(OrderTables.MESSAGE_VALIDATE_GROUPABLE);
    }

    @Test
    @DisplayName("테이블 그룹에서 테이블을 제거한다.")
    void ungroup() {
        // given
        OrderTables orderTables = new OrderTables(Arrays.asList(orderTable1, orderTable2));

        // when
        orderTables.ungroup(new OrderTableValidator(mock(OrderRepository.class)));

        // then
        assertThat(orderTable1.getTableGroupId()).isNull();
    }
}

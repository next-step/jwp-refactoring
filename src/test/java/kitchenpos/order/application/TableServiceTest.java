package kitchenpos.order.application;

import kitchenpos.order.application.TableService;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    OrderDao orderDao;
    @Mock
    OrderTableDao orderTableDao;
    @InjectMocks
    TableService tableService;

    OrderTable 주문테이블_테이블1;
    OrderTable 주문테이블_테이블2;
    OrderTable 주문테이블_상태변경테이블;
    OrderTable 주문테이블_인원변경테이블;

    long 존재하지않는아이디;

    @BeforeEach
    void setUp() {
        존재하지않는아이디 = 99L;

        주문테이블_테이블1 = new OrderTable();
        주문테이블_테이블1.setId(1L);

        주문테이블_테이블2 = new OrderTable();
        주문테이블_테이블2.setId(2L);

        주문테이블_상태변경테이블 = new OrderTable();
        주문테이블_상태변경테이블.setEmpty(false);

        주문테이블_인원변경테이블 = new OrderTable();
        주문테이블_인원변경테이블.setNumberOfGuests(4);
    }

    @Test
    @DisplayName("테이블을 생성한다.")
    void create() {
        //given
        when(orderTableDao.save(주문테이블_테이블1)).thenReturn(주문테이블_테이블1);

        //when
        OrderTable createdOrderTable = tableService.create(주문테이블_테이블1);

        //then
        assertThat(createdOrderTable.getId()).isEqualTo(주문테이블_테이블1.getId());
    }

    @Test
    @DisplayName("전체 테이블을 조회한다.")
    void list() {
        //given
        when(orderTableDao.findAll()).thenReturn(Arrays.asList(주문테이블_테이블1, 주문테이블_테이블2));

        //when
        List<OrderTable> orderTables = tableService.list();

        //then
        assertThat(orderTables).containsExactly(주문테이블_테이블1, 주문테이블_테이블2);
    }

    @Test
    @DisplayName("테이블의 상태를 변경한다.")
    void changeEmpty() {
        //given
        OrderTable 주문테이블_상태변경테이블 = new OrderTable();
        주문테이블_상태변경테이블.setEmpty(false);

        when(orderTableDao.findById(주문테이블_테이블1.getId())).thenReturn(Optional.of(주문테이블_테이블1));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(
                주문테이블_테이블1.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .thenReturn(false);
        when(orderTableDao.save(주문테이블_테이블1)).thenReturn(주문테이블_테이블1);

        //when
        OrderTable changedOrderTable = tableService.changeEmpty(주문테이블_테이블1.getId(), 주문테이블_상태변경테이블);

        //then
        assertThat(changedOrderTable.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("존재하지않는 테이블의 상태변경은 실패한다.")
    void changeEmpty_with_exception_when_not_exist_orderTableId() {
        //when && then
        assertThatThrownBy(() -> tableService.changeEmpty(존재하지않는아이디, 주문테이블_상태변경테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("그룹테이블로 지정되어있을경우 상태변경은 실패한다.")
    void changeEmpty_with_exception() {
        //when && then
        assertThatThrownBy(() -> tableService.changeEmpty(주문테이블_테이블1.getId(), 주문테이블_상태변경테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("조리 또는 식사중일 경우 상태변경은 실패한다.")
    void changeEmpty_when_orderStatus_in_cooking_or_meal() {
        //given
        when(orderTableDao.findById(주문테이블_테이블1.getId())).thenReturn(Optional.of(주문테이블_테이블1));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(
                주문테이블_테이블1.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .thenReturn(true);

        //when && then
        assertThatThrownBy(() -> tableService.changeEmpty(주문테이블_테이블1.getId(), 주문테이블_상태변경테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블 손님 인원을 변경한다.")
    void changeNumberOfGuests() {
        //given
        when(orderTableDao.findById(주문테이블_테이블1.getId())).thenReturn(Optional.of(주문테이블_테이블1));
        when(orderTableDao.save(주문테이블_테이블1)).thenReturn(주문테이블_테이블1);

        //when
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(주문테이블_테이블1.getId(), 주문테이블_인원변경테이블);

        //then
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(4);
    }

    @Test
    @DisplayName("변경인원이 0명 미만일 경우 변경은 실패한다.")
    void changeNumberOfGuests_with_exception_when_person_smaller_than_zero() {
        //given
        주문테이블_인원변경테이블.setNumberOfGuests(-1);

        //when && then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문테이블_테이블1.getId(), 주문테이블_인원변경테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("없는테이블의 인원을 변경할 경우 변경은 실패한다.")
    void changeNumberOfGuests_with_exception_when_not_exist_orderTableId() {
        //when && then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(존재하지않는아이디, 주문테이블_인원변경테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("비어있는 테이블의 인원을 변경할 경우 변경은 실패한다.")
    void changeNumberOfGuests_with_exception_when_orderTable_isEmpty() {
        //given
        주문테이블_테이블1.setEmpty(true);

        when(orderTableDao.findById(주문테이블_테이블1.getId())).thenReturn(Optional.of(주문테이블_테이블1));

        //when && then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문테이블_테이블1.getId(), 주문테이블_인원변경테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
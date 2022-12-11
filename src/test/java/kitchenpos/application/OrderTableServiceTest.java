package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
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

import static kitchenpos.domain.OrderTableTestFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@DisplayName("주문 테이블 관련 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class OrderTableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;

    @BeforeEach
    void setUp() {
        주문테이블1 = 주문테이블1();
        주문테이블2 = 주문테이블2();
    }

    @DisplayName("주문 테이블 생성 작업을 성공한다.")
    @Test
    void create() {
        // given
        when(orderTableDao.save(주문테이블1)).thenReturn(주문테이블1);

        // when
        OrderTable orderTable = tableService.create(주문테이블1);

        // then
        assertThat(orderTable.getTableGroupId()).isNull();
    }

    @DisplayName("주문 테이블 전체 목록 조회 작업을 성공한다.")
    @Test
    void list() {
        // given
        List<OrderTable> expected = Arrays.asList(주문테이블1, 주문테이블2);
        when(orderTableDao.findAll()).thenReturn(expected);

        // when
        List<OrderTable> actual = tableService.list();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(expected.size()),
                () -> assertThat(actual).containsExactly(주문테이블1, 주문테이블2)
        );
    }

    @DisplayName("주문 테이블 빈좌석 여부에 대한 변경 작업을 성공한다.")
    @Test
    void changeEmpty() {
        // given
        주문테이블1.setEmpty(true);
        when(orderTableDao.findById(주문테이블1.getId())).thenReturn(Optional.of(주문테이블1));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(주문테이블1.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);
        when(orderTableDao.save(주문테이블1)).thenReturn(주문테이블1);

        // when
        OrderTable orderTable = tableService.changeEmpty(주문테이블1.getId(), 주문테이블1);

        // then
        assertThat(orderTable.isEmpty()).isEqualTo(주문테이블1.isEmpty());
    }

    @DisplayName("테이블의 빈좌석 상태를 변경할때, 테이블이 단체 지정이 되어 있으면 IllegalArgumentException을 반환한다.")
    @Test
    void changeEmptyWithException1() {
        // given
        OrderTable orderTable = createOrderTable(1L, 1L, 10, true);
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableService.changeEmpty(orderTable.getId(), orderTable));
    }

    @DisplayName("테이블의 빈좌석 상태를 변경할때, 테이블이 주문 상태가 조리중이거나 식사중이면 IllegalArgumentException을 반환한다.")
    @Test
    void changeEmptyWithException2() {
        // given
        when(orderTableDao.findById(주문테이블1.getId())).thenReturn(Optional.of(주문테이블1));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(주문테이블1.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableService.changeEmpty(주문테이블1.getId(), 주문테이블1));
    }

    @DisplayName("주문 테이블 고객 인원 수에 대한 변경 작업을 성공한다.")
    @Test
    void changeNumberOfGuestsInTable() {
        // given
        주문테이블1.setNumberOfGuests(10);
        when(orderTableDao.findById(주문테이블1.getId())).thenReturn(Optional.of(주문테이블1));
        when(orderTableDao.save(주문테이블1)).thenReturn(주문테이블1);

        // when
        OrderTable orderTable = tableService.changeNumberOfGuests(주문테이블1.getId(), 주문테이블1);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
    }

    @DisplayName("고객 인원을 변경할 때, 인원 수가 0보다 작으면 IllegalArgumentException을 반환한다.")
    @Test
    void changeNumberOfGuestsWithException1() {
        // given
        OrderTable orderTable = createOrderTable(3L, null, -1, false);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable));
    }


    @DisplayName("고객 인원을 변경할 때, 주문 테이블이 비어있으면 IllegalArgumentException을 반환한다.")
    @Test
    void changeNumberOfGuestsWithException2() {
        // given
        OrderTable orderTable = createOrderTable(3L, 1L, 10, true);
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable));
    }
}

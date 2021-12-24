package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @DisplayName("주문 테이블을 등록한다.")
    @Test
    void 주문테이블_등록() {
        // given
        OrderTable expected = new OrderTable(1L, 5);
        given(orderTableDao.save(expected))
            .willReturn(expected);

        // when
        OrderTable actual = tableService.create(expected);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void 주문테이블_목록_조회() {
        // given
        OrderTable orderTable1 = new OrderTable(1L, 3);
        OrderTable orderTable2 = new OrderTable(2L, 2);
        List<OrderTable> expected = Arrays.asList(orderTable1, orderTable2);

        given(orderTableDao.findAll())
            .willReturn(expected);

        // when
        List<OrderTable> actual = tableService.list();

        // then
        assertThat(actual).containsAll(expected);
    }

    @DisplayName("주문테이블을 빈테이블로 변경할 수 있다.")
    @Test
    void 빈_테이블로_변경() {
        // given
        Long orderTableId = 1L;
        OrderTable orderTable = new OrderTable(orderTableId, 3);
        OrderTable expected = new OrderTable(orderTableId, 3, true);

        given(orderTableDao.findById(orderTableId))
            .willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId,
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
            .willReturn(false);
        given(orderTableDao.save(any()))
            .willReturn(expected);

        // when
        OrderTable actual = tableService.changeEmpty(orderTableId, orderTable);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("주문 테이블이 등록되어 있지 않으면 빈 테이블로 변경할 수 없다.")
    @Test
    void 빈_테이블_변경_예외_존재하지_않는_주문_테이블() {
        // given
        Long orderTableId = 1L;
        OrderTable orderTable = new OrderTable(orderTableId, 3);

        given(orderTableDao.findById(orderTableId))
            .willReturn(Optional.empty());

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> tableService.changeEmpty(orderTableId, orderTable)
        );
    }

    @DisplayName("주문 테이블 그룹이 단체 지정 되어있으면 빈 테이블로 변경할 수 없다.")
    @Test
    void 빈_테이블_변경_예외_단체_지정됨() {
        // given
        Long orderTableId = 1L;
        Long orderTableGroupId = 5L;
        OrderTable orderTable = new OrderTable(orderTableId, 3, orderTableGroupId);

        given(orderTableDao.findById(orderTableId))
            .willReturn(Optional.of(orderTable));

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> tableService.changeEmpty(orderTableId, orderTable)
        );
    }

    @DisplayName("주문 테이블이 '조리' 나 '식사' 상태이면 빈 테이블로 변경할 수 없다.")
    @Test
    void 빈_테이블_변경_예외_조리_또는_식사_상태() {
        // given
        Long orderTableId = 1L;
        OrderTable orderTable = new OrderTable(orderTableId, 3);

        given(orderTableDao.findById(orderTableId))
            .willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId,
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
            .willReturn(true);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> tableService.changeEmpty(orderTableId, orderTable)
        );
    }

    @DisplayName("주문 테이블의 방문한 손님 수를 변경할 수 있다.")
    @Test
    void 테이블_인원_변경() {
        // given
        Long orderTableId = 1L;
        OrderTable orderTable = new OrderTable(orderTableId, 3);
        OrderTable expected = new OrderTable(orderTableId, 5);

        given(orderTableDao.findById(orderTableId))
            .willReturn(Optional.of(orderTable));
        given(orderTableDao.save(any()))
            .willReturn(expected);

        // when
        OrderTable actual = tableService.changeNumberOfGuests(orderTableId, expected);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("변경할 손님 수가 0보다 작으면 주문 테이블의 방문한 손님 수를 변경할 수 없다.")
    @Test
    void 테이블_인원_변경_예외_인원_0보다_작음() {
        // given
        Long orderTableId = 1L;
        OrderTable expected = new OrderTable(orderTableId, -2);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> tableService.changeNumberOfGuests(orderTableId, expected)
        );
    }

    @DisplayName("주문 테이블이 빈 테이블이면 주문 테이블의 방문한 손님 수를 변경할 수 없다.")
    @Test
    void 테이블_인원_변경_예외_빈테이블() {
        // given
        Long orderTableId = 1L;
        OrderTable orderTable = new OrderTable(orderTableId, 3, true);
        OrderTable expected = new OrderTable(orderTableId, 5);

        given(orderTableDao.findById(orderTableId))
            .willReturn(Optional.of(orderTable));

        // when, then
        assertThatIllegalArgumentException().isThrownBy(
            () -> tableService.changeNumberOfGuests(orderTableId, expected)
        );
    }

}

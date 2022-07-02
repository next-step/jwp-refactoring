package kitchenpos.application;

import kitchenpos.dao.OrderRepository;
import kitchenpos.dao.OrderTableRepository;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private TableService tableService;

    @Test
    void 주문_테이블을_등록한다() {
        // given
        OrderTable orderTable = new OrderTable(0, true);
        given(orderTableRepository.save(orderTable))
                .willReturn(createOrderTable());

        // when
        OrderTable result = tableService.create(orderTable);

        // then
        assertThat(result.getId()).isNotNull();
    }

    @Test
    void 주문_테이블_목록을_조회한다() {
        // given
        given(orderTableRepository.findAll())
                .willReturn(Arrays.asList(new OrderTable(1L), new OrderTable(2L)));

        // when
        List<OrderTable> result = tableService.list();

        // then
        assertThat(result).hasSize(2);
    }

    @Test
    void 단체_지정이_되어_있으면_이용_여부를_변경할_수_없다() {
        // given
        OrderTable orderTable = new OrderTable(0, true);
        given(orderTableRepository.findById(1L))
                .willReturn(Optional.of(orderTable));

        // when & then
        assertThatThrownBy(() ->
                tableService.changeEmpty(1L, orderTable)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 지정이 되어 있는 테이블은 이용 여부를 변경할 수 없습니다.");
    }

    @Test
    void 조리_또는_식사_중인_테이블은_이용_여부를_변경할_수_없다() {
        // given
        OrderTable orderTable = createOrderTable();
        given(orderTableRepository.findById(1L))
                .willReturn(Optional.of(orderTable));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(1L, OrderStatus.findNotCompletionStatus()))
                .willReturn(true);

        // when & then
        assertThatThrownBy(() ->
                tableService.changeEmpty(1L, orderTable)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("조리 또는 식사 중인 테이블은 이용 여부를 변경할 수 없습니다.");
    }

    @Test
    void 테이블_이용_여부를_변경한다() {
        // given
        OrderTable orderTable = createOrderTable();
        given(orderTableRepository.findById(1L))
                .willReturn(Optional.of(orderTable));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(1L, OrderStatus.findNotCompletionStatus()))
                .willReturn(false);

        // when
        tableService.changeEmpty(1L, orderTable);

        // then
        then(orderTableRepository).should().save(orderTable);
    }

    @Test
    void 방문한_손님의_수가_0보다_작으면_손님의_수를_변경할_수_없다() {
        // given
        OrderTable orderTable = new OrderTable(-1, true);

        // when & then
        assertThatThrownBy(() ->
                tableService.changeNumberOfGuests(1L, orderTable)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("방문한 손님의 수가 0보다 작으면 손님의 수를 변경할 수 없습니다.");
    }

    @Test
    void 빈_테이블이면_방문한_손님_수를_변경할_수_없다() {
        // given
        given(orderTableRepository.findById(1L))
                .willReturn(Optional.of(createOrderTable()));

        // when & then
        assertThatThrownBy(() ->
                tableService.changeNumberOfGuests(1L, createOrderTable())
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블이면 방문한 손님 수를 변경할 수 없습니다.");
    }

    @Test
    void 방문한_손님의_수를_변경한다() {
        // given
        OrderTable orderTable = new OrderTable(0, false);
        given(orderTableRepository.findById(1L))
                .willReturn(Optional.of(orderTable));

        // when
        tableService.changeNumberOfGuests(1L, orderTable);

        // then
        then(orderTableRepository).should().save(orderTable);
    }

    private OrderTable createOrderTable() {
        return new OrderTable( 0, true);
    }
}

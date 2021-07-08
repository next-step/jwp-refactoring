package kitchenpos.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.dto.*;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    @Mock
    OrderTableRepository orderTableRepository;
    @Mock
    OrderRepository orderRepository;

    TableService tableService;

    OrderTable orderTable;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderRepository, orderTableRepository);
        orderTable = new OrderTable(4, true);
    }

    @DisplayName("빈 주문 테이블을 생성한다.")
    @Test
    void 빈_주문_테이블을_생성() {
        //given
        OrderTable savedOrderTable = new OrderTable(1L, new TableGroup(1L, LocalDateTime.now()), 4, true);
        given(orderTableRepository.save(any())).willReturn(savedOrderTable);

        //when
        OrderTableRequest orderTableRequest = new OrderTableRequest(4, true);
        OrderTableResponse createOrderTable = tableService.create(orderTableRequest);

        //then
        assertThat(createOrderTable.getId()).isNotNull();
        assertThat(createOrderTable.getNumberOfGuests()).isEqualTo(savedOrderTable.getNumberOfGuests());
        assertThat(createOrderTable.isEmpty()).isEqualTo(true);
    }

    @DisplayName("모든 주문 테이블의 항복을 불러옴")
    @Test
    void 모든_주문_테이블의_항목을_불러옴() {
        //given
        given(orderTableRepository.findAll()).willReturn(Arrays.asList(
                new OrderTable(1L, new TableGroup(1L, LocalDateTime.now()), 4, false),
                new OrderTable(2L, new TableGroup(2L, LocalDateTime.now()), 3, false)
        ));

        //when, then
        assertThat(tableService.list()).hasSize(2);
    }

    @DisplayName("테이블의 상태값을 변경한다.")
    @Test
    void 테이블의_상태값을_변경한다() {
        //given
        OrderTable savedOrderTable = new OrderTable(1L, null, 0, true);
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(savedOrderTable));
        given(orderRepository.existsByOrderTableAndOrderStatusIn(anyLong(), anyList())).willReturn(false);

        //when
        OrderTableEmptyChangeResponse resultOrderTable =
                tableService.changeEmpty(1L, new OrderTableEmptyChangeRequest(false));

        //then
        assertThat(resultOrderTable.isEmpty()).isEqualTo(false);
    }

    @DisplayName("주문 테이블 번호가 잘못 됨")
    @Test
    void 주문_테이블_번호가_잘못_됨() {
        //given
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTableEmptyChangeRequest(false)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("개별 주문 테이블이 그룹에 속해있음")
    @Test
    void 개별_주문_테이블이_그룹에_속해있음() {
        //given
        OrderTable savedOrderTable = new OrderTable(1L, new TableGroup(1L, LocalDateTime.now()), 4, false);
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(savedOrderTable));

        //when, then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTableEmptyChangeRequest(false)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문이 조리 또는 식사의 상태이면 안됨")
    @Test
    void 주문이_이미_들어가_있으면_안됨() {
        //given
        OrderTable savedOrderTable = new OrderTable(1L, null, 4, false);
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(savedOrderTable));
        given(orderRepository.existsByOrderTableAndOrderStatusIn(anyLong(), anyList())).willReturn(true);

        //when, then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTableEmptyChangeRequest(false)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님 숫자 정상적인 변경")
    @Test
    void 손님_숫자_정상적인_변경() {
        //given
        OrderTable savedOrderTable = new OrderTable(1L, null, 3, false);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(savedOrderTable));
        when(orderTableRepository.save(savedOrderTable)).thenAnswer(i -> i.getArgument(0));

        //when
        OrderTable resultOrderTable = tableService.changeNumberOfGuests(1L, new OrderTableGuestChangeRequest(5));

        //then
        assertThat(resultOrderTable.getNumberOfGuests()).isEqualTo(5);
    }

    @DisplayName("변경하려는 손님 숫자에 음수 입력")
    @Test
    void 변경하려는_손님_숫자에_음수_입력() {
        //when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, new OrderTableGuestChangeRequest(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("찾는 주문 테이블이 없는 경우")
    @Test
    void 찾는_주문_테이블이_없는_경우() {
        //given
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.empty());

        //when ,then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, new OrderTableGuestChangeRequest(1)))
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("사람이 없는 주문 테이블의 인원을 변경하려는 경우")
    @Test
    void 사람이_없는_주문_테이블의_인원을_변경하려는_경우() {
        //given
        OrderTable savedOrderTable = new OrderTable(1L, new TableGroup(1L, LocalDateTime.now()), 0, true);
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(savedOrderTable));

        //when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, new OrderTableGuestChangeRequest(1)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

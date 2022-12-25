package kitchenpos.application;

import static kitchenpos.common.exception.ErrorCode.EXISTS_NOT_COMPLETION_STATUS;
import static kitchenpos.common.exception.ErrorCode.NOT_BEEN_UNGROUP;
import static kitchenpos.common.exception.ErrorCode.NOT_EXISTS_TABLE;
import static kitchenpos.common.exception.ErrorCode.PEOPLE_LESS_THAN_ZERO;
import static kitchenpos.common.exception.ErrorCode.TABLE_IS_EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.common.exception.KitchenposException;
import kitchenpos.table.application.TableService;
import kitchenpos.table.application.validator.TableValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.response.OrderTableResponse;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableValidator tableValidator;
    @InjectMocks
    private TableService tableService;
    private OrderTable 주문_좌석;
    private OrderTable 공석_주문_좌석;
    private OrderTable 공석_변경_요청;
    private OrderTable 인원_변경_요청;
    private TableGroup 좌석_그룹;

    @BeforeEach
    void setUp() {
        주문_좌석 = new OrderTable(1L, null, 0, false);
        좌석_그룹 = new TableGroup(1L, Arrays.asList(주문_좌석));
        공석_주문_좌석 = new OrderTable(1L, null, 0, true);
        공석_변경_요청 = new OrderTable(1L, 좌석_그룹, 0, true);
        인원_변경_요청 = new OrderTable(1L, null, 4, false);
    }

    @Test
    void 생성() {
        given(orderTableRepository.save(any())).willReturn(주문_좌석);

        OrderTableResponse response = tableService.create(주문_좌석);

        assertAll(
                () -> assertThat(response.getNumberOfGuests()).isEqualTo(0),
                () -> assertThat(response.getTableGroup()).isNull(),
                () -> assertThat(response.isEmpty()).isFalse()
        );
    }

    @Test
    void 조회() {
        given(orderTableRepository.findAll()).willReturn(Arrays.asList(주문_좌석));

        List<OrderTableResponse> responses = tableService.list();

        assertThat(responses.size()).isEqualTo(1);
    }

    @Test
    void 공석으로_변경() {
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(주문_좌석));
        doNothing().when(tableValidator).validateChangeEmpty(any());

        OrderTableResponse response = tableService.changeEmpty(주문_좌석.getId(), true);

        assertThat(response.isEmpty()).isTrue();
    }

    @Test
    void 등록된_주문_좌석이_아닌_경우() {
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(
                () -> tableService.changeEmpty(주문_좌석.getId(), true)
        )
                .isInstanceOf(KitchenposException.class)
                .hasMessageContaining(NOT_EXISTS_TABLE.getDetail());
    }

    @Test
    void 좌석_그룹으로_등록된_경우() {
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(공석_변경_요청));
        doThrow(new KitchenposException(NOT_BEEN_UNGROUP)).when(tableValidator).validateChangeEmpty(any());

        assertThatThrownBy(
                () -> tableService.changeEmpty(공석_변경_요청.getId(), true)
        )
                .isInstanceOf(KitchenposException.class)
                .hasMessageContaining(NOT_BEEN_UNGROUP.getDetail());
    }

    @Test
    void 좌석_상태가_준비중이거나_식사중인_경우() {
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(주문_좌석));
        doThrow(new KitchenposException(EXISTS_NOT_COMPLETION_STATUS)).when(tableValidator).validateChangeEmpty(any());

        assertThatThrownBy(
                () -> tableService.changeEmpty(주문_좌석.getId(), true)
        )
                .isInstanceOf(KitchenposException.class)
                .hasMessageContaining(EXISTS_NOT_COMPLETION_STATUS.getDetail());
    }

    @Test
    void 좌석_인원_변경() {
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(주문_좌석));
        given(orderTableRepository.save(any())).willReturn(인원_변경_요청);

        OrderTableResponse response = tableService.changeNumberOfGuests(주문_좌석.getId(), 인원_변경_요청);

        assertThat(response.getNumberOfGuests()).isEqualTo(4);
    }

    @Test
    void 음수를_인원_변경을_요청한_경우() {
        인원_변경_요청 = new OrderTable(1L, null, -4, false);
        doThrow(new KitchenposException(PEOPLE_LESS_THAN_ZERO)).when(tableValidator).validateNumberOfGuests(anyInt());

        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(주문_좌석.getId(), 인원_변경_요청)
        )
                .isInstanceOf(KitchenposException.class)
                .hasMessageContaining(PEOPLE_LESS_THAN_ZERO.getDetail());
    }

    @Test
    void 주문한_좌석이_아닌_경우() {
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(주문_좌석.getId(), 인원_변경_요청)
        )
                .isInstanceOf(KitchenposException.class)
                .hasMessageContaining(NOT_EXISTS_TABLE.getDetail());
    }

    @Test
    void 공석인_경우() {
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(공석_주문_좌석));
        doThrow(new KitchenposException(TABLE_IS_EMPTY)).when(tableValidator).validateEmptyTrue(any());

        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(주문_좌석.getId(), 인원_변경_요청)
        )
                .isInstanceOf(KitchenposException.class)
                .hasMessageContaining(TABLE_IS_EMPTY.getDetail());
    }
}

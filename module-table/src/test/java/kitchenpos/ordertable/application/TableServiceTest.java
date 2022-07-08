package kitchenpos.ordertable.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.exception.IllegalOrderException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.dto.OrderTableEmptyRequest;
import kitchenpos.ordertable.dto.OrderTableNumOfGuestRequest;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.exception.IllegalOrderTableException;
import kitchenpos.ordertable.exception.NoSuchOrderTableException;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.utils.fixture.TableGroupFixtureFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static kitchenpos.utils.fixture.OrderTableFixtureFactory.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private TableService tableService;

    private OrderTable 테이블_EMPTY;
    private OrderTable 테이블_NOT_EMPTY;

    private TableGroup 테이블_그룹;
    private OrderTable 테이블_GROUPED;
    private OrderTable 테이블_GROUPED2;

    @BeforeEach
    void setUp() {
        테이블_EMPTY = createOrderTable(1L, null, 0, true);
        테이블_NOT_EMPTY = createOrderTable(1L, null, 4, false);
        테이블_GROUPED = createOrderTable(3L, null, 0, true);
        테이블_GROUPED2 = createOrderTable(4L, null, 0, true);
        테이블_그룹 = TableGroupFixtureFactory.createTableGroup(1L, Arrays.asList(테이블_GROUPED, 테이블_GROUPED2));
    }

    @DisplayName("주문테이블을 등록할 수 있다")
    @Test
    void 주문테이블_등록(){
        //given
        given(orderTableRepository.save(any(OrderTable.class))).willReturn(테이블_EMPTY);

        //when
        OrderTableRequest orderTableRequest = OrderTableRequest.of(0, true);
        OrderTableResponse savedOrderTable = tableService.create(orderTableRequest);

        //then
        assertThat(savedOrderTable).isEqualTo(OrderTableResponse.from(테이블_EMPTY));
    }

    @DisplayName("주문테이블의 목록을 조회할 수 있다")
    @Test
    void 주문테이블_목록_조회(){
        //given
        given(orderTableRepository.findAll()).willReturn(Arrays.asList(테이블_EMPTY));

        //when
        List<OrderTableResponse> list = tableService.list();

        //then
        assertThat(list).containsExactly(OrderTableResponse.from(테이블_EMPTY));
    }

    @DisplayName("주문테이블의 비어있음 여부를 업데이트할 수 있다")
    @ParameterizedTest(name = "이전 Empty 상태: {0}, 새로운 Empty 상태: {1}")
    @MethodSource("provideParametersForEmptyUpdate")
    void 주문테이블_Empty_업데이트(boolean before, boolean after){
        //given
        테이블_EMPTY.changeEmpty(before);
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(테이블_EMPTY));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(false);

        //when
        OrderTableEmptyRequest orderTableEmptyRequest = OrderTableEmptyRequest.from(after);
        tableService.changeEmpty(테이블_EMPTY.getId(), orderTableEmptyRequest);

        //then
        assertThat(테이블_EMPTY.isEmpty()).isEqualTo(after);
    }

    private static Stream<Arguments> provideParametersForEmptyUpdate() {
        return Stream.of(
                Arguments.of(true, false),
                Arguments.of(false, true)
        );
    }

    @DisplayName("주문테이블의 비어있음 여부를 업데이트시, 주문테이블이 존재해야 한다")
    @Test
    void 주문테이블_Empty_업데이트_주문테이블_검증(){
        //given
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));

        //when
        OrderTableEmptyRequest orderTableEmptyRequest = OrderTableEmptyRequest.from(false);

        //then
        assertThrows(NoSuchOrderTableException.class, () -> tableService.changeEmpty(테이블_EMPTY.getId(), orderTableEmptyRequest));
    }

    @DisplayName("주문테이블의 비어있음 여부를 업데이트시, 주문테이블이 테이블그룹에 등록되어있으면 안된다")
    @Test
    void 주문테이블_Empty_업데이트_주문테이블_테이블그룹_등록_검증(){
        //given
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(테이블_GROUPED));
        OrderTableEmptyRequest orderTableEmptyRequest = OrderTableEmptyRequest.from(false);

        //then
        assertThrows(IllegalOrderTableException.class,
                () -> tableService.changeEmpty(테이블_GROUPED.getId(), orderTableEmptyRequest));
    }

    @DisplayName("주문테이블의 비어있음 여부를 업데이트시, 주문테이블에 COOKING이나 MEAL 상태의 주문이 있으면 안된다")
    @Test
    void 주문테이블_Empty_업데이트_주문_상태_검증(){
        //given
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(테이블_EMPTY));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(true);
        OrderTableEmptyRequest orderTableEmptyRequest = OrderTableEmptyRequest.from(false);

        //then
        assertThrows(IllegalOrderException.class,
                () -> tableService.changeEmpty(테이블_EMPTY.getId(), orderTableEmptyRequest));
    }

    @DisplayName("주문테이블의 손님 수를 업데이트할 수 있다")
    @Test
    void 주문테이블_손님수_업데이트(){
        //given
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(테이블_NOT_EMPTY));

        //when
        OrderTableNumOfGuestRequest orderTableNumOfGuestRequest = OrderTableNumOfGuestRequest.from(10);
        tableService.changeNumberOfGuests(테이블_NOT_EMPTY.getId(), orderTableNumOfGuestRequest);

        //then
        assertThat(테이블_NOT_EMPTY.getNumberOfGuests()).isEqualTo(10);
    }

    @DisplayName("주문테이블의 손님 수를 업데이트 시, 손님 수는 0 이상이다")
    @Test
    void 주문테이블_손님수_업데이트_손님수_검증(){
        //given
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(테이블_NOT_EMPTY));

        //when
        OrderTableNumOfGuestRequest orderTableNumOfGuestRequest = OrderTableNumOfGuestRequest.from(-10);

        //then
        assertThrows(IllegalOrderTableException.class,
                () -> tableService.changeNumberOfGuests(테이블_NOT_EMPTY.getId(), orderTableNumOfGuestRequest));
    }

    @DisplayName("주문테이블의 손님 수를 업데이트 시, 업데이트할 주문테이블이 존재해야 한다")
    @Test
    void 주문테이블_손님수_업데이트_주문테이블_검증(){
        //given
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));

        //when
        OrderTableNumOfGuestRequest orderTableNumOfGuestRequest = OrderTableNumOfGuestRequest.from(10);

        //then
        assertThrows(NoSuchOrderTableException.class,
                () -> tableService.changeNumberOfGuests(테이블_NOT_EMPTY.getId(), orderTableNumOfGuestRequest));
    }

    @DisplayName("주문테이블의 손님 수를 업데이트 시, 주문테이블이 비어있으면 안된다")
    @Test
    void 주문테이블_손님수_업데이트_주문테이블_Empty_검증(){
        //given
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(테이블_EMPTY));

        //when
        OrderTableNumOfGuestRequest orderTableNumOfGuestRequest = OrderTableNumOfGuestRequest.from(10);

        //then
        assertThrows(IllegalOrderTableException.class,
                () -> tableService.changeNumberOfGuests(테이블_EMPTY.getId(), orderTableNumOfGuestRequest));
    }

    @DisplayName("주문테이블을 삭제할 수 있다")
    @Test
    void 테이블그룹_삭제(){
        //given
        given(orderTableRepository.findAllByTableGroupId(anyLong()))
                .willReturn(Arrays.asList(테이블_GROUPED, 테이블_GROUPED2));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(false);

        //when
        tableService.ungroupTableByTableGroupId(테이블_그룹.getId());

        //then
        assertAll(
                () -> assertThat(테이블_GROUPED.getTableGroupId()).isNull(),
                () -> assertThat(테이블_GROUPED2.getTableGroupId()).isNull()
        );
    }

    @DisplayName("주문테이블에 COOKING이나 MEAL 상태의 주문이 있으면 삭제할 수 없다")
    @Test
    void 테이블그룹_삭제_주문상태_검증(){
        //given
        given(orderTableRepository.findAllByTableGroupId(anyLong()))
                .willReturn(Arrays.asList(테이블_GROUPED, 테이블_GROUPED2));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        //then
        assertThrows(IllegalOrderException.class, () ->  tableService.ungroupTableByTableGroupId(테이블_그룹.getId()));
    }
}
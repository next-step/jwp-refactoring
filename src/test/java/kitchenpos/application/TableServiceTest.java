package kitchenpos.application;

import static kitchenpos.helper.TableFixtures.테이블_요청_만들기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("테이블 관련 Service 단위 테스트 - Stub")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private OrderTableRepository orderTableRepository;
    private TableService tableService;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderDao, orderTableDao, orderTableRepository);
    }

    @DisplayName("테이블을 생성한다.")
    @Test
    void create() {
        //given
        long generateTableId = 1;
        OrderTableRequest request = 테이블_요청_만들기(0, true);
        doAnswer(invocation -> new kitchenpos.table.domain.OrderTable(generateTableId,
                request.getNumberOfGuests(),
                request.getEmpty())
        ).when(orderTableRepository).save(any());

        //when
        OrderTableResponse result = tableService.create(request);

        //then
        assertThat(result.getId()).isEqualTo(generateTableId);
        assertThat(result.getTableGroupId()).isEqualTo(request.getTableGroupId());
        assertThat(result.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
        assertThat(result.getEmpty()).isEqualTo(request.getEmpty());
    }

    @DisplayName("빈 테이블 여부를 업데이트 한다.")
    @Test
    void changeEmpty() {
        //given
        long requestTableId = 1L;
        OrderTableRequest request = 테이블_요청_만들기(3, true);

        given(orderTableRepository.findById(requestTableId))
                .willReturn(Optional.of(new kitchenpos.table.domain.OrderTable(1L, 0, true)));

        //when
        OrderTableResponse result = tableService.changeEmpty(requestTableId, request);

        //then
        assertThat(result.getEmpty()).isEqualTo(request.getEmpty());
        assertThat(result.getNumberOfGuests()).isEqualTo(result.getNumberOfGuests());
    }

    @DisplayName("단체 지정이 되어있는 경우 빈 테이블 여부 업데이트 할 수 없다.")
    @Test
    void changeEmpty_table_group() {
        //given
        long requestTableId = 1;
        OrderTableRequest request = 테이블_요청_만들기(3, true);
        kitchenpos.table.domain.OrderTable orderTable = new kitchenpos.table.domain.OrderTable(requestTableId,
                request.getNumberOfGuests(), request.getEmpty());
        orderTable.setTableGroup(new TableGroup(1L, null, null));
        given(orderTableRepository.findById(requestTableId)).willReturn(Optional.of(orderTable));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(requestTableId, request));
    }

    @DisplayName("주문 상태가 조리, 식사 단계인 경우 빈 테이블 여부 업데이트 할 수 없다.")
    @Test
    void changeEmpty_order_stats_cooking_or_meal() {
        //given
        long requestTableId = 1;
        OrderTableRequest request = 테이블_요청_만들기(3, true);
        kitchenpos.table.domain.OrderTable orderTable = mock(kitchenpos.table.domain.OrderTable.class);
        given(orderTableRepository.findById(requestTableId)).willReturn(Optional.of(orderTable));
        doThrow(new IllegalStateException()).when(orderTable).checkPossibleChangeEmpty();

        //when then
        assertThatIllegalStateException()
                .isThrownBy(() -> tableService.changeEmpty(requestTableId, request));

    }

    @DisplayName("방문 손님 수를 업데이트 한다.")
    @Test
    void changeNumberOfGuests() {
        //given
        long requestTableId = 1;
        OrderTableRequest request = 테이블_요청_만들기(3);

        kitchenpos.table.domain.OrderTable orderTable = spy(kitchenpos.table.domain.OrderTable.class);
        given(orderTableRepository.findById(requestTableId)).willReturn(Optional.of(orderTable));

        //when
        OrderTableResponse result = tableService.changeNumberOfGuests(requestTableId, request);

        //then
        assertThat(result.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
    }

    @DisplayName("방문 손님 수가 0명 미만인 경우 업데이트 할 수 없다.")
    @Test
    void changeNumberOfGuests_less_than_zero() {
        //given
        long requestTableId = 1;
        OrderTableRequest request = 테이블_요청_만들기(-1);

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(requestTableId, request));
    }

    @DisplayName("빈 테이블인 경우 방문 손님 수를 업데이트 할 수 없다.")
    @Test
    void changeNumberOfGuests_empty_table() {
        //given
        long requestTableId = 1;
        OrderTableRequest request = 테이블_요청_만들기(3);
        kitchenpos.table.domain.OrderTable orderTable = new kitchenpos.table.domain.OrderTable(requestTableId,
                request.getNumberOfGuests(), true);

        given(orderTableRepository.findById(requestTableId)).willReturn(Optional.of(orderTable));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(requestTableId, request));
    }

}

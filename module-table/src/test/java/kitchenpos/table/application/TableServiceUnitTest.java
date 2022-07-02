package kitchenpos.table.application;

import static kitchenpos.helper.TableFixtures.빈_테이블_만들기;
import static kitchenpos.helper.TableFixtures.테이블_만들기;
import static kitchenpos.helper.TableFixtures.테이블_요청_만들기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;

import java.util.Optional;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
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
class TableServiceUnitTest {

    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private OrderRepository orderRepository;
    private TableService tableService;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderTableRepository, orderRepository);
    }

    @DisplayName("테이블을 생성한다.")
    @Test
    void create() {
        //given
        long generateTableId = 1;
        OrderTableRequest request = 테이블_요청_만들기(0, true);
        doAnswer(invocation -> 테이블_만들기(generateTableId, request.getNumberOfGuests(), request.getEmpty()))
                .when(orderTableRepository).save(any());

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

        given(orderTableRepository.findById(requestTableId)).willReturn(Optional.of(테이블_만들기(1L, 0, true)));
        given(orderTableRepository.findTableGroupId(requestTableId)).willReturn(null);

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
        OrderTable orderTable = 테이블_만들기(requestTableId, request.getNumberOfGuests(), request.getEmpty());
        given(orderTableRepository.findById(requestTableId)).willReturn(Optional.of(테이블_만들기(1L, 0, true)));
        given(orderTableRepository.findTableGroupId(requestTableId)).willReturn(1L);

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(requestTableId, request));
    }


    @DisplayName("방문 손님 수를 업데이트 한다.")
    @Test
    void changeNumberOfGuests() {
        //given
        long requestTableId = 1;
        OrderTableRequest request = 테이블_요청_만들기(3);

        OrderTable orderTable = 테이블_만들기(0, false);
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
        OrderTable orderTable = 빈_테이블_만들기();

        given(orderTableRepository.findById(requestTableId)).willReturn(Optional.of(orderTable));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(requestTableId, request));
    }

}

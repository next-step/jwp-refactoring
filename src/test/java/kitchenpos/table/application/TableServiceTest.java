package kitchenpos.table.application;

import kitchenpos.exception.CannotFindException;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.common.Message.ERROR_ORDER_TABLE_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    private final Long 단체지정_안됨 = null;
    private final OrderTable 비어있지_않은_테이블 = new OrderTable(1L, 단체지정_안됨, 3, false);
    private final OrderTable 비어있는_테이블 = new OrderTable(1L, 단체지정_안됨, 3, true);

    @DisplayName("테이블을 등록한다")
    @Test
    void 테이블_등록() {
        //Given
        when(orderTableRepository.save(any())).thenReturn(비어있지_않은_테이블);

        //When
        tableService.create(OrderTableRequest.of(비어있지_않은_테이블));

        //Then
        verify(orderTableRepository, times(1)).save(any());
    }

    @DisplayName("테이블의 목록을 조회한다")
    @Test
    void 테이블_목록_조회() {
        //Given
        when(orderTableRepository.findAll()).thenReturn(Arrays.asList(비어있지_않은_테이블, 비어있는_테이블));

        //When
        List<OrderTableResponse> 조회된_테이블_목록 = tableService.list();

        //Then
        verify(orderTableRepository, times(1)).findAll();
        assertThat(조회된_테이블_목록).hasSize(2);
    }

    @DisplayName("등록되지 않은 테이블의 비어있음 여부를 변경하는 경우, 예외발생한다")
    @Test
    void 등록안된_테이블의_비어있음_여부_변경시_예외발생() {
        //Given
        OrderTableRequest 비어있음으로_변경_요청 = OrderTableRequest.of(비어있는_테이블);
        when(orderTableRepository.findById(비어있지_않은_테이블.getId())).thenReturn(Optional.empty());

        //When + Then
        Throwable 테이블_등록안됨_예외 = catchThrowable(() -> tableService.changeEmpty(비어있지_않은_테이블.getId(), 비어있음으로_변경_요청));
        assertThat(테이블_등록안됨_예외).isInstanceOf(CannotFindException.class)
                .hasMessage(ERROR_ORDER_TABLE_NOT_FOUND.showText());
    }

    @DisplayName("테이블의 손님수를 변경한다")
    @Test
    void 테이블의_손님수_변경() {
        //Given
        OrderTable 변경될_손님수_정보_가지고있는_테이블 = new OrderTable(99, false);
        OrderTableRequest 손님수_변경_요청 = OrderTableRequest.of(변경될_손님수_정보_가지고있는_테이블);
        when(orderTableRepository.findById(비어있지_않은_테이블.getId())).thenReturn(Optional.of(비어있지_않은_테이블));

        //When
        OrderTableResponse 변경된_테이블 = tableService.changeNumberOfGuests(비어있지_않은_테이블.getId(), 손님수_변경_요청);

        //Then
        assertThat(변경된_테이블.getNumberOfGuests()).isEqualTo(변경될_손님수_정보_가지고있는_테이블.getNumberOfGuests());
    }

    @DisplayName("등록되지 않은 테이블의 손님수를 변경시, 예외 발생한다")
    @Test
    void 등록되지_않은_테이블의_손님수_변경시_예외발생() {
        //Given
        OrderTable 변경될_손님수_정보_가지고있는_테이블 = new OrderTable(99, false);
        OrderTableRequest 손님수_변경_요청 = OrderTableRequest.of(변경될_손님수_정보_가지고있는_테이블);
        when(orderTableRepository.findById(비어있지_않은_테이블.getId())).thenReturn(Optional.empty());

        //When + Then
        Throwable 테이블_등록안됨_예외 = catchThrowable(() -> tableService.changeNumberOfGuests(비어있지_않은_테이블.getId(), 손님수_변경_요청));
        assertThat(테이블_등록안됨_예외).isInstanceOf(CannotFindException.class)
                .hasMessage(ERROR_ORDER_TABLE_NOT_FOUND.showText());
    }

}

package kitchenpos.order.application;

import kitchenpos.common.exception.CannotFindException;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import kitchenpos.ordertable.OrderTable;
import kitchenpos.ordertable.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.common.exception.Message.ERROR_ORDER_TABLE_NOT_FOUND;
import static kitchenpos.order.OrderTableTestFixture.*;
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


    @DisplayName("테이블을 등록한다")
    @Test
    void 테이블_등록() {
        //Given
        when(orderTableRepository.save(any())).thenReturn(비어있지_않은_테이블);

        //When
        tableService.create(비어있지_않은_테이블_요청);

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
        when(orderTableRepository.findById(비어있지_않은_테이블.getId())).thenReturn(Optional.empty());

        //When + Then
        Throwable 테이블_등록안됨_예외 = catchThrowable(() -> tableService.changeEmpty(비어있지_않은_테이블.getId(), 비어있는_테이블_요청));
        assertThat(테이블_등록안됨_예외).isInstanceOf(CannotFindException.class)
                .hasMessage(ERROR_ORDER_TABLE_NOT_FOUND.showText());
    }

    @DisplayName("테이블의 손님수를 변경한다")
    @Test
    void 테이블의_손님수_변경() {
        //Given
        OrderTable 변경테이블 = new OrderTable(99, false);
        OrderTableRequest 손님수_변경_요청 = new OrderTableRequest(변경테이블.getNumberOfGuests(), 변경테이블.isEmpty());
        when(orderTableRepository.findById(비어있지_않은_테이블.getId())).thenReturn(Optional.of(비어있지_않은_테이블));

        //When
        OrderTableResponse 변경된_테이블 = tableService.changeNumberOfGuests(비어있지_않은_테이블.getId(), 손님수_변경_요청);

        //Then
        assertThat(변경된_테이블.getNumberOfGuests()).isEqualTo(변경테이블.getNumberOfGuests());
    }

    @DisplayName("등록되지 않은 테이블의 손님수를 변경시, 예외 발생한다")
    @Test
    void 등록되지_않은_테이블의_손님수_변경시_예외발생() {
        //Given
        OrderTable 변경테이블 = new OrderTable(99, false);
        OrderTableRequest 손님수_변경_요청 = new OrderTableRequest(변경테이블.getNumberOfGuests(), 변경테이블.isEmpty());
        when(orderTableRepository.findById(비어있지_않은_테이블.getId())).thenReturn(Optional.empty());

        //When + Then
        Throwable 테이블_등록안됨_예외 = catchThrowable(() -> tableService.changeNumberOfGuests(비어있지_않은_테이블.getId(), 손님수_변경_요청));
        assertThat(테이블_등록안됨_예외).isInstanceOf(CannotFindException.class)
                .hasMessage(ERROR_ORDER_TABLE_NOT_FOUND.showText());
    }

}

package kitchenpos.table.application;

import kitchenpos.ordertable.application.TableService;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    private OrderTable 첫번째_테이블 = new OrderTable(null, 3, true);
    private OrderTable 두번째_테이블 = new OrderTable(null, 5, false);

    @DisplayName("테이블을 등록한다")
    @Test
    void 테이블_등록() {
        //Given
        Mockito.when(orderTableRepository.save(첫번째_테이블)).thenReturn(첫번째_테이블);

        //When
        OrderTable 생성된_테이블 = tableService.create(첫번째_테이블);

        //Then
        Assertions.assertThat(생성된_테이블).isNotNull();
        Assertions.assertThat(생성된_테이블.getTableGroup()).isNull();
    }

    @DisplayName("테이블의 목록을 조회한다")
    @Test
    void 테이블_목록_조회() {
        //Given
        Mockito.when(orderTableRepository.findAll()).thenReturn(Arrays.asList(첫번째_테이블, 두번째_테이블));

        //When
        List<OrderTable> 조회된_테이블_목록 = tableService.list();

        //Then
        Assertions.assertThat(조회된_테이블_목록).hasSize(2)
                .containsExactly(첫번째_테이블, 두번째_테이블);
    }

    @DisplayName("테이블의 비어있음 여부를 변경한다")
    @Test
    void 테이블의_비어있음_여부_변경() {
        //Given
        Mockito.when(orderTableRepository.findById(첫번째_테이블.getId())).thenReturn(Optional.of(첫번째_테이블));
        Mockito.when(orderRepository.existsByOrderTableIdAndOrderStatusIn(첫번째_테이블.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .thenReturn(false);
        Mockito.when(orderTableRepository.save(첫번째_테이블)).thenReturn(첫번째_테이블);

        //When
        첫번째_테이블.setEmpty(false);
        OrderTable 변경된_테이블 = tableService.changeEmpty(첫번째_테이블.getId(), 첫번째_테이블);

        //Then
        Assertions.assertThat(변경된_테이블.isEmpty()).isFalse();
    }

    @DisplayName("등록되지 않은 테이블의 비어있음 여부를 변경하는 경우, 예외발생한다")
    @Test
    void 등록안된_테이블의_비어있음_여부_변경시_예외발생() {
        //Given
        Mockito.when(orderTableRepository.findById(첫번째_테이블.getId())).thenReturn(Optional.empty());

        //When + Then
        Assertions.assertThatThrownBy(() -> tableService.changeEmpty(첫번째_테이블.getId(), 첫번째_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상태가 요리중/식사중인 테이블의 비어있음 여부를 변경하는 경우, 예외발생한다")
    @Test
    void 상태가_요리중_혹은_식사중인_테이블의_비어있음_여부_변경시_예외발생() {
        //Given
        Mockito.when(orderTableRepository.findById(첫번째_테이블.getId())).thenReturn(Optional.of(첫번째_테이블));
        Mockito.when(orderRepository.existsByOrderTableIdAndOrderStatusIn(첫번째_테이블.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .thenReturn(true);

        //When + Then
        Assertions.assertThatThrownBy(() -> tableService.changeEmpty(첫번째_테이블.getId(), 첫번째_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님수를 변경한다")
    @Test
    void 테이블의_손님수_변경() {
        //Given
        Mockito.when(orderTableRepository.findById(첫번째_테이블.getId())).thenReturn(Optional.of(첫번째_테이블));
        Mockito.when(orderTableRepository.save(첫번째_테이블)).thenReturn(첫번째_테이블);

        //When
        첫번째_테이블.setNumberOfGuests(3);
        OrderTable 변경된_테이블 = tableService.changeNumberOfGuests(첫번째_테이블.getId(), 첫번째_테이블);

        //Then
        Assertions.assertThat(변경된_테이블.getNumberOfGuests()).isEqualTo(첫번째_테이블.getNumberOfGuests());
    }

    @DisplayName("등록되지 않은 테이블의 손님수를 변경시, 예외 발생한다")
    @Test
    void 등록되지_않은_테이블의_손님수_변경시_예외발생() {
        //Given
        첫번째_테이블.setNumberOfGuests(3);
        Mockito.when(orderTableRepository.findById(첫번째_테이블.getId())).thenReturn(Optional.empty());

        //When + Then
        Assertions.assertThatThrownBy(() -> tableService.changeNumberOfGuests(첫번째_테이블.getId(), 첫번째_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

}

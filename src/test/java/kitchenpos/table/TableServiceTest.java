package kitchenpos.table;

import kitchenpos.application.TableService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    private OrderTable 첫번째_테이블;
    private OrderTable 두번째_테이블;

    @BeforeEach
    void setUp() {
        첫번째_테이블 = new OrderTable();
        첫번째_테이블.setId(1L);
        두번째_테이블 = new OrderTable();
        두번째_테이블.setId(2L);
    }

    @DisplayName("테이블을 등록한다")
    @Test
    void 테이블_등록() {
        //Given
        Mockito.when(orderTableDao.save(첫번째_테이블)).thenReturn(첫번째_테이블);

        //When
        OrderTable 생성된_테이블 = tableService.create(첫번째_테이블);

        //Then
        Assertions.assertThat(생성된_테이블).isNotNull();
        Assertions.assertThat(생성된_테이블.getTableGroupId()).isNull();
    }

    @DisplayName("테이블의 목록을 조회한다")
    @Test
    void 테이블_목록_조회() {
        //Given
        Mockito.when(orderTableDao.findAll()).thenReturn(Arrays.asList(첫번째_테이블, 두번째_테이블));

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
        Mockito.when(orderTableDao.findById(첫번째_테이블.getId())).thenReturn(Optional.of(첫번째_테이블));
        Mockito.when(orderDao.existsByOrderTableIdAndOrderStatusIn(첫번째_테이블.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .thenReturn(false);
        Mockito.when(orderTableDao.save(첫번째_테이블)).thenReturn(첫번째_테이블);

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
        Mockito.when(orderTableDao.findById(첫번째_테이블.getId())).thenReturn(Optional.empty());

        //When + Then
        Assertions.assertThatThrownBy(() -> tableService.changeEmpty(첫번째_테이블.getId(), 첫번째_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체지정된 테이블의 비어있음 여부를 변경하는 경우, 예외발생한다")
    @Test
    void 단체지정된_테이블의_비어있음_여부_변경시_예외발생() {
        //Given
        첫번째_테이블.setTableGroupId(1L);
        Mockito.when(orderTableDao.findById(첫번째_테이블.getId())).thenReturn(Optional.of(첫번째_테이블));

        //When + Then
        Assertions.assertThatThrownBy(() -> tableService.changeEmpty(첫번째_테이블.getId(), 첫번째_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상태가 요리중/식사중인 테이블의 비어있음 여부를 변경하는 경우, 예외발생한다")
    @Test
    void 상태가_요리중_혹은_식사중인_테이블의_비어있음_여부_변경시_예외발생() {
        //Given
        Mockito.when(orderTableDao.findById(첫번째_테이블.getId())).thenReturn(Optional.of(첫번째_테이블));
        Mockito.when(orderDao.existsByOrderTableIdAndOrderStatusIn(첫번째_테이블.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .thenReturn(true);

        //When + Then
        Assertions.assertThatThrownBy(() -> tableService.changeEmpty(첫번째_테이블.getId(), 첫번째_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님수를 변경한다")
    @Test
    void 테이블의_손님수_변경() {
        //Given
        Mockito.when(orderTableDao.findById(첫번째_테이블.getId())).thenReturn(Optional.of(첫번째_테이블));
        Mockito.when(orderTableDao.save(첫번째_테이블)).thenReturn(첫번째_테이블);

        //When
        첫번째_테이블.setNumberOfGuests(3);
        OrderTable 변경된_테이블 = tableService.changeNumberOfGuests(첫번째_테이블.getId(), 첫번째_테이블);

        //Then
        Assertions.assertThat(변경된_테이블.getNumberOfGuests()).isEqualTo(첫번째_테이블.getNumberOfGuests());
    }

    @DisplayName("테이블의 손님수를 0명 미만으로 입력하여 변경시, 예외 발생한다")
    @Test
    void 테이블의_손님수를_0명미만으로_입력하여_변경시_예외발생() {
        //Given
        첫번째_테이블.setNumberOfGuests(-1);

        //When + Then
        Assertions.assertThatThrownBy(() -> tableService.changeNumberOfGuests(첫번째_테이블.getId(), 첫번째_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되지 않은 테이블의 손님수를 변경시, 예외 발생한다")
    @Test
    void 등록되지_않은_테이블의_손님수_변경시_예외발생() {
        //Given
        첫번째_테이블.setNumberOfGuests(3);
        Mockito.when(orderTableDao.findById(첫번째_테이블.getId())).thenReturn(Optional.empty());

        //When + Then
        Assertions.assertThatThrownBy(() -> tableService.changeNumberOfGuests(첫번째_테이블.getId(), 첫번째_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("비어있는 테이블의 손님수를 변경시, 예외 발생한다")
    @Test
    void 비어있는_테이블의_손님수_변경시_예외발생() {
        //Given
        첫번째_테이블.setNumberOfGuests(3);
        Mockito.when(orderTableDao.findById(첫번째_테이블.getId())).thenReturn(Optional.of(첫번째_테이블));

        //When + Then
        첫번째_테이블.setEmpty(true);
        Assertions.assertThatThrownBy(() -> tableService.changeNumberOfGuests(첫번째_테이블.getId(), 첫번째_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

}

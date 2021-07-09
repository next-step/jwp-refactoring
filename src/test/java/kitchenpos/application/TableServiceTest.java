package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import static org.mockito.Mockito.when;

@DisplayName("테이블 기능 관련 테스트")
@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    private OrderTable firstOrderTable;
    private OrderTable secondOrderTable;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @BeforeEach
    public void setUp() {
        // given
        firstOrderTable = 주문_테이블_생성(1L);
    }

    @Test
    @DisplayName("테이블 등록 테스트")
    void 테이블_등록() {
        // when
        // 주문 테이블 등록 요청
        when(orderTableDao.save(firstOrderTable)).thenReturn(firstOrderTable);
        OrderTable expected = tableService.create(firstOrderTable);

        // then
        // 주문 테이블 저장
        assertThat(expected.getId()).isEqualTo(firstOrderTable.getId());
    }

    @Test
    @DisplayName("주문 테이블 리스트 조회")
    void 테이블_리스트_조회() {
        // when
        // 주문 테이블 리스트 조회
        when(orderTableDao.findAll()).thenReturn(Arrays.asList(firstOrderTable, secondOrderTable));
        List<OrderTable> expected = tableService.list();

        // then
        // 리스트 조회됨
        assertThat(expected.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("저장되어 있지 않은 테이블 빈 테이블로 변경 시 예외 처리")
    void isBlackOrderTable_exception() {
        // when
        // 주문 테이블 리스트 조회
        when(orderTableDao.findById(1L)).thenReturn(Optional.empty());

        // then
        // 예외 발생
        assertThatThrownBy(() -> tableService.changeEmpty(1L, firstOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 테이블에 속한 테이블 변경 불가")
    void isOrderTalbleOfGroupTable_exception() {
        // when
        // 주문 테이블 리스트 조회
        firstOrderTable.setTableGroupId(1L);
        when(orderTableDao.findById(1L)).thenReturn(Optional.ofNullable(firstOrderTable));

        // then
        // 예외 발생
        assertThatThrownBy(() -> tableService.changeEmpty(1L, firstOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("요리중이거나 식사중인 테이블 변경 불가")
    void isCookingOrMealingOrderTable_exception() {
        // given
        when(orderTableDao.findById(1L)).thenReturn(Optional.ofNullable(firstOrderTable));

        // and
        // 주문 테이블 아직 식사중임
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(1L, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .thenReturn(true);

        // then
        // 예외 발생
        assertThatThrownBy(() -> tableService.changeEmpty(1L, firstOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블 정상 변경")
    void 주문_테이블_정상_변경() {
        // given
        firstOrderTable.setEmpty(false);
        when(orderTableDao.findById(1L)).thenReturn(Optional.ofNullable(firstOrderTable));

        // and
        // 주문 테이블 요리 또는 식사 종료
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(1L, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .thenReturn(false);

        // when
        // 빈 테이블로 변경 요청
        firstOrderTable.setEmpty(true);
        when(orderTableDao.save(firstOrderTable)).thenReturn(firstOrderTable);

        //then 변경됨
        OrderTable expected = tableService.changeEmpty(1L, firstOrderTable);
        assertThat(expected.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("테이블 손님 명수 0 이하 일때 오류")
    void isZeroGuestNumber_exception() {
        // given
        // 손님 명수 오류 등록
        firstOrderTable.setNumberOfGuests(-1);

        // then
        // 예외 발생
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, firstOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 테이블 예외처리")
    void isBlankOrderTable_exception() {
        // given
        firstOrderTable.setNumberOfGuests(3);

        // when
        // 등록되지 않은 테이블
        when(orderTableDao.findById(1L)).thenReturn(Optional.empty());

        // then
        // 예외 발생
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, firstOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 테이블 예외처리")
    void isEmptyOrderTable_exception() {
        // given
        firstOrderTable.setNumberOfGuests(3);

        // when
        // 빈 테이블임
        firstOrderTable.setEmpty(true);
        when(orderTableDao.findById(1L)).thenReturn(Optional.ofNullable(firstOrderTable));

        // then
        // 예외 발생
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, firstOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블 인원수 정상 변경")
    void 주문_테이블_인원수_정상_변경() {
        // given
        secondOrderTable = 주문_테이블_생성(1L);
        firstOrderTable.setNumberOfGuests(3);

        // when
        // 빈 테이블임
        secondOrderTable.setEmpty(false);
        secondOrderTable.setNumberOfGuests(5);
        when(orderTableDao.findById(1L)).thenReturn(Optional.ofNullable(secondOrderTable));

        // then
        // 정상 변경
        when(orderTableDao.save(secondOrderTable)).thenReturn(secondOrderTable);
        OrderTable expected = tableService.changeNumberOfGuests(1L, firstOrderTable);
        assertThat(expected.getNumberOfGuests()).isEqualTo(3);
    }

    private OrderTable 주문_테이블_생성(Long id) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        return orderTable;
    }
}

package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService service;

    private OrderTable 테이블;

    @BeforeEach
    void setUp() {
        테이블 = new OrderTable(1L, null, 3, false);
    }

    @DisplayName("주문 테이블 등록")
    @Test
    void create() {
        //given
        when(orderTableDao.save(테이블)).thenReturn(테이블);

        //when
        OrderTable result = service.create(테이블);

        //then
        assertAll(
                () -> assertThat(result.getTableGroupId()).isEqualTo(테이블.getTableGroupId()),
                () -> assertThat(result.getNumberOfGuests()).isEqualTo(테이블.getNumberOfGuests()),
                () -> assertThat(result.isEmpty()).isEqualTo(테이블.isEmpty())
        );
    }

    @DisplayName("전체 주문 테이블의 조회")
    @Test
    void list() {
        //given
        OrderTable 다른테이블 = new OrderTable(2L, 2L, 4, false);
        when(orderTableDao.findAll()).thenReturn(Arrays.asList(테이블, 다른테이블));

        //when
        List<OrderTable> orderTables = service.list();

        //then
        assertThat(orderTables).contains(다른테이블, 테이블);
    }

    @DisplayName("주문 테이블을 빈 테이블로 변경")
    @Test
    void changeEmpty() {
        //given
        OrderTable 빈테이블 = new OrderTable(null, null, 0, true);
        when(orderTableDao.findById(테이블.getId())).thenReturn(Optional.of(테이블));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(
                테이블.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .thenReturn(false);
        when(orderTableDao.save(테이블)).thenReturn(테이블);

        //when
        OrderTable response = service.changeEmpty(테이블.getId(), 빈테이블);

        //then
        assertAll(
                () -> assertThat(response.getId()).isEqualTo(테이블.getId()),
                () -> assertThat(response.isEmpty()).isTrue()
        );
    }

    @DisplayName("존재하지 않는 주문 테이블을 빈 테이블로 변경")
    @Test
    void changeEmptyWithNotExistsTable() {
        //given
        OrderTable 빈테이블 = new OrderTable(null, null, 0, true);
        when(orderTableDao.findById(테이블.getId())).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> service.changeEmpty(테이블.getId(), 빈테이블)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정에 속해있는 주문 테이블을 빈 테이블로 변경")
    @Test
    void changeEmptyWithTableOfGroup() {
        //given
        OrderTable 타그룹테이블 = new OrderTable(2L, 3, false);
        OrderTable 빈테이블 = new OrderTable(null, null, 0, true);
        when(orderTableDao.findById(타그룹테이블.getId())).thenReturn(Optional.of(타그룹테이블));

        //when & then
        assertThatThrownBy(() -> service.changeEmpty(타그룹테이블.getId(), 빈테이블)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("식사 중인 주문 테이블을 빈 테이블로 변경")
    @Test
    void changeEmptyWithEatingTable() {
        //given
        OrderTable 빈테이블 = new OrderTable(null, 0, true);
        when(orderTableDao.findById(테이블.getId())).thenReturn(Optional.of(테이블));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(
                테이블.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .thenReturn(true);

        //when & then
        assertThatThrownBy(() -> service.changeEmpty(테이블.getId(), 빈테이블)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 손님 숫자 변경")
    @Test
    void changeNumberOfGuests() {
        //given
        OrderTable 손님두명테이블 = new OrderTable(null, 2, false);
        when(orderTableDao.findById(테이블.getId())).thenReturn(Optional.of(테이블));
        when(orderTableDao.save(테이블)).thenReturn(테이블);

        //when
        OrderTable result = service.changeNumberOfGuests(테이블.getId(), 손님두명테이블);

        //then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(테이블.getId()),
                () -> assertThat(result.getNumberOfGuests()).isEqualTo(2)
        );
    }

    @DisplayName("존재하지 않는 테이블의 손님 숫자 변경")
    @Test
    void changeNumberOfGuestsWithNotExistsTable() {
        //given
        OrderTable 손님숫자테이블 = new OrderTable(null, 2, false);
        when(orderTableDao.findById(테이블.getId())).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> service.changeNumberOfGuests(테이블.getId(), 손님숫자테이블)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @DisplayName("테이블의 손님 숫자를 음수로 변경")
    @Test
    void changeNumberOfGuestsWithNegativeNumber() {
        //given
        OrderTable 손님숫자테이블 = new OrderTable(null, -3, false);

        //when & then
        assertThatThrownBy(() -> service.changeNumberOfGuests(테이블.getId(), 손님숫자테이블)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블의 손님 숫자 변경")
    @Test
    void changeNumberOfGuestsWithEmptyTable() {
        //given
        OrderTable 빈테이블 = new OrderTable(2L, 3, true);
        OrderTable 손님숫자테이블 = new OrderTable(null, 2, false);
        when(orderTableDao.findById(빈테이블.getId())).thenReturn(Optional.of(빈테이블));

        //when & then
        assertThatThrownBy(() -> service.changeNumberOfGuests(빈테이블.getId(), 손님숫자테이블)).isInstanceOf(
                IllegalArgumentException.class);
    }
}

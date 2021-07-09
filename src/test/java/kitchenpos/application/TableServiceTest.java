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
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    private OrderTable table;
    private OrderTable table2;
    private OrderTable table3;
    private OrderTable table4;

    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private OrderDao orderDao;

    @InjectMocks
    private TableService tableService;

    @BeforeEach
    void setUp() {
        table = new OrderTable();
        table.setId(1L);
        table.setNumberOfGuests(0);
        table.setEmpty(true);

        table2 = new OrderTable();
        table2.setId(2L);
        table2.setNumberOfGuests(0);
        table2.setEmpty(true);

        table3 = new OrderTable();
        table3.setId(3L);
        table3.setNumberOfGuests(0);
        table3.setEmpty(false);

        table4 = new OrderTable();
        table4.setId(4L);
        table4.setNumberOfGuests(2);
        table4.setEmpty(false);
    }

    @DisplayName("테이블 정보를 등록한다")
    @Test
    void create() {
        // given
        given(orderTableDao.save(table)).willReturn(table);

        // when
        OrderTable actual = tableService.create(table);

        // then
        assertThat(actual).isEqualTo(table);
    }

    @DisplayName("등록한 테이블 목록을 조회한다")
    @Test
    void findAll() {
        // given
        given(orderTableDao.findAll()).willReturn(Arrays.asList(table, table2));

        // when
        List<OrderTable> actual = tableService.list();

        // then
        assertThat(actual).isEqualTo(Arrays.asList(table, table2));
    }

    @DisplayName("테이블을 빈 상태로 변경한다")
    @Test
    void changeEmpty() {
        // given
        given(orderTableDao.findById(table3.getId())).willReturn(Optional.of(table3));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(table3.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
        given(orderTableDao.save(table3)).willReturn(table3);

        // when
        table3.setEmpty(true);
        OrderTable actual = tableService.changeEmpty(table3.getId(), table3);

        // then
        assertThat(actual.isEmpty()).isTrue();
    }

    @DisplayName("테이블 빈 상태로 변경 - 테이블에 연결된 조리, 식사 상태의 주문이 존재하는 경우 변경 불가")
    @Test
    void changeEmpty_existsCookingOrMealOrder() {
        // given
        given(orderTableDao.findById(table3.getId())).willReturn(Optional.of(table3));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(table3.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        // when
        table3.setEmpty(true);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(table3.getId(), table3));
    }

    @DisplayName("테이블 빈 상태로 변경 - 테이블에 연결된 테이블 그룹이 존재하는 경우 변경 불가")
    @Test
    void changeEmpty_existsTableGroup() {
        // given
        given(orderTableDao.findById(table3.getId())).willReturn(Optional.of(table3));

        // when
        table3.setTableGroupId(1L);
        table3.setEmpty(true);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(table3.getId(), table3));
    }

    @DisplayName("테이블에 착석한 손님의 수를 변경한다")
    @Test
    void changeNumberOfGuests() {
        // given
        given(orderTableDao.findById(table4.getId())).willReturn(Optional.of(table4));
        given(orderTableDao.save(table4)).willReturn(table4);

        // when
        table4.setNumberOfGuests(3);
        OrderTable actual = tableService.changeNumberOfGuests(table4.getId(), table4);

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(3);
    }

    @DisplayName("테이블 손님 수 변경 - 손님 수는 0 이상의 정수여야 한다")
    @Test
    void changeNumberOfGuests_invalidNumber() {
        // given when
        table4.setNumberOfGuests(-1);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(table4.getId(), table4));
    }

    @DisplayName("테이블 손님 수 변경 - 테이블이 비어있는 경우 변경 불가")
    @Test
    void changeNumberOfGuests_emptyTable() {
        // given
        given(orderTableDao.findById(table.getId())).willReturn(Optional.of(table));

        // when
        table.setNumberOfGuests(5);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(table.getId(), table));
    }
}
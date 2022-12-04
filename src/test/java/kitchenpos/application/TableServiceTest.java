package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@DisplayName("TableService 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    private static final Long TABLE_GROUP_ID = 1L;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;

    @BeforeEach
    void setUp() {
        주문테이블1 = new OrderTable(1L, null, 2, false);
        주문테이블2 = new OrderTable(2L, null, 3, false);
    }

    @Test
    void 주문_테이블_생성() {
        given(orderTableDao.save(주문테이블1)).willReturn(주문테이블1);

        OrderTable savedOrderTable = tableService.create(주문테이블1);

        assertThat(savedOrderTable).satisfies(orderTable -> {
           assertEquals(주문테이블1.getId(), orderTable.getId());
           assertEquals(주문테이블1.getNumberOfGuests(), orderTable.getNumberOfGuests());
        });
    }

    @Test
    void 주문_테이블_목록_조회() {
        given(orderTableDao.findAll()).willReturn(Arrays.asList(주문테이블1, 주문테이블2));

        List<OrderTable> orderTables = tableService.list();

        assertAll(
                () -> assertThat(orderTables).hasSize(2),
                () -> assertThat(orderTables).containsExactly(주문테이블1, 주문테이블2)
        );
    }

    @Test
    void 등록되지_않은_주문_테이블이면_빈_테이블로_변경할_수_없음() {
        OrderTable 빈_테이블 = new OrderTable();
        빈_테이블.setEmpty(true);
        given(orderTableDao.findById(주문테이블1.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> {
            tableService.changeEmpty(주문테이블1.getId(), 빈_테이블);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹에_속해있으면_빈_테이블로_변경할_수_없음() {
        주문테이블1.setTableGroupId(TABLE_GROUP_ID);
        OrderTable 빈_테이블 = new OrderTable();
        빈_테이블.setEmpty(true);

        given(orderTableDao.findById(주문테이블1.getId())).willReturn(Optional.of(주문테이블1));

        assertThatThrownBy(() -> {
            tableService.changeEmpty(주문테이블1.getId(), 빈_테이블);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블_상태가_조리중_또는_식사중이면_빈_테이블로_변경할_수_없음() {
        OrderTable 빈_테이블 = new OrderTable();
        빈_테이블.setEmpty(true);

        given(orderTableDao.findById(주문테이블1.getId())).willReturn(Optional.of(주문테이블1));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(주문테이블1.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        assertThatThrownBy(() -> {
            tableService.changeEmpty(주문테이블1.getId(), 빈_테이블);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블을_빈_테이블로_변경() {
        OrderTable 빈_테이블 = new OrderTable();
        빈_테이블.setEmpty(true);

        given(orderTableDao.findById(주문테이블1.getId())).willReturn(Optional.of(주문테이블1));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(주문테이블1.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
        given(orderTableDao.save(주문테이블1)).willReturn(주문테이블1);

        OrderTable savedOrderTable = tableService.changeEmpty(주문테이블1.getId(), 빈_테이블);

        assertTrue(savedOrderTable.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(ints = { -1, -2, -5, -10 })
    void 주문_테이블의_방문한_손님_수가_0보다_작으면_변경할_수_없음(int numberOfGuests) {
        OrderTable 변경_테이블 = new OrderTable();
        변경_테이블.setNumberOfGuests(numberOfGuests);

        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(주문테이블1.getId(), 변경_테이블);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_등록되어_있지_않으면_방문한_손님_수를_변경할_수_없음() {
        OrderTable 변경_테이블 = new OrderTable();
        변경_테이블.setNumberOfGuests(5);

        given(orderTableDao.findById(주문테이블1.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(주문테이블1.getId(), 변경_테이블);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블이_빈_테이블이면_방문한_손님_수를_변경할_수_없음() {
        주문테이블1.setEmpty(true);
        주문테이블1.setNumberOfGuests(0);
        OrderTable 변경_테이블 = new OrderTable();
        변경_테이블.setNumberOfGuests(5);

        given(orderTableDao.findById(주문테이블1.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(주문테이블1.getId(), 변경_테이블);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블의_방문한_손님_수를_변경() {
        OrderTable 변경_테이블 = new OrderTable();
        변경_테이블.setNumberOfGuests(5);

        given(orderTableDao.findById(주문테이블1.getId())).willReturn(Optional.of(주문테이블1));
        given(orderTableDao.save(주문테이블1)).willReturn(주문테이블1);

        OrderTable savedOrderTable = tableService.changeNumberOfGuests(주문테이블1.getId(), 변경_테이블);

        assertEquals(5, savedOrderTable.getNumberOfGuests());
    }
}

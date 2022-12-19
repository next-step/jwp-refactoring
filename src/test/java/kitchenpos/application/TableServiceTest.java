package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.assertj.core.api.Assertions;
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

import static kitchenpos.domain.OrderTableTestFixture.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@DisplayName("주문 테이블 관련 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;

    public TableServiceTest() {
    }

    @BeforeEach
    void setUp() {
        주문테이블1 = createOrderTable(1L, null, 5, false);
        주문테이블2 = createOrderTable(2L, null, 4, false);
    }

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void 주문_테이블_생성() {
        // given
        when(orderTableDao.save(주문테이블1)).thenReturn(주문테이블1);
        // when
        OrderTable saveOrderTable = tableService.create(주문테이블1);

        // then
        assertAll(
                () -> assertThat(saveOrderTable.getTableGroupId()).isNull(),
                () -> assertThat(saveOrderTable.getNumberOfGuests()).isEqualTo(주문테이블1.getNumberOfGuests())
        );
    }


    @DisplayName("주문 테이블 전체 목록을 조회한다.")
    @Test
    void 주문_테이블_전체_목록_조회() {
        // given
        List<OrderTable> orderTables = Arrays.asList(주문테이블1, 주문테이블2);
        when(orderTableDao.findAll()).thenReturn(orderTables);
        // when
        List<OrderTable> findOrderTables = tableService.list();
        // then
        assertAll(
                () -> assertThat(findOrderTables).hasSize(orderTables.size()),
                () -> assertThat(findOrderTables).containsExactly(주문테이블1, 주문테이블2)
        );
    }


    @DisplayName("주문 테이블의 비어있는지 여부를 변경한다.")
    @Test
    void 주문_테이블_상태_변경() {
        // given
        boolean isEmpty = 주문테이블1.isEmpty();
        OrderTable changeOrderTable = createOrderTable(null, null, 주문테이블1.getNumberOfGuests(), !isEmpty);
        when(orderTableDao.findById(주문테이블1.getId())).thenReturn(Optional.of(주문테이블1));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(주문테이블1.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);
        when(orderTableDao.save(주문테이블1)).thenReturn(주문테이블1);
        // when
        OrderTable resultOrderTable = tableService.changeEmpty(주문테이블1.getId(), changeOrderTable);
        // then
        assertThat(resultOrderTable.isEmpty()).isEqualTo(!isEmpty);
    }

    @DisplayName("주문 테이블에 방문한 손님 수를 변경한다.")
    @ParameterizedTest(name = "변경된 손님 수: {0}")
    @ValueSource(ints = {2, 3, 7})
    void 주문_테이블_방문한자수_변경(int expectNumberOfGuests) {
        // given
        OrderTable changeOrderTable = createOrderTable(null, null, expectNumberOfGuests, 주문테이블1.isEmpty());
        when(orderTableDao.findById(주문테이블1.getId())).thenReturn(Optional.of(주문테이블1));
        when(orderTableDao.save(주문테이블1)).thenReturn(주문테이블1);
        // when
        OrderTable resultOrderTable = tableService.changeNumberOfGuests(주문테이블1.getId(), changeOrderTable);
        // then
        assertThat(resultOrderTable.getNumberOfGuests()).isEqualTo(expectNumberOfGuests);
    }

    @DisplayName("존재하지 않는 주문 테이블의 비어있는지 여부는 변경할 수 없다.")
    @Test
    void 주문_테이블_상태_변경_예외1() {
        // given
        boolean isEmpty = 주문테이블1.isEmpty();
        OrderTable changeOrderTable = createOrderTable(null, null, 주문테이블1.getNumberOfGuests(), !isEmpty);
        when(orderTableDao.findById(10L)).thenReturn(Optional.empty());
        // when & then
        Assertions.assertThatThrownBy(
                () -> tableService.changeEmpty(10L, changeOrderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정이 되어 있는 테이블은 비어있는지 여부를 변경할 수 없다.")
    @Test
    void 주문_테이블_상태_변경_예외2() {
        // given
        OrderTable orderTable = createOrderTable(5L, 1L, 4, false);
        OrderTable changeOrderTable = createOrderTable(null, null, 주문테이블1.getNumberOfGuests(), !orderTable.isEmpty());
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));
        // when & then
        Assertions.assertThatThrownBy(
                () -> tableService.changeEmpty(orderTable.getId(), changeOrderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 주문 상태가 조리중이거나 식사중이면 비어있는지 여부를 변경할 수 없다.")
    @Test
    void 주문_테이블_상태_변경_예외3() {
        // given
        boolean isEmpty = 주문테이블1.isEmpty();
        OrderTable changeOrderTable = createOrderTable(null, null, 주문테이블1.getNumberOfGuests(), !isEmpty);
        when(orderTableDao.findById(주문테이블1.getId())).thenReturn(Optional.of(주문테이블1));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(주문테이블1.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);
        // when & then
        Assertions.assertThatThrownBy(
                () -> tableService.changeEmpty(주문테이블1.getId(), changeOrderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블에 방문한 손님 수를 변경하고자 할 때, 변경하고자 하는 손님 수가 0보다 작으면 예외가 발생한다.")
    @ParameterizedTest(name = "변경하고자 하는 손님 수: {0}")
    @ValueSource(ints = {-1, -3, -5})
    void 주문_테이블_방문한자수_변경_예외1(int expectNumberOfGuests) {
        // given
        OrderTable changeOrderTable = createOrderTable(null, null, expectNumberOfGuests, 주문테이블1.isEmpty());
        // when & then
        Assertions.assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(주문테이블1.getId(), changeOrderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하지 않는 주문 테이블의 방문한 손님 수를 변경할 수 없다.")
    @Test
    void 주문_테이블_방문한자수_변경_예외2() {
        // given
        OrderTable orderTable = createOrderTable(5L, 1L, 4, false);
        OrderTable changeOrderTable = createOrderTable(null, null, 1, orderTable.isEmpty());
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.empty());
        // when & then
        Assertions.assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(orderTable.getId(), changeOrderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 비어있는 경우, 방문한 손님 수를 변경할 수 없다.")
    @Test
    void 주문_테이블_방문한자수_변경_예외3() {
        // given
        OrderTable orderTable = createOrderTable(5L, 1L, 4, true);
        OrderTable changeOrderTable = createOrderTable(null, null, 1, orderTable.isEmpty());
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));
        // when & then
        Assertions.assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(orderTable.getId(), changeOrderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }

}

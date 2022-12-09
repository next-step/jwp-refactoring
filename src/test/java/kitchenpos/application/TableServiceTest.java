package kitchenpos.application;

import static kitchenpos.domain.OrderTableTestFixture.generateOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 테이블 관련 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    private OrderTable 주문테이블A;
    private OrderTable 주문테이블B;

    @BeforeEach
    void setUp() {
        주문테이블A = generateOrderTable(1L, null, 5, false);
        주문테이블B = generateOrderTable(2L, null, 4, false);
    }

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void createTable() {
        // given
        given(orderTableDao.save(주문테이블A)).willReturn(주문테이블A);

        // when
        OrderTable saveOrderTable = tableService.create(주문테이블A);

        // then
        assertAll(
                () -> assertThat(saveOrderTable.getTableGroupId()).isNull(),
                () -> assertThat(saveOrderTable.getNumberOfGuests()).isEqualTo(주문테이블A.getNumberOfGuests())
        );
    }

    @DisplayName("주문 테이블 전체 목록을 조회한다.")
    @Test
    void findAllTables() {
        // given
        List<OrderTable> orderTables = Arrays.asList(주문테이블A, 주문테이블B);
        given(orderTableDao.findAll()).willReturn(orderTables);

        // when
        List<OrderTable> findOrderTables = tableService.list();

        // then
        assertAll(
                () -> assertThat(findOrderTables).hasSize(orderTables.size()),
                () -> assertThat(findOrderTables).containsExactly(주문테이블A, 주문테이블B)
        );
    }

    @DisplayName("주문 테이블의 비어있는지 여부를 변경한다.")
    @Test
    void changeTableEmpty() {
        // given
        boolean isEmpty = 주문테이블A.isEmpty();
        OrderTable changeOrderTable = generateOrderTable(null, 주문테이블A.getNumberOfGuests(), !isEmpty);
        given(orderTableDao.findById(주문테이블A.getId())).willReturn(Optional.of(주문테이블A));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(주문테이블A.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
        given(orderTableDao.save(주문테이블A)).willReturn(주문테이블A);

        // when
        OrderTable resultOrderTable = tableService.changeEmpty(주문테이블A.getId(), changeOrderTable);

        // then
        assertThat(resultOrderTable.isEmpty()).isEqualTo(!isEmpty);
    }

    @DisplayName("존재하지 않는 주문 테이블의 비어있는지 여부는 변경할 수 없다.")
    @Test
    void changeTableEmptyThrowErrorWhenOrderTableIsNotExists() {
        // given
        boolean isEmpty = 주문테이블A.isEmpty();
        OrderTable changeOrderTable = generateOrderTable(null, 주문테이블A.getNumberOfGuests(), !isEmpty);
        given(orderTableDao.findById(10L)).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableService.changeEmpty(10L, changeOrderTable));
    }

    @DisplayName("단체 지정이 되어 있는 테이블은 비어있는지 여부를 변경할 수 없다.")
    @Test
    void changeTableEmptyThrowErrorWhenTableGroupIdIsNotNull() {
        // given
        OrderTable orderTable = generateOrderTable(5L, 1L, 4, false);
        OrderTable changeOrderTable = generateOrderTable(null, 주문테이블A.getNumberOfGuests(), !orderTable.isEmpty());
        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.of(orderTable));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableService.changeEmpty(orderTable.getId(), changeOrderTable));
    }

    @DisplayName("주문 테이블의 주문 상태가 조리중이거나 식사중이면 비어있는지 여부를 변경할 수 없다.")
    @Test
    void changeTableEmptyThrowErrorWhenOrderStatusIsCooking() {
        // given
        boolean isEmpty = 주문테이블A.isEmpty();
        OrderTable changeOrderTable = generateOrderTable(null, 주문테이블A.getNumberOfGuests(), !isEmpty);
        given(orderTableDao.findById(주문테이블A.getId())).willReturn(Optional.of(주문테이블A));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(주문테이블A.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableService.changeEmpty(주문테이블A.getId(), changeOrderTable));
    }

    @DisplayName("주문 테이블에 방문한 손님 수를 변경한다.")
    @ParameterizedTest(name = "변경된 손님 수: {0}")
    @ValueSource(ints = {6, 10, 13})
    void changeNumberOfGuestsInTable(int expectNumberOfGuests) {
        // given
        OrderTable changeOrderTable = generateOrderTable(null, expectNumberOfGuests, 주문테이블A.isEmpty());
        given(orderTableDao.findById(주문테이블A.getId())).willReturn(Optional.of(주문테이블A));
        given(orderTableDao.save(주문테이블A)).willReturn(주문테이블A);

        // when
        OrderTable resultOrderTable = tableService.changeNumberOfGuests(주문테이블A.getId(), changeOrderTable);

        // then
        assertThat(resultOrderTable.getNumberOfGuests()).isEqualTo(expectNumberOfGuests);
    }

    @DisplayName("주문 테이블에 방문한 손님 수를 변경하고자 할 때, 변경하고자 하는 손님 수가 0보다 작으면 예외가 발생한다.")
    @ParameterizedTest(name = "변경하고자 하는 손님 수: {0}")
    @ValueSource(ints = {-1, -3, -5})
    void changeNumberOfGuestsInTableThrowErrorWhenNumberOfGuestIsSmallerThanZero(int expectNumberOfGuests) {
        // given
        OrderTable changeOrderTable = generateOrderTable(null, expectNumberOfGuests, 주문테이블A.isEmpty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableService.changeNumberOfGuests(주문테이블A.getId(), changeOrderTable));
    }

    @DisplayName("존재하지 않는 주문 테이블의 방문한 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestInTableThrowErrorWhenTableIsNotExists() {
        // given
        OrderTable orderTable = generateOrderTable(5L, 1L, 4, false);
        OrderTable changeOrderTable = generateOrderTable(null, 1, orderTable.isEmpty());
        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableService.changeNumberOfGuests(orderTable.getId(), changeOrderTable));
    }

    @DisplayName("주문 테이블이 비어있는 경우, 방문한 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestInTableThrowErrorWhenTableIsEmpty() {
        // given
        OrderTable orderTable = generateOrderTable(5L, 1L, 4, true);
        OrderTable changeOrderTable = generateOrderTable(null, 1, orderTable.isEmpty());
        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.of(orderTable));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableService.changeNumberOfGuests(orderTable.getId(), changeOrderTable));
    }
}

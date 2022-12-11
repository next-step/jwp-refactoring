package kitchenpos.application;

import static kitchenpos.domain.OrderTableTestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 테이블 비즈니스 로직 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @InjectMocks
    private TableService tableService;

    @Test
    @DisplayName("주문 테이블을 등록한다.")
    void createTable() {
        // given
        given(orderTableDao.save(주문테이블)).willReturn(주문테이블);

        // when
        OrderTable actual = tableService.create(주문테이블);

        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isInstanceOf(OrderTable.class)
        );
    }

    @Test
    @DisplayName("주문 테이블 목록을 조회하면 주문 테이블 목록을 반환한다.")
    void findOrderTables() {
        // given
        OrderTable 주문테이블2 = orderTable(2L, null, 4, false);
        List<OrderTable> orderTables = Arrays.asList(주문테이블, 주문테이블2);
        given(orderTableDao.findAll()).willReturn(orderTables);

        // when
        List<OrderTable> actual = tableService.list();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual).containsExactly(주문테이블, 주문테이블2)
        );
    }

    @Test
    @DisplayName("빈 테이블로 수정시 주문 테이블로 등록되어 있어야 한다.")
    void updateEmptyTableByCreatedOrderTable() {
        // given
        OrderTable changeTable = emptyTableRequest(true);
        given(orderTableDao.findById(주문테이블.getId())).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(주문테이블.getId(), changeTable));
    }

    @Test
    @DisplayName("빈 테이블로 수정시 주문 테이블은 단체 테이블이 아니어야 한다.")
    void updateEmptyTableByNoneTableGroup() {
        // given
        OrderTable changeTable = emptyTableRequest(true);
        OrderTable 단체테이블 = orderTable(2L, 1L, 8, false);
        given(orderTableDao.findById(단체테이블.getId())).willReturn(Optional.of(단체테이블));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(단체테이블.getId(), changeTable));
    }

    @Test
    @DisplayName("빈 테이블로 수정시 주문 상태가 조리 중 또는 식사 중이면 안된다.")
    void updateEmptyTableByOrderStatusIsCookingOrMeal() {
        // given
        OrderTable changeTable = emptyTableRequest(true);
        given(orderTableDao.findById(주문테이블.getId())).willReturn(Optional.of(주문테이블));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(주문테이블.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(true);

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(주문테이블.getId(), changeTable));
    }

    @Test
    @DisplayName("주문 테이블을 빈 테이블로 수정한다.")
    void updateEmptyTable() {
        // given
        OrderTable changeTable = emptyTableRequest(true);
        given(orderTableDao.findById(주문테이블.getId())).willReturn(Optional.of(주문테이블));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(주문테이블.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
        given(orderTableDao.save(주문테이블)).willReturn(주문테이블);

        // when
        OrderTable actual = tableService.changeEmpty(주문테이블.getId(), changeTable);

        // then
        assertTrue(actual.isEmpty());
    }
    
    @ParameterizedTest
    @ValueSource(ints = {-2, -1, 0})
    @DisplayName("주문 테이블 손님 수 수정시 손님은 0명 이상이어야 한다.")
    void updateTableNumberOfGuestByGuestMoreThanZero(int numberOfGuests) {
        // given
        OrderTable changeTable = changeGuestTableRequest(numberOfGuests);

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(주문테이블.getId(), changeTable));
    }

    @Test
    @DisplayName("주문 테이블 손님 수 수정시 주문 테이블로 등록되어 있어야 한다.")
    void updateTableNumberOfGuestByCreatedOrderTable() {
        // given
        OrderTable changeTable = changeGuestTableRequest(3);
        given(orderTableDao.findById(주문테이블.getId())).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(주문테이블.getId(), changeTable));
    }

    @Test
    @DisplayName("빈 테이블은 손님 수를 수정할 수 없다.")
    void updateTableNumberOfGuestByEmptyTable() {
        // given
        OrderTable changeTable = changeGuestTableRequest(3);
        OrderTable 주문테이블 = orderTable(1L, null, 2, true);

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(주문테이블.getId(), changeTable));
    }
    
    @Test
    @DisplayName("주문 테이블의 손님 수를 수정한다.")
    void updateTableNumberOfGuest() {
        // given
        OrderTable changeTable = changeGuestTableRequest(3);
        given(tableService.create(주문테이블)).willReturn(주문테이블);
        given(orderTableDao.findById(주문테이블.getId())).willReturn(Optional.of(주문테이블));

        // when
        OrderTable actual = tableService.changeNumberOfGuests(주문테이블.getId(), changeTable);

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(3);
    }
}

package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.*;
import kitchenpos.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@DisplayName("주문 테이블 서비스에 관련한 기능")
@SpringBootTest
class TableServiceTest {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderTableDao orderTableDao;
    @Autowired
    private TableService tableService;
    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuGroupService menuGroupService;

    private OrderTable orderTable;

    @BeforeEach
    void beforeEach() {
        orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setNumberOfGuests(3);
        orderTable.setEmpty(true);
    }

    @DisplayName("`주문 테이블`을 생성한다.")
    @Test
    void createTable() {
        // Given
        OrderTableRequest request = new OrderTableRequest(3, true);

        // When
        OrderTable actual = tableService.create(request);

        // Then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests()),
                () -> assertThat(actual.isEmpty()).isEqualTo(request.isEmpty()),
                () -> assertThat(actual.getTableGroupId()).isNull()
        );
    }

    @DisplayName("모든 `주문 테이블` 목록을 조회한다.")
    @Test
    void findAllTables() {
        // Given
        given(orderTableDao.findAll()).willReturn(Collections.singletonList(orderTable));

        // When
        List<OrderTable> actual = tableService.list();

        // Then
        assertAll(
                () -> assertThat(actual).extracting(OrderTable::getId)
                        .containsExactly(orderTable.getId()),
                () -> assertThat(actual).extracting(OrderTable::getNumberOfGuests)
                        .containsExactly(orderTable.getNumberOfGuests()),
                () -> assertThat(actual).extracting(OrderTable::isEmpty)
                        .containsExactly(orderTable.isEmpty()),
                () -> assertThat(actual).extracting(OrderTable::getTableGroupId)
                        .containsExactly(orderTable.getTableGroupId())
        );
    }

    @DisplayName("`주문 테이블`을 비어있는 상태로 변경한다.")
    @Test
    void changeEmpty() {
        // Given
        OrderTable orderTable = tableService.create(new OrderTableRequest(3, false));
        OrderTableRequest request = new OrderTableRequest(true);

        // When
        OrderTable actual = tableService.changeEmpty(orderTable.getId(), request);

        // Then
        assertThat(actual.isEmpty()).isEqualTo(request.isEmpty());
    }

    @DisplayName("`주문 테이블`이 `단체 지정`되어 있으면 비어있는 상태로 변경할 수 없다.")
    @Test
    void exceptionToChangeEmptyWithTableGroup() {
        // Given
        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.of(orderTable));
        orderTable.setTableGroupId(1L);
        OrderTable updateOrderTable = new OrderTable();
        updateOrderTable.setEmpty(false);

        // When & Then
        assertThatThrownBy(() -> tableService.changeEmpty(this.orderTable.getId(), updateOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("`주문 테이블`의 `주문 상태`가 'COOKING' 이나 'MEAL' 상태가 이면 비어있는 상태로 변경할 수 없다.")
    @Test
    void exceptionToChangeEmptyWithCookingAndMeal() {
        // Given
        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.of(orderTable));
        List<String> orderStatuses = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), orderStatuses))
                .willReturn(true);
        OrderTable updateOrderTable = new OrderTable();
        updateOrderTable.setEmpty(false);

        // when & Then
        assertThatThrownBy(() -> tableService.changeEmpty(this.orderTable.getId(), updateOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("`주문 테이블`에 `방문한 손님 수`를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // Given
        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.of(orderTable));
        given(orderTableDao.save(orderTable)).willReturn(orderTable);
        orderTable.setEmpty(false);
        OrderTable updateOrderTable = new OrderTable();
        updateOrderTable.setNumberOfGuests(5);

        // When
        OrderTable actual = tableService.changeNumberOfGuests(orderTable.getId(), updateOrderTable);

        // Then
        assertEquals(updateOrderTable.getNumberOfGuests(), actual.getNumberOfGuests());
    }

    @DisplayName("하나의 `주문 테이블`의 `방문한 손님 수`가 0명보다 적으면, `방문한 손님 수`를 변경할 수 없다.")
    @Test
    void exceptionToChangeNumberOfGuestsWithoutGuests() {
        // Given
        OrderTable updateOrderTable = new OrderTable();
        updateOrderTable.setNumberOfGuests(-1);

        // When & Then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), updateOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("`주문 테이블`이 비어있으면, `방문한 손님 수`를 변경할 수 없다.")
    @Test
    void exceptionToChangeNumberOfGuestsWithoutOrderTable() {
        // Given
        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.of(orderTable));
        orderTable.setEmpty(true);
        OrderTable updateOrderTable = new OrderTable();
        updateOrderTable.setNumberOfGuests(5);

        // When & Then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), updateOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

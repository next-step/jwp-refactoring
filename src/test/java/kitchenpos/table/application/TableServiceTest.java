package kitchenpos.table.application;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.generator.BuilderArbitraryGenerator;
import kitchenpos.order.domain.Order;
import kitchenpos.order.persistence.OrderDao;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.persistence.OrderTableDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import net.jqwik.api.Arbitraries;
import org.junit.jupiter.api.BeforeAll;
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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    @InjectMocks
    private TableService tableService;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    public static FixtureMonkey fixtureMonkey;

    @BeforeAll
    public static void setup() {
        fixtureMonkey = FixtureMonkey.builder()
                .defaultGenerator(BuilderArbitraryGenerator.INSTANCE)
                .build();
    }

    @DisplayName("주문테이블을 생성할 경우 주문테이블을 반환")
    @Test
    public void returnOderTable() {
        OrderTable orderTable = fixtureMonkey
                .giveMeBuilder(OrderTable.class)
                .set("tableGroupId",443l)
                .sample();
        doReturn(orderTable).when(orderTableDao).save(any(OrderTable.class));

        assertThat(tableService.create(new OrderTableRequest()).getTableGroupId()).isEqualTo(443l);
    }

    @DisplayName("주문테이블목록을 조회할경우 주문테이블목록 반환")
    @Test
    public void returnOderTables() {
        List<OrderTable> orderTables = fixtureMonkey
                .giveMeBuilder(OrderTable.class).set("id", 13l)
                .set("empty", true)
                .sampleList(100);
        doReturn(orderTables).when(orderTableDao).findAll();

        List<OrderTableResponse> returnedTables = tableService.list();
        assertAll(() -> assertThat(returnedTables).hasSize(100), () -> assertThat(returnedTables.stream().map(OrderTableResponse::getId)).allMatch(id -> id == 13l), () -> assertThat(returnedTables.stream().map(OrderTableResponse::isEmpty)).allMatch(empty -> empty));
    }

    @DisplayName("주문테이블의 공석여부를 수정할 경우 주문테이블이 등록안되있으면 예외발생")
    @Test
    public void throwsExceptionWhenGroupIdIsNull() {
        OrderTable orderTable = fixtureMonkey
                .giveMeBuilder(OrderTable.class)
                .sample();
        doReturn(Optional.empty()).when(orderTableDao).findById(orderTable.getId());

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), OrderTable.builder().build())).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블의 공석여부를 수정할 경우 테이블그룹이 존재하면 예외발생")
    @Test
    public void throwsExceptionWhenExistsTableGroup() {
        List<Order> orders = fixtureMonkey.giveMeBuilder(Order.class)
                .set("orderStatus",OrderStatus.COOKING.name())
                .sampleList(5);
        OrderTable orderTable = fixtureMonkey
                .giveMeBuilder(OrderTable.class)
                .set("tableGroupId", 13l).sample();
        doReturn(Optional.ofNullable(orderTable))
                .when(orderTableDao)
                .findById(orderTable.getId());
        doReturn(orders).when(orderDao)
                .findAllByOrderTableId(orderTable.getId());
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), OrderTable.builder().build())).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블의 공석여부를 수정할 경우 테이블이 조리중이나 식사중이면 예외발생")
    @Test
    public void throwsExceptionWhenExistsTableGroupAndMillOrCook() {
        List<Order> orders = fixtureMonkey.giveMeBuilder(Order.class)
                .set("orderStatus",OrderStatus.COOKING.name())
                .sampleList(5);
        OrderTable orderTable = fixtureMonkey
                .giveMeBuilder(OrderTable.class)
                .setNull("tableGroupId")
                .sample();
        doReturn(Optional.ofNullable(orderTable)).when(orderTableDao).findById(orderTable.getId());
        doReturn(orders).when(orderDao)
                .findAllByOrderTableId(orderTable.getId());

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), OrderTable.builder().build())).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블의 공석여부를 수정하면 수정된 테이블정보을 반환")
    @Test
    public void returnOrderTableWithEmpty() {
        List<Order> orders = fixtureMonkey.giveMeBuilder(Order.class)
                .set("orderStatus",OrderStatus.COMPLETION.name())
                .sampleList(5);
        OrderTable orderTable = fixtureMonkey
                .giveMeBuilder(OrderTable.class)
                .setNull("tableGroupId")
                .set("empty", false)
                .sample();
        OrderTable savedTable = fixtureMonkey
                .giveMeBuilder(OrderTable.class)
                .setNull("tableGroupId")
                .set("empty", true)
                .sample();
        doReturn(Optional.ofNullable(savedTable)).when(orderTableDao).findById(orderTable.getId());
        doReturn(savedTable).when(orderTableDao).save(savedTable);
        assertThat(tableService.changeEmpty(orderTable.getId(), orderTable).isEmpty()).isFalse();
    }

    @DisplayName("주문테이블의 손님수를 수정할 경우 손님수가 0보다 작으면 예외발생")
    @Test
    public void throwsExceptionWhenGuestNumberIsNegative() {
        OrderTable orderTable = fixtureMonkey
                .giveMeBuilder(OrderTable.class)
                .set("numberOfGuests", Arbitraries.integers().lessOrEqual(-1))
                .sample();

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블의 손님수를 수정할 경우 테이블이 등록안되있으면 예외발생")
    @Test
    public void throwsExceptionWhenNoneExistsTable() {
        OrderTable orderTable = fixtureMonkey
                .giveMeBuilder(OrderTable.class)
                .set("numberOfGuests", Arbitraries.integers().greaterOrEqual(0))
                .sample();
        doReturn(Optional.empty()).when(orderTableDao).findById(orderTable.getId());

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블의 손님수를 수정할 경우 테이블이 공석이면 예외발생")
    @Test
    public void throwsExceptionWhenEmptyTable() {
        OrderTable orderTable = fixtureMonkey
                .giveMeBuilder(OrderTable.class)
                .set("numberOfGuests", Arbitraries.integers().greaterOrEqual(0))
                .set("empty", true)
                .sample();
        doReturn(Optional.ofNullable(orderTable)).when(orderTableDao).findById(orderTable.getId());

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블의 손님수를 수정할 경우 수정된 테이블을 반환")
    @Test
    public void returnOrderTableWithGuest() {
        OrderTable orderTable = fixtureMonkey.giveMeBuilder(OrderTable.class)
                .set("numberOfGuests", 15)
                .set("empty", false)
                .sample();
        OrderTable findTable = fixtureMonkey.giveMeBuilder(OrderTable.class)
                .set("numberOfGuests", 5)
                .set("empty", false)
                .sample();
        doReturn(Optional.ofNullable(findTable)).when(orderTableDao).findById(orderTable.getId());
        doReturn(findTable).when(orderTableDao).save(findTable);

        assertThat(tableService.changeNumberOfGuests(orderTable.getId(), orderTable).getNumberOfGuests()).isEqualTo(15);

    }
}

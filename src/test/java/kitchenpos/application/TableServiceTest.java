package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import kitchenpos.application.fixture.OrderTableFixtureFactory;
import kitchenpos.application.fixture.TableGroupFixtureFactory;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
    private TableService tableService;

    private TableGroup 단체_1;
    private OrderTable 빈_테이블;
    private OrderTable 주문_테이블;
    private OrderTable 주문_테이블_10명;
    private OrderTable 단체_1_주문_테이블;

    @BeforeEach
    void setUp() {
        빈_테이블 = OrderTableFixtureFactory.create(1L, true);
        주문_테이블 = OrderTableFixtureFactory.create(1L, false);
        주문_테이블_10명 = OrderTableFixtureFactory.createWithGuest(1L, false, 10);

        단체_1 = TableGroupFixtureFactory.create(1L);
        단체_1_주문_테이블 = OrderTableFixtureFactory.create(2L, true);
    }

    @DisplayName("테이블을 등록할 수 있다.")
    @Test
    void create01() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);

        given(orderTableDao.save(any(OrderTable.class))).willReturn(빈_테이블);

        // when
        OrderTable createdOrderTable = tableService.create(orderTable);

        // then
        assertThat(createdOrderTable).isEqualTo(빈_테이블);
    }

    @DisplayName("테이블 목록을 조회할 수 있다.")
    @Test
    void find01() {
        // given
        given(orderTableDao.findAll()).willReturn(Lists.newArrayList(빈_테이블, 주문_테이블));

        // when
        List<OrderTable> orderTables = tableService.list();

        // then
        assertThat(orderTables).containsExactly(빈_테이블, 주문_테이블);
    }

    @DisplayName("테이블의 상태를 빈 테이블 상태로 변경할 수 있다.")
    @Test
    void change01() {
        // given
        주문_테이블.setEmpty(true);

        given(orderTableDao.findById(주문_테이블.getId())).willReturn(Optional.ofNullable(주문_테이블));
        given(orderTableDao.save(any(OrderTable.class))).willReturn(주문_테이블);

        // when
        OrderTable changedOrderTable = tableService.changeEmpty(주문_테이블.getId(), 빈_테이블);

        // then
        assertAll(
                () -> assertThat(changedOrderTable).isEqualTo(주문_테이블),
                () -> assertThat(changedOrderTable.isEmpty()).isTrue()
        );
    }

    @DisplayName("테이블이 존재하지 않으면 빈 테이블 상태로 변경할 수 없다.")
    @Test
    void change02() {
        // given
        given(orderTableDao.findById(주문_테이블.getId())).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeEmpty(주문_테이블.getId(), 빈_테이블));
    }

    @DisplayName("테이블 그룹이 존재하면 빈 테이블 상태로 변경할 수 없다.")
    @Test
    void change03() {
        // given
        단체_1_주문_테이블.setTableGroupId(단체_1.getId());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeEmpty(단체_1_주문_테이블.getId(), 빈_테이블));
    }

    @DisplayName("테이블의 주문 상태가 COOKING 상태이면 빈 테이블 상태로 변경할 수 없다.")
    @Test
    void change04() {
        // given
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(true);
        given(orderTableDao.findById(주문_테이블.getId())).willReturn(Optional.ofNullable(주문_테이블));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeEmpty(주문_테이블.getId(), 빈_테이블));
    }

    @DisplayName("테이블의 손님 수를 변경할 수 있다.")
    @Test
    void change05() {
        // given
        given(orderTableDao.findById(주문_테이블.getId())).willReturn(Optional.ofNullable(주문_테이블));
        given(orderTableDao.save(any(OrderTable.class))).willReturn(주문_테이블);

        // when
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(주문_테이블.getId(), 주문_테이블_10명);

        // then
        assertAll(
                () -> assertThat(changedOrderTable).isEqualTo(주문_테이블),
                () -> assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(주문_테이블_10명.getNumberOfGuests())
        );
    }

    @DisplayName("테이블의 변경하려는 손님 수는 1명 이상이어야 한다.")
    @ParameterizedTest
    @ValueSource(ints = {-100, -1})
    void change06(int numberOfGuest) {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(numberOfGuest);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeNumberOfGuests(주문_테이블.getId(), orderTable));
    }

    @DisplayName("테이블이 없으면 손님 수를 변경할 수 없다.")
    @Test
    void change07() {
        // given
        given(orderTableDao.findById(주문_테이블.getId())).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeNumberOfGuests(주문_테이블.getId(), 주문_테이블_10명));
    }

    @DisplayName("테이블이 비어있으면 테이블 손님 수를 변경할 수 없다.")
    @Test
    void change08() {
        // given & when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeNumberOfGuests(빈_테이블.getId(), 주문_테이블_10명));
    }
}
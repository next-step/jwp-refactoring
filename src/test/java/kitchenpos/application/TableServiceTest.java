package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixtures.OrderTableFixtures;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

/**
 * packageName : kitchenpos.application
 * fileName : TableServiceTest
 * author : haedoang
 * date : 2021/12/17
 * description :
 */
@Disabled
@DisplayName("테이블 통합 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    private OrderTable orderTable;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @BeforeEach
    void setUp() {
//        orderTable = OrderTableFixtures.createOrderTable(1L, null, 2, false);
    }

    @Test
    @DisplayName("테이블 목록을 조회할 수 있다.")
    public void list() throws Exception {
        // given
        given(orderTableDao.findAll()).willReturn(Lists.newArrayList(orderTable));

        // when
        List<OrderTable> lists = tableService.list();

        // then
        assertThat(lists).hasSize(1);
    }

    @Test
    @DisplayName("테이블을 등록할 수 있다.")
    public void createTable() throws Exception {
        // when
        given(orderTableDao.save(any(OrderTable.class))).willReturn(orderTable);
        OrderTable actual = tableService.create(orderTable);

        // then
        assertThat(actual).isEqualTo(orderTable);
    }

    @Test
    @DisplayName("테이블의 상태를 변경할 수 있다.")
    public void changeTableStatus() {
        // given
        boolean before = orderTable.isEmpty();
//        orderTable.setEmpty(!before);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(false);
        given(orderTableDao.save(any(OrderTable.class))).willReturn(orderTable);

        // when
        OrderTable actual = tableService.changeEmpty(orderTable.getId(), orderTable);

        // then
        assertThat(actual.isEmpty()).isEqualTo(!before);
    }

    @Test
    @DisplayName("존재하지 않은 테이블은 상태를 변경할 수 없다.")
    public void changeTableStatusFailByUnknownTable() throws Exception {
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블이 그룹 테이블인 경우 상태를 변경할 수 없다.")
    public void changeTableStatusFailByGroupTable() throws Exception {
        // given
//        orderTable.setTableGroupId(1L);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));

        // then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블의 주문 상태가 조리, 식사인 경우 변경할 수 없다.")
    public void changeTableStatusFailByTableOrderStatus() throws Exception {
        // given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(true);

        // then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블의 사용자 수를 변경할 수 있다.")
    public void changeNumberOfGuests() throws Exception {
        // given
        int before = orderTable.getNumberOfGuests();
//        orderTable.setNumberOfGuests(before + 1);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));
        given(orderTableDao.save(any(OrderTable.class))).willReturn(orderTable);

        // when
        OrderTable actual = tableService.changeNumberOfGuests(this.orderTable.getId(), this.orderTable);

        // then
        assertThat(actual.getNumberOfGuests()).isNotEqualTo(before);
        assertThat(actual.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
    }

    @DisplayName("테이블 사용자 수가 올바르지 않을 경우 테이블을 등록할 수 없다.")
    @ParameterizedTest(name = "테이블 사용자 수는 0명 이상이어야 한다: " + ParameterizedTest.ARGUMENTS_PLACEHOLDER)
    @ValueSource(ints = {Integer.MIN_VALUE, -10, -5, -1})
    public void changeNumberOfGuestsByInvalidNumber(int candidate) {
        //given
//        orderTable.setNumberOfGuests(candidate);

        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않은 테이블의 사용자 수를 변경할 수 없다.")
    public void changeNumberOfGuestsByUnknownTable() throws Exception {
        // given
        given(orderTableDao.findById(anyLong())).willThrow(IllegalArgumentException.class);

        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 테이블의 사용자 수를 변경할 수 없다.")
    public void changeNumberOfGuestsByEmptyTable() throws Exception {
        // given
//        orderTable.setEmpty(true);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(orderTable));

        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable)).isInstanceOf(IllegalArgumentException.class);
    }
}

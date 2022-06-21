package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.application.fixture.OrderTableFixtureFactory;
import kitchenpos.application.fixture.TableGroupFixtureFactory;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
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
    private TableService tableService;

    private OrderTable 주문_테이블1;
    private OrderTable 주문_테이블2;
    private OrderTable 빈주문_테이블;
    private OrderTable 손님_주문_테이블_5명;

    private TableGroup 단체;

    @BeforeEach
    void before() {
        주문_테이블1 = OrderTableFixtureFactory.create(1L, false);
        빈주문_테이블 = OrderTableFixtureFactory.create(1L, true);
        주문_테이블2 = OrderTableFixtureFactory.create(2L, false);
        손님_주문_테이블_5명 = OrderTableFixtureFactory.createByGuestNumber(1L, 5,false);
        단체 = TableGroupFixtureFactory.create(1L);
    }

    @Test
    @DisplayName("주문 테이블을 생성 할 수 있다.")
    void createTest() {
        //given
        given(orderTableDao.save(주문_테이블1)).willReturn(주문_테이블1);

        //when
        OrderTable orderTable = tableService.create(주문_테이블1);

        //then
        assertThat(orderTable).isEqualTo(주문_테이블1);
    }

    @Test
    @DisplayName("주문 테이블 목록을 조회 할 수 있다.")
    void listTest() {
        //given
        given(orderTableDao.findAll()).willReturn(Arrays.asList(주문_테이블1, 주문_테이블2));

        //when
        List<OrderTable> tables = tableService.list();

        //then
        assertThat(tables).containsExactly(주문_테이블1, 주문_테이블2);
    }

    @Test
    @DisplayName("주문 테이블이 시스템에 등록 되어 있지 않으면 빈테이블로 변경 할 수 없다.")
    void changeEmptyFailTest01() {
        //given
        given(orderTableDao.findById(주문_테이블1.getId())).willThrow(IllegalArgumentException.class);

        //when & then
        assertThatThrownBy(
                () -> tableService.changeEmpty(주문_테이블1.getId(), 주문_테이블1)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 단체 지정 되어 있으면 빈테이블로 지정 할 수 없다.")
    void changeEmptyFailTest02() {
        //given
        주문_테이블1.setTableGroupId(단체.getId());
        given(orderTableDao.findById(주문_테이블1.getId())).willReturn(Optional.of(주문_테이블1));

        //when & then
        assertThatThrownBy(
                () -> tableService.changeEmpty(주문_테이블1.getId(), 주문_테이블1)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("조리 중(COOKING), 식사 중(MEAL) 상태에 있으면 빈테이블로 지정 할 수 없다.")
    void changeEmptyFailTest03() {
        //given
        given(orderTableDao.findById(주문_테이블1.getId())).willReturn(Optional.of(주문_테이블1));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(true);

        //when & then
        assertThatThrownBy(
                () -> tableService.changeEmpty(주문_테이블1.getId(), 주문_테이블1)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블을 빈테이블로 변경 할 수 있다.")
    void changeEmptyTest03() {
        //given
        given(orderTableDao.findById(주문_테이블1.getId())).willReturn(Optional.of(주문_테이블1));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(false);
        given(orderTableDao.save(any(OrderTable.class))).willReturn(주문_테이블1);

        //when
        OrderTable orderTable = tableService.changeEmpty(주문_테이블1.getId(), 빈주문_테이블);

        //then
        assertThat(orderTable).isEqualTo(주문_테이블1);
    }

    @Test
    @DisplayName("변경하려는 손님수가 0 보다 작을 수 없다.")
    void changeNumberOfGuestsFailTest01() {
        //given
        OrderTable orderTable = new OrderTable(1L, -1, false);

        //when & then
        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 시스템에 등록 되어 있지 않으면 손님수를 변경 할 수 없다.")
    void changeNumberOfGuestsFailTest02() {
        //given
        OrderTable orderTable = new OrderTable(1L, 5, false);
        given(orderTableDao.findById(orderTable.getId())).willThrow(IllegalArgumentException.class);

        //when & then
        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈테이블 이면 손님수를 변경 할 수 없다.")
    void changeNumberOfGuestsFailTest03() {
        //given
        OrderTable orderTable = new OrderTable(1L, 5, true);
        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.of(orderTable));

        //when & then
        assertThatThrownBy(
                () -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블에 방문한 손님수를 변경 할 수 있다.")
    void changeNumberOfGuestsTest() {
        //given
        given(orderTableDao.findById(손님_주문_테이블_5명.getId())).willReturn(Optional.of(손님_주문_테이블_5명));
        given(orderTableDao.save(손님_주문_테이블_5명)).willReturn(손님_주문_테이블_5명);

        //when
        OrderTable orderTable = tableService.changeNumberOfGuests(손님_주문_테이블_5명.getId(), 손님_주문_테이블_5명);

        //then
        assertThat(orderTable).isEqualTo(손님_주문_테이블_5명);
    }
}

package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("주문테이블 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @InjectMocks
    private TableService tableService;

    @DisplayName("주문테이블 등록")
    @Test
    public void 등록_확인() throws Exception {
        //given
        OrderTable returnOrderTable = 주문테이블_등록됨(1L, 1L, false, 5);
        given(orderTableDao.save(any(OrderTable.class))).willReturn(returnOrderTable);

        //when
        OrderTable orderTable = 주문테이블_생성(1L, false, 5);
        OrderTable createOrderTable = tableService.create(orderTable);

        //then
        assertThat(createOrderTable.getId()).isNotNull();
    }

    @DisplayName("주문테이블 목록 조회")
    @Test
    public void 목록_조회_확인() throws Exception {
        //given
        OrderTable orderTable1 = 주문테이블_등록됨(1L, 1L, false, 5);
        OrderTable orderTable2 = 주문테이블_등록됨(2L, 2L, false, 5);
        OrderTable orderTable3 = 주문테이블_등록됨(3L, 3L, false, 5);
        given(orderTableDao.findAll()).willReturn(Arrays.asList(orderTable1, orderTable2, orderTable3));

        //when
        List<OrderTable> orderTables = tableService.list();

        //then
        assertThat(orderTables.size()).isEqualTo(3);
    }

    @DisplayName("주문테이블 빈테이블 여부 변경")
    @Test
    public void 빈테이블여부_변경_확인() throws Exception {
        //given
        OrderTable orderTable = 주문테이블_등록됨(1L, null, false, 5);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(1L,
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
        orderTable.setEmpty(true);
        given(orderTableDao.save(any(OrderTable.class))).willReturn(orderTable);

        //when
        OrderTable changeOrderTable = 주문테이블_생성(1L, true, 5);
        OrderTable saveOrderTable = tableService.changeEmpty(1L, changeOrderTable);

        //then
        assertThat(saveOrderTable.isEmpty()).isTrue();
    }

    @DisplayName("주문테이블 빈테이블 여부 변경 예외 - 주문테이블이 존재하지않는 경우")
    @Test
    public void 주문테이블이존재하지않는경우_빈테이블여부_변경_예외() throws Exception {
        //given
        given(orderTableDao.findById(1L)).willThrow(IllegalArgumentException.class);

        //when
        //then
        OrderTable changeOrderTable = 주문테이블_생성(1L, false, 5);
        assertThatThrownBy(() -> tableService.changeEmpty(1L, changeOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블 빈테이블 여부 변경 예외 - 주문테이블이 단체 지정되어있는 경우")
    @Test
    public void 단체지정인경우_빈테이블여부_변경_예외() throws Exception {
        //given
        OrderTable orderTable = 주문테이블_등록됨(1L, 1L, false, 5);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));

        //when
        //then
        OrderTable changeOrderTable = 주문테이블_생성(1L, false, 5);
        assertThatThrownBy(() -> tableService.changeEmpty(1L, changeOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블 빈테이블 여부 변경 예외 - 주문상태가 조리나 식사인 경우")
    @Test
    public void 주문상태가조리나식사인경우_빈테이블여부_변경_예외() throws Exception {
        //given
        OrderTable orderTable = 주문테이블_등록됨(1L, null, false, 5);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(1L,
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        //when
        //then
        OrderTable changeOrderTable = 주문테이블_생성(1L, false, 5);
        assertThatThrownBy(() -> tableService.changeEmpty(1L, changeOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블 방문한 손님 수 변경")
    @Test
    public void 방문한손님수_변경_확인() throws Exception {
        //given
        OrderTable orderTable = 주문테이블_등록됨(1L, null, false, 5);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));
        orderTable.setNumberOfGuests(2);
        given(orderTableDao.save(any(OrderTable.class))).willReturn(orderTable);

        //when
        OrderTable changeOrderTable = 주문테이블_생성(1L, false, 2);
        OrderTable saveOrderTable = tableService.changeNumberOfGuests(1L, changeOrderTable);

        //then
        assertThat(saveOrderTable.getNumberOfGuests()).isEqualTo(2);
    }

    @DisplayName("주문테이블 방문한 손님 수 변경 예외 - 방문한 손님 수가 음수인 경우")
    @Test
    public void 방문한손님수가음수인경우_방문한손님수변경_예외() throws Exception {
        //when
        //then
        OrderTable changeOrderTable = 주문테이블_생성(1L, false, 2);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, changeOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블 방문한 손님 수 변경 예외 - 주문테이블이 존재하지않는 경우")
    @Test
    public void 주문테이블이존재하지않는경우_방문한손님수변경_예외() throws Exception {
        //given
        given(orderTableDao.findById(1L)).willThrow(IllegalArgumentException.class);

        //when
        //then
        OrderTable changeOrderTable = 주문테이블_생성(1L, false, 2);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, changeOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블 방문한 손님 수 변경 예외 - 주문테이블이 빈테이블인 경우")
    @Test
    public void 주문테이블이빈테이블인경우_방문한손님수변경_예외() throws Exception {
        //given
        OrderTable orderTable = 주문테이블_등록됨(1L, null, true, 5);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));

        //when
        //then
        OrderTable changeOrderTable = 주문테이블_생성(1L, false, 2);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, changeOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    public static OrderTable 주문테이블_생성(Long tableGroupId, boolean empty, int numberOfGuests) {
        OrderTable orderTable = new OrderTable();
        orderTable.setTableGroupId(tableGroupId);
        orderTable.setEmpty(empty);
        orderTable.setNumberOfGuests(numberOfGuests);
        return orderTable;
    }

    public static OrderTable 주문테이블_등록됨(Long id, Long tableGroupId, boolean empty, int numberOfGuests) {
        OrderTable orderTable = 주문테이블_생성(tableGroupId, empty, numberOfGuests);
        orderTable.setId(id);
        return orderTable;
    }
}

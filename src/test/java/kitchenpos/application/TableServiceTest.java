package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.application.OrderServiceTest.주문_생성;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@DisplayName("주문 테이블 관련 기능 테스트")
@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @BeforeEach
    void setUp() {
        orderTable1 = 주문테이블_생성(1L, 0, true);
        orderTable2 = 주문테이블_생성(2L, 0, true);
    }

    @DisplayName("단체 지정되지 않은 주문 테이블을 등록한다.")
    @Test
    void create() {
        given(orderTableDao.save(orderTable1)).willReturn(orderTable1);

        OrderTable createdOrderTable = 주문테이블_생성_요청();

        주문테이블_생성됨(createdOrderTable);
    }

    @DisplayName("주문 테이블 조회한다.")
    @Test
    void list() {
        given(orderTableDao.findAll()).willReturn(Arrays.asList(orderTable1, orderTable2));

        List<OrderTable> orderTables = 주문테이블_목록_조회요청();

        주문테이블_목록_조회됨(orderTables);
    }

    @DisplayName("단체에 속한 주문 테이블은 비울수 없다.")
    @Test
    void changeEmpty_단체속한_경우_예외() {
        주문테이블_단체_설정();

        단체에_속한_주문테이블_해제시_예외발생();

    }

    @DisplayName("주문상태가 조리 또는 식사가 아니라면 예외 발생")
    @Test
    void changeEmpty_주문상태_완료_예외() {
        주문상태_완료_설정();
        주문상태_완료일경우_해제시_예외발생();
    }

    @DisplayName("주문테이블을 비울 수 있다.")
    @Test
    void changeEmpty() {
        주문테이블_비움_설정();

        OrderTable orderTable = 주문테이블_비움요청();

        주문테이블_비움_완료됨(orderTable);
    }

    @DisplayName("주문 테이블의 손님이 1명 이상이어야 한다.")
    @Test
    void changeNumberOfGuests_손님_0명_예외() {
        주문테이블_손님_수_0명_이하_설정();

        주문테이블_손님_수_0명_이하로_변경시_예외발생();
    }

    @DisplayName("주문 테이블 손님 수 변경시 비어있지 않아야 한다.")
    @Test
    void changeNumberOfGuests_주문테이블_없을경우_예외() {
        주문테이블_비어있음_설정();

        주문테이블_손님_수_1명이상_설정();

        주문테이블_손님_수_변경요청시_비어있으면_예외_발생();
    }

    @DisplayName("주문 테이블의 손님의 수를 입력한다.")
    @Test
    void changeNumberOfGuests() {
        주문테이블_손님_수_1명_이상_입력();

        OrderTable orderTable = 주문테이블_손님_수_변경_요청();

        주문테이블_손님_수_변경됨(orderTable);
    }

    public static OrderTable 주문테이블_생성(long id, int numberOfGuests, boolean isEmpty) {
        return new OrderTable(id, numberOfGuests, isEmpty);
    }

    private OrderTable 주문테이블_생성_요청() {
        return tableService.create(this.orderTable1);
    }

    private void 주문테이블_생성됨(OrderTable createdOrderTable) {
        assertThat(createdOrderTable.getId()).isEqualTo(this.orderTable1.getId());
        assertThat(createdOrderTable.getNumberOfGuests()).isEqualTo(this.orderTable1.getNumberOfGuests());
        assertThat(createdOrderTable.isEmpty()).isEqualTo(this.orderTable1.isEmpty());
    }

    private List<OrderTable> 주문테이블_목록_조회요청() {
        return tableService.list();
    }

    private void 주문테이블_목록_조회됨(List<OrderTable> orderTables) {
        assertThat(orderTables).containsExactly(orderTable1, orderTable2);
        assertThat(orderTables.get(0).getId()).isEqualTo(orderTable1.getId());
        assertThat(orderTables.get(0).getNumberOfGuests()).isEqualTo(orderTable1.getNumberOfGuests());
        assertThat(orderTables.get(0).isEmpty()).isEqualTo(orderTable1.isEmpty());
        assertThat(orderTables.get(1).getId()).isEqualTo(orderTable2.getId());
        assertThat(orderTables.get(1).getNumberOfGuests()).isEqualTo(orderTable2.getNumberOfGuests());
        assertThat(orderTables.get(1).isEmpty()).isEqualTo(orderTable2.isEmpty());
    }

    private void 주문테이블_단체_설정() {
        given(orderTableDao.findById(orderTable1.getId())).willReturn(Optional.of(orderTable1));
        orderTable1.updateTableGroupId(1L);
    }

    private void 단체에_속한_주문테이블_해제시_예외발생() {
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), orderTable1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private void 주문상태_완료_설정() {
        given(orderTableDao.findById(orderTable1.getId())).willReturn(Optional.of(orderTable1));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable1.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

    }

    private void 주문상태_완료일경우_해제시_예외발생() {
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), orderTable1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private void 주문테이블_비움_설정() {
        given(orderTableDao.findById(orderTable1.getId())).willReturn(Optional.of(orderTable1));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable1.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
        given(orderTableDao.save(orderTable1)).willReturn(orderTable1);
        orderTable1.updateEmpty(false);
    }

    private OrderTable 주문테이블_비움요청() {
        return tableService.changeEmpty(orderTable1.getId(), orderTable1);
    }

    private void 주문테이블_비움_완료됨(OrderTable orderTable) {
        assertThat(orderTable.isEmpty()).isFalse();
    }

    private void 주문테이블_손님_수_0명_이하_설정() {
        orderTable1.updateEmpty(false);
        orderTable1.updateNumberOfGuests(-10);
    }

    private void 주문테이블_손님_수_0명_이하로_변경시_예외발생() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(orderTable1.getId(), orderTable1));
    }

    private void 주문테이블_비어있음_설정() {
        orderTable1.updateEmpty(true);
    }

    private void 주문테이블_손님_수_변경요청시_비어있으면_예외_발생() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(orderTable1.getId(), orderTable1));
    }

    private void 주문테이블_손님_수_1명이상_설정() {
        orderTable1.updateNumberOfGuests(15);
    }

    private void 주문테이블_손님_수_1명_이상_입력() {
        given(orderTableDao.findById(orderTable1.getId())).willReturn(Optional.of(orderTable1));
        given(orderTableDao.save(orderTable1)).willReturn(orderTable1);
        orderTable1.updateEmpty(false);
        orderTable1.updateNumberOfGuests(15);
    }

    private OrderTable 주문테이블_손님_수_변경_요청() {
        return tableService.changeNumberOfGuests(orderTable1.getId(), orderTable1);
    }

    private void 주문테이블_손님_수_변경됨(OrderTable orderTable) {
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(orderTable1.getNumberOfGuests());
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(15);
    }
}

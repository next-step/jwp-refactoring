package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.repository.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("테이블 - 식당의 테이블")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    @Test
    @DisplayName("주문 테이블을 생성.")
    void createOrderTable() {
        //given
        OrderTableRequest orderTableRequest = new OrderTableRequest();
        OrderTable orderTable = new OrderTable(1L,null,0, false);
        given(orderTableRepository.save(any())).willReturn(orderTable);

        //when
        final OrderTableResponse saveOrderTable = tableService.create(orderTableRequest);

        //then
        assertAll(
                () -> assertThat(saveOrderTable.getId()).isEqualTo(orderTable.getId()),
                () -> assertThat(saveOrderTable.getTableGroupId()).isNull()

        );

    }

    @Test
    @DisplayName("주문 테이블을 조회한다.")
    void orderList() {
        //given
        OrderTable orderTable1 = new OrderTable(1L, null, 3, false);
        OrderTable orderTable2 = new OrderTable(2L, null, 3, false);
        given(orderTableRepository.findAll()).willReturn(Arrays.asList(orderTable1, orderTable2));

        //when
        final List<OrderTable> list = tableService.list();

        //then
        assertAll(
                () -> assertThat(list).hasSize(2),
                () -> assertThat(list).extracting("id").contains(orderTable1.getId(), orderTable2.getId())
        );

    }


    @Test
    @DisplayName("존재하지 않은 테이블인 경우에는 변경 할수 없다.")
    void emptyTable() {
        //given
        given(orderTableDao.findById(any())).willReturn(Optional.empty());

        //when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(1L, OrderTable.createOrderTable()));
    }


    @Test
    @DisplayName("단체지정인 테이블인 경우에는 변경 할수 없다.")
    void changEmptyGroupTable() {
        //given
        OrderTable orderTable = new OrderTable(1L, 1L, 3, false);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));

        //when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(1L, orderTable));

    }


    @Test
    @DisplayName("주문 상태가 조리중인 경우 변경할 수 없다.")
    void notChangeTableStatusIsMeal() {
        //given
        OrderTable beforeOrderTable = new OrderTable(1L, null, 3, false);
        OrderTable orderTable = new OrderTable(1L, null, 3, false);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(beforeOrderTable));
        //주문상태가 조리중 경우
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        //when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(1L, orderTable));
    }

    @Test
    @DisplayName("주문 테이블을 빈테이블로 변경한다.")
    void changeEmptyTable(){
        //givne
        OrderTable beforeOrderTable = new OrderTable(1L, null, 3, false);
        OrderTable orderTable = new OrderTable(1L, null, 3, true);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(beforeOrderTable));

        given(orderDao.existsByOrderTableIdAndOrderStatusIn(beforeOrderTable.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);

        given(orderTableDao.save(beforeOrderTable)).willReturn(orderTable);

        //when
        final OrderTable saveTable = tableService.changeEmpty(1L, orderTable);

        //then
        assertThat(saveTable.isEmpty()).isTrue();
    }


    @Test
    @DisplayName("방문자수가 음수 일 경우 변경할 수 없다.")
    void changeGuestNumberMinus() {
        //given


        //when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> {
                    OrderTable orderTable = new OrderTable(1L, 2L, -1, false);
                    tableService.changeNumberOfGuests(orderTable.getId(), orderTable);
                });
    }

    @Test
    @DisplayName("존재하지 않은 테이블인 경우에는 방문자를 변경 할수 없다.")
    void existTableChangeGuestNumber() {
        //given
        OrderTable orderTable = new OrderTable(1L, 2L, 1, false);
        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.empty());

        //when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable));
    }

    @Test
    @DisplayName("해당 주문테이블이 비어 있을경우 방문자를 변경 할 수 없다.")
    void emptyTableChangeGuestNumber() {
        //given
        OrderTable beforeOrderTable = new OrderTable(1L, 2L, 1, true);
        OrderTable orderTable = new OrderTable(1L, 2L, 1, false);
        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.of(beforeOrderTable));

        //when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable));
    }

    @Test
    @DisplayName("해당 주문테이블이 방문자수가 변경이 된다.")
    void changeNumberOfGuests() {
        //given
        OrderTable beforeOrderTable = new OrderTable(1L, 2L, 3, false);
        OrderTable orderTable = new OrderTable(1L, 2L, 1, false);

        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.of(beforeOrderTable));
        given(orderTableDao.save(beforeOrderTable)).willReturn(orderTable);

        //when
        final OrderTable changeOrderTable = tableService.changeNumberOfGuests(orderTable.getId(), orderTable);

        //then
        assertThat(changeOrderTable.getNumberOfGuests()).isEqualTo(1L);
    }


}
package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.graalvm.compiler.core.common.type.ArithmeticOpTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;

    private TableService tableService;
    @BeforeEach
    void setUp() {
        tableService = new TableService(orderDao, orderTableDao);
    }

    @Test
    public void 주문테이블생성_성공() {
        //given
        OrderTable orderTable = mock(OrderTable.class);

        //when
        tableService.create(orderTable);

        //then
        verify(orderTableDao).save(orderTable);
    }

    @Test
    public void 주문테이블조회_성공() {
        //given
        OrderTable orderTable = mock(OrderTable.class);
        given(orderTableDao.findAll()).willReturn(asList(orderTable));

        //when
        tableService.list();

        //then
        verify(orderTableDao).findAll();
    }

    @Test
    public void 주문테이블_상태변경_비우기_성공() {
        //given
        OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setId(1L);
        savedOrderTable.setTableGroupId(null);
        savedOrderTable.setNumberOfGuests(0);
        savedOrderTable.setEmpty(false);

        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(savedOrderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(false);

        //given(orderTableDao.save(any())).willAnswer(i -> i.getArgument(0));
        given(orderTableDao.save(any())).willReturn(savedOrderTable);

        //when
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(true);
        OrderTable resultOrderTable = tableService.changeEmpty(1L, orderTable);

        //then
        assertThat(resultOrderTable.isEmpty()).isEqualTo(true);
    }

    @Test
    public void 주문테이블_상태변경_비우기_예외_주문테이블번호잘못된경우() {
        //given
        OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setId(1L);
        savedOrderTable.setTableGroupId(1L);
        savedOrderTable.setNumberOfGuests(0);
        savedOrderTable.setEmpty(false);

        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(savedOrderTable));

        //when, then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTable())).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void 주문테이블_상태변경_비우기_예외_주문테이블이그룹에속해있는경우() {
        //given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTable())).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void 주문테이블_상태변경_비우기_예외_주문테이블이조리또는식사상태인경우() {
        //given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(new OrderTable()));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(1L, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        //when, then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, new OrderTable())).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void 주문테이블_손님숫자변경_성공() {
        //given
        OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setId(1L);
        savedOrderTable.setTableGroupId(null);
        savedOrderTable.setNumberOfGuests(3);
        savedOrderTable.setEmpty(false);

        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(savedOrderTable));
        given(orderTableDao.save(any())).willReturn(savedOrderTable);

        //when
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(2);
        OrderTable resultOrderTable = tableService.changeNumberOfGuests(1L, orderTable);

        //then
        assertThat(resultOrderTable.getNumberOfGuests()).isEqualTo(2);
    }

    @Test
    public void 주문테이블_손님숫자변경_예외_손님숫자가0보다작을때() {
        //given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(-1);

        //when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void 주문테이블_손님숫자변경_예외_주문테이블이없을때() {
        //given
        given(orderTableDao.findById(anyLong())).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, new OrderTable())).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void 주문테이블_손님숫자변경_예외_주문테이블이비어있을때() {
        //given
        OrderTable orderTable = mock(OrderTable.class);

        OrderTable savedOrderTable = new OrderTable();
        savedOrderTable.setEmpty(true);

        given(orderTable.getNumberOfGuests()).willReturn(2);
        given(orderTableDao.findById(anyLong())).willReturn(Optional.of(savedOrderTable));

        //when, then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, orderTable)).isInstanceOf(IllegalArgumentException.class);
    }
}
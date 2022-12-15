package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
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
    OrderDao orderDao;
    @Mock
    OrderTableDao orderTableDao;

    @InjectMocks
    TableService tableService;

    private OrderTable 주문테이블;

    @BeforeEach
    public void setUp() {
        주문테이블 = new OrderTable(1L, null, 3, false);
    }

    @DisplayName("주문 테이블을 생성할 수 있다.")
    @Test
    void create() {
        //given
        given(orderTableDao.save(any())).willReturn(주문테이블);

        //when
        OrderTable orderTable = tableService.create(주문테이블);

        //then
        assertAll(
                () -> assertThat(orderTable.getId()).isNotNull(),
                () -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(주문테이블.getNumberOfGuests()),
                () -> assertThat(orderTable.isEmpty()).isEqualTo(주문테이블.isEmpty())
        );
    }

    @DisplayName("주문 테이블 목록 조회 요청할 수 있다.")
    @Test
    void list() {
        //given
        given(orderTableDao.findAll()).willReturn(Collections.singletonList(주문테이블));
        //when
        List<OrderTable> list = tableService.list();
        //then
        assertThat(list).contains(주문테이블);

    }

    @DisplayName("주문 테이블에 빈 테이블 상태를 변경할 수 있다.")
    @Test
    void changeEmpty() {
        //given
        given(orderTableDao.findById(1L)).willReturn(Optional.ofNullable(주문테이블));
        OrderTable request = new OrderTable(null, null, 0, false);
        주문테이블.setEmpty(request.isEmpty());
        given(orderTableDao.save(any())).willReturn(주문테이블);
        //when
        OrderTable 변경상태태이블 = tableService.changeEmpty(1L, request);
        //then
        assertThat(변경상태태이블.isEmpty()).isEqualTo(request.isEmpty());
    }

    @DisplayName("등록된 주문 테이블만 변경 가능하다.")
    @Test
    void changeEmptyRegistered() {
        //given
        given(orderTableDao.findById(1L)).willReturn(Optional.empty());
        OrderTable request = new OrderTable(null, null, 0, false);
        //when & then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, request))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("주문 테이블이 테이블 그룹에 속해있으면 변경 불가하다.")
    @Test
    void changeEmptyTableGroup() {
        //given
        OrderTable orderTable = new OrderTable(2L, 1L, 0, false);
        given(orderTableDao.findById(2L)).willReturn(Optional.of(orderTable));
        OrderTable request = new OrderTable(null, null, 0, false);
        //when & then
        assertThatThrownBy(() -> tableService.changeEmpty(2L, request))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("주문 테이블의 주문 상태가 COMPLETION 이 아니면 에러가 발생한다.")
    @Test
    void name() {
        //given
        OrderTable request = new OrderTable(null, null, 0, true);
        given(orderTableDao.findById(1L)).willReturn(Optional.ofNullable(주문테이블));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).willReturn(true);
        //when & then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 방문한 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        //given
        OrderTable request = new OrderTable(null, null, 5, false);
        주문테이블.setNumberOfGuests(request.getNumberOfGuests());
        given(orderTableDao.findById(any())).willReturn(Optional.ofNullable(주문테이블));
        given(orderTableDao.save(any())).willReturn(주문테이블);
        //when
        OrderTable orderTable = tableService.changeNumberOfGuests(주문테이블.getId(), request);
        //then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());

    }

    @DisplayName("방문 손님을 변경할때 손님의 수는 0이상이어야 한다.")
    @Test
    void changeNumberOfGuestsZero() {
        //given
        OrderTable request = new OrderTable(null, null, -1, false);
        //when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문테이블.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되지 않은 주문테이블의 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsNotRegistered() {
        //given
        OrderTable request = new OrderTable(null, null, 5, false);
        given(orderTableDao.findById(any())).willReturn(Optional.empty());
        //when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문테이블.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블의 상태가 비어 있으면 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsEmpty() {
        //given
        주문테이블.setEmpty(true);
        OrderTable request = new OrderTable(null, null, 5, false);
        given(orderTableDao.findById(any())).willReturn(Optional.of(주문테이블));

        //when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문테이블.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

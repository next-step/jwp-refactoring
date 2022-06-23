package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.fixture.OrderTableFixture.*;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    private TableService tableService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderDao, orderTableDao);
    }

    @DisplayName("테이블을 생성한다.")
    @Test
    void create() {
        //given
        int numberOfGuests = 4;
        OrderTable request = 주문테이블_생성요청_데이터_생성(numberOfGuests);

        Long id = 1L;
        given(orderTableDao.save(any())).willReturn(주문테이블_데이터_생성(id, null, numberOfGuests, false));

        //when
        OrderTable orderTable = tableService.create(request);

        //then
        주문테이블_데이터_확인(orderTable, id, null, numberOfGuests, false);
    }

    @DisplayName("테이블 전체를 조회한다.")
    @Test
    void list() {
        //given
        Long id = 1L;
        Long tableGroupId = 1L;
        int numberOfGuests = 4;
        given(orderTableDao.findAll()).willReturn(Arrays.asList(주문테이블_데이터_생성(id, tableGroupId, numberOfGuests, false)));

        //when
        List<OrderTable> list = tableService.list();

        //then
        assertEquals(1, list.size());
        주문테이블_데이터_확인(list.get(0), id, tableGroupId, 4, false);
    }

    @DisplayName("빈테이블로 변경한다.")
    @Test
    void changeEmpty() {
        //given
        Long orderTableId = 1L;
        int numberOfGuest = 4;
        OrderTable request = 주문테이블_비우기_데이터_생성(true);

        given(orderTableDao.findById(orderTableId))
                .willReturn(Optional.of(주문테이블_데이터_생성(orderTableId, null, numberOfGuest, false)));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).willReturn(false);
        given(orderTableDao.save(any())).willReturn(주문테이블_데이터_생성(orderTableId, null, numberOfGuest, false));

        //when
        OrderTable orderTable = tableService.changeEmpty(orderTableId, request);

        //then
        주문테이블_데이터_확인(orderTable, orderTableId, null, numberOfGuest, false);
    }

    @DisplayName("테이블이 없으면 변경할 수 없다.")
    @Test
    void changeEmpty_fail_notExistsOrderTable() {
        //given
        Long orderTableId = 1L;
        OrderTable request = 주문테이블_비우기_데이터_생성(true);

        given(orderTableDao.findById(orderTableId)).willReturn(Optional.empty());

        //when //then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeEmpty(orderTableId, request));
    }

    @DisplayName("단체 지정된 테이블이면 변경할 수 없다.")
    @Test
    void changeEmpty_fail_tableGroupId() {
        Long orderTableId = 1L;
        OrderTable request = 주문테이블_비우기_데이터_생성(true);

        given(orderTableDao.findById(orderTableId))
                .willReturn(Optional.of(주문테이블_데이터_생성(orderTableId, 1L, 4, false)));

        //when //then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeEmpty(orderTableId, request));
    }

    @DisplayName("요리중, 식사중 주문이 있으면 변경할 수 없다.")
    @Test
    void changeEmpty_fail_invalidOrderStatus() {
        //given
        Long orderTableId = 1L;
        int numberOfGuest = 4;
        OrderTable request = 주문테이블_비우기_데이터_생성(true);

        given(orderTableDao.findById(orderTableId))
                .willReturn(Optional.of(주문테이블_데이터_생성(orderTableId, null, numberOfGuest, false)));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).willReturn(true);

        //when //then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeEmpty(orderTableId, request));
    }

    @DisplayName("테이블의 손님수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        //given
        Long orderTableId = 1L;
        int numberOfGuests = 4;
        OrderTable request = 주문테이블_손님수변경_데이터_생성(numberOfGuests);

        given(orderTableDao.findById(orderTableId))
                .willReturn(Optional.of(주문테이블_데이터_생성(orderTableId, null, 2, false)));
        given(orderTableDao.save(any()))
                .willReturn(주문테이블_데이터_생성(orderTableId, null, numberOfGuests, false));

        //when
        OrderTable orderTable = tableService.changeNumberOfGuests(orderTableId, request);

        //then
        주문테이블_데이터_확인(orderTable, orderTableId, null, numberOfGuests, false);
    }

    @DisplayName("변경할 손님수가 0보다 작으면 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_fail_negative() {
        //given
        Long orderTableId = 1L;
        OrderTable request = 주문테이블_손님수변경_데이터_생성(-1);

        //when //then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, request));
    }

    @DisplayName("등록된 주문 테이블이 아니면 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_fail_notExistsOrderTable() {
        //given
        Long orderTableId = 1L;
        OrderTable request = 주문테이블_손님수변경_데이터_생성(4);
        given(orderTableDao.findById(any())).willReturn(Optional.empty());

        //when //then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, request));
    }

    @DisplayName("주문테이블이 비어있으면 변경할 수 없다.")
    @Test
    void changeNumberOfGuests_fail_emptyOrderTable() {
        //given
        Long orderTableId = 1L;
        OrderTable request = 주문테이블_손님수변경_데이터_생성(4);
        given(orderTableDao.findById(any()))
                .willReturn(Optional.of(주문테이블_데이터_생성(orderTableId, null, 2, true)));

        //when //then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeNumberOfGuests(orderTableId, request));
    }

    private void 주문테이블_데이터_확인(OrderTable orderTable, Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        assertAll(
                () -> assertEquals(id, orderTable.getId()),
                () -> assertEquals(tableGroupId, orderTable.getTableGroupId()),
                () -> assertEquals(numberOfGuests, orderTable.getNumberOfGuests()),
                () -> assertEquals(empty, orderTable.isEmpty())
        );
    }
}
package kitchenpos.application;

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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @InjectMocks
    private TableService tableService;

    private OrderTable givenOrderTable = new OrderTable();

    @BeforeEach
    void setUp() {
        givenOrderTable.setId(1L);
        givenOrderTable.setEmpty(false);
        givenOrderTable.setNumberOfGuests(2);
    }

    @DisplayName("주문 테이블 생성")
    @Test
    void createTest() {
        //when
        tableService.create(givenOrderTable);

        //then
        verify(orderTableDao).save(givenOrderTable);
    }

    @DisplayName("주문 테이블 목록 조회")
    @Test
    void list() {
        //given
        given(orderTableDao.findAll()).willReturn(Arrays.asList(givenOrderTable));

        //when
        List<OrderTable> result = tableService.list();

        //then
        assertThat(result).containsExactly(givenOrderTable);
    }

    @DisplayName("등록되어있지 않은 주문 테이블은 빈 테이블 설정할 수 없다")
    @Test
    void changeEmptyFailBecauseOfNotExistOrderTableTest() {
        //given
        given(orderTableDao.findById(givenOrderTable.getId())).willReturn(Optional.empty());

        //when && then
        assertThatThrownBy(() -> tableService.changeEmpty(givenOrderTable.getId(), givenOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("등록되지 않은 주문 테이블입니다.");
    }

    @DisplayName("단체 지정된 테이블은 빈 테이블 설정 할 수 없다")
    @Test
    void changeEmptyFailBecauseOfHasTableGroupIdTest() {
        //given
        givenOrderTable.setTableGroupId(1L);
        given(orderTableDao.findById(givenOrderTable.getId())).willReturn(Optional.ofNullable(givenOrderTable));

        //when && then
        assertThatThrownBy(() -> tableService.changeEmpty(givenOrderTable.getId(), givenOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("단체 지정된 테이블입니다.");
    }

    @DisplayName("주문 상태가 `조리`, `식사` 이면 빈 테이블 설정 할 수 없다")
    @Test
    void changeEmptyFailBecauseOfOrderStatusTest() {
        //given
        given(orderTableDao.findById(givenOrderTable.getId())).willReturn(Optional.ofNullable(givenOrderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(),any())).willReturn(true);

        //when && then
        assertThatThrownBy(() -> tableService.changeEmpty(givenOrderTable.getId(), givenOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문이 조리나 식사 상태입니다.");
    }

    @DisplayName("빈 테이블 설정")
    @Test
    void changeEmptyTest() {
        //given
        given(orderTableDao.findById(givenOrderTable.getId())).willReturn(Optional.ofNullable(givenOrderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(),any())).willReturn(false);

        //when
        tableService.changeEmpty(givenOrderTable.getId(), givenOrderTable);

        //then
        verify(orderTableDao).save(givenOrderTable);
    }

    @DisplayName("방문한 고객 수를 마이너스로 변경 할 수 없다")
    @Test
    void changeNumberOfGuestsFailBecauseOfMinusGuestNumberTest() {
        //given
        givenOrderTable.setNumberOfGuests(-1);

        //when && then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(givenOrderTable.getId(), givenOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("방문한 고객 수는 0명 이상이어야 합니다.");
    }


    @DisplayName("주문 테이블이 존재해야 합니다")
    @Test
    void changeNumberOfGuestsFailBecauseOfNotExistOrderTableTest() {
        //given
        given(orderTableDao.findById(givenOrderTable.getId())).willReturn(Optional.empty());

        //when && then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(givenOrderTable.getId(), givenOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("등록되지 않은 주문 테이블입니다.");
    }

    @DisplayName("빈 주문 테이블이어선 안됩니다.")
    @Test
    void changeNumberOfGuestsFailBecauseOfOrderTableIsEmptyTest() {
        //given
        givenOrderTable.setEmpty(true);
        given(orderTableDao.findById(givenOrderTable.getId())).willReturn(Optional.ofNullable(givenOrderTable));

        //when && then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(givenOrderTable.getId(), givenOrderTable))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("빈 주문 테이블입니다.");
    }

    @DisplayName("방문 고객 수 변경")
    @Test
    void changeNumberOfGuestsTest() {
        //given
        given(orderTableDao.findById(givenOrderTable.getId())).willReturn(Optional.ofNullable(givenOrderTable));

        //when
        tableService.changeNumberOfGuests(givenOrderTable.getId(), givenOrderTable);

        //then
        verify(orderTableDao).save(givenOrderTable);
    }
}
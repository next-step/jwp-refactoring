package kitchenpos.table.application;

import kitchenpos.common.Exception.AlreadyGroupedException;
import kitchenpos.common.Exception.NotExistException;
import kitchenpos.common.Exception.UnchangeableException;
import kitchenpos.order.application.OrderService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderTableService orderTableService;

    private OrderTableRequest orderTableRequest;
    private OrderTableRequest changeEmptyRequest;
    private OrderTableRequest changeNumberOfGuestsRequest;
    private OrderTable givenOrderTable;

    @BeforeEach
    public void setUp() {
        orderTableRequest = new OrderTableRequest(1, true);
        changeEmptyRequest = new OrderTableRequest(1, false);
        changeNumberOfGuestsRequest = new OrderTableRequest(2, false);
        givenOrderTable = new OrderTable(1L, null, 0, true);
    }

    @DisplayName("주문테이블을 등록할 수 있다.")
    @Test
    void createTest() {
        //given
        given(orderTableRepository.save(any())).willReturn(orderTableRequest.toEntity());

        //when
        OrderTableResponse orderTableResponse = orderTableService.create(orderTableRequest);

        //then
        assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(orderTableRequest.getNumberOfGuests());
        assertThat(orderTableResponse.isEmpty()).isEqualTo(orderTableRequest.isEmpty());
    }

    @DisplayName("주문테이블을 조회할 수 있다.")
    @Test
    void listTest() {
        //given
        given(orderTableRepository.findAll()).willReturn(Arrays.asList(orderTableRequest.toEntity()));

        //when
        List<OrderTableResponse> orderTableResponses = orderTableService.list();

        //then
        assertThat(orderTableResponses.size()).isGreaterThan(0);
    }


    @DisplayName("빈 테이블 설정을 할 때 등록되어있지 않은 주문 테이블은 빈 테이블 설정할 수 없다.")
    @Test
    void changeEmptyFailBecauseOfNotExistOrderTableTest() {
        //given
        given(orderTableRepository.findById(any())).willReturn(Optional.empty());

        //when && then
        assertThatThrownBy(() -> orderTableService.changeEmpty(givenOrderTable.getId(), changeEmptyRequest))
                .isInstanceOf(NotExistException.class)
                .hasMessageContaining("등록되지 않은 주문 테이블입니다.");
    }

    @DisplayName("빈 테이블 설정을 할 때 단체 지정된 테이블은 빈 테이블 설정 할 수 없다.")
    @Test
    void changeEmptyFailBecauseOfHasTableGroupIdTest() {
        //given
        TableGroup tableGroup = new TableGroup(1l, LocalDateTime.now());
        givenOrderTable.updateTableGroup(1l);
        given(orderTableRepository.findById(givenOrderTable.getId())).willReturn(Optional.ofNullable(givenOrderTable));

        //when && then
        assertThatThrownBy(() -> orderTableService.changeEmpty(givenOrderTable.getId(), changeEmptyRequest))
                .isInstanceOf(AlreadyGroupedException.class)
                .hasMessageContaining("단체 지정된 주문 테이블입니다.");
    }

    @DisplayName("빈 테이블 설정을 할 때 주문 상태가 `조리`, `식사` 이면 빈 테이블 설정 할 수 없다.")
    @Test
    void changeEmptyFailBecauseOfOrderStatusTest() {
        //given
        given(orderTableRepository.findById(givenOrderTable.getId())).willReturn(Optional.ofNullable(givenOrderTable));
        doThrow(new UnchangeableException("주문이 조리나 식사 상태에서는 변경할 수 없습니다.")).when(orderService).changeStatusValidCheck(any());

        //when && then
        assertThatThrownBy(() -> orderTableService.changeEmpty(givenOrderTable.getId(), changeEmptyRequest))
                .isInstanceOf(UnchangeableException.class)
                .hasMessageContaining("주문이 조리나 식사 상태에서는 변경할 수 없습니다.");
    }

    @DisplayName("빈 테이블 설정을 할 수 있다.")
    @Test
    void changeEmptyTest() {
        //given
        given(orderTableRepository.findById(givenOrderTable.getId())).willReturn(Optional.ofNullable(givenOrderTable));

        //when
        OrderTableResponse orderTableResponse = orderTableService.changeEmpty(givenOrderTable.getId(), changeEmptyRequest);

        //then
        assertThat(orderTableResponse.isEmpty()).isEqualTo(changeEmptyRequest.isEmpty());
    }


    @DisplayName("방문 고객 수를 변경할 때 방문한 고객 수를 마이너스로 변경 할 수 없다.")
    @Test
    void changeNumberOfGuestsFailBecauseOfMinusGuestNumberTest() {
        //given
        OrderTableRequest orderTableRequest =new OrderTableRequest(-1, false);
        given(orderTableRepository.findById(givenOrderTable.getId())).willReturn(Optional.ofNullable(givenOrderTable));

        //when && then
        assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(givenOrderTable.getId(), orderTableRequest))
                .isInstanceOf(UnchangeableException.class)
                .hasMessageContaining("방문 고객 수는 0명 이상이어야 합니다.");
    }


    @DisplayName("방문 고객 수를 변경할 때 주문 테이블이 존재해야 한다.")
    @Test
    void changeNumberOfGuestsFailBecauseOfNotExistOrderTableTest() {
        //given
        given(orderTableRepository.findById(givenOrderTable.getId())).willReturn(Optional.empty());

        //when && then
        assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(givenOrderTable.getId(), changeNumberOfGuestsRequest))
                .isInstanceOf(NotExistException.class)
                .hasMessageContaining("등록되지 않은 주문 테이블입니다.");
    }

    @DisplayName("방문 고객 수를 변경할 때 빈 주문 테이블이어선 안됩니다.")
    @Test
    void changeNumberOfGuestsFailBecauseOfOrderTableIsEmptyTest() {
        //given
        OrderTableRequest orderTableRequest =new OrderTableRequest(1, true);
        given(orderTableRepository.findById(givenOrderTable.getId())).willReturn(Optional.ofNullable(givenOrderTable));

        //when && then
        assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(givenOrderTable.getId(), orderTableRequest))
                .isInstanceOf(UnchangeableException.class)
                .hasMessageContaining("빈 주문 테이블입니다.");
    }

    @DisplayName("방문 고객 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuestsTest() {
        //given
        givenOrderTable.changeEmpty(false);
        given(orderTableRepository.findById(givenOrderTable.getId())).willReturn(Optional.ofNullable(givenOrderTable));

        //when
        OrderTableResponse orderTableResponse = orderTableService.changeNumberOfGuests(givenOrderTable.getId(), changeNumberOfGuestsRequest);

        //then
        assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(changeNumberOfGuestsRequest.getNumberOfGuests());
    }


}
package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableFactory;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupFactory;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
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
    OrderRepository orderRepository;
    @Mock
    OrderTableRepository orderTableRepository;

    @InjectMocks
    TableService tableService;

    private OrderTable 주문테이블;

    @BeforeEach
    public void setUp() {
        주문테이블 = OrderTableFactory.create(1L, null, 3, false);
    }

    @DisplayName("주문 테이블을 생성할 수 있다.")
    @Test
    void create() {
        //given
        OrderTableRequest orderTableRequest = new OrderTableRequest(10, false);
        given(orderTableRepository.save(any())).willReturn(주문테이블);

        //when
        OrderTableResponse orderTable = tableService.create(orderTableRequest);

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
        given(orderTableRepository.findAll()).willReturn(Collections.singletonList(주문테이블));
        //when
        List<OrderTableResponse> list = tableService.list();
        //then
        assertThat(list.size()).isEqualTo(1);

    }

    @DisplayName("주문 테이블에 빈 테이블 상태를 변경할 수 있다.")
    @Test
    void changeEmpty() {
        //given
        given(orderTableRepository.findById(1L)).willReturn(Optional.ofNullable(주문테이블));
        OrderTableRequest request = new OrderTableRequest(0, true);
        //when
        OrderTableResponse 변경상태태이블 = tableService.changeEmpty(1L, request);
        //then
        assertThat(변경상태태이블.isEmpty()).isTrue();
    }

    @DisplayName("등록된 주문 테이블만 변경 가능하다.")
    @Test
    void changeEmptyRegistered() {
        //given
        given(orderTableRepository.findById(1L)).willReturn(Optional.empty());
        OrderTableRequest request = new OrderTableRequest(0, true);

        //when & then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, request))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("주문 테이블이 테이블 그룹에 속해있으면 변경 불가하다.")
    @Test
    void changeEmptyTableGroup() {
        //given
        OrderTable 빈테이블일번 = OrderTableFactory.create(1L, null, 0, true);
        OrderTable 빈테이블이번 = OrderTableFactory.create(2L, null, 0, true);
        TableGroupFactory.create(1L, Arrays.asList(빈테이블일번, 빈테이블이번));

        given(orderTableRepository.findById(1L)).willReturn(Optional.of(빈테이블일번));
        OrderTableRequest request = new OrderTableRequest(0, true);
        //when & then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, request))
                .isInstanceOf(IllegalArgumentException.class);

    }


    @DisplayName("주문 테이블의 방문한 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        //given
        OrderTableRequest request = new OrderTableRequest(10, null);
        given(orderTableRepository.findById(1L)).willReturn(Optional.ofNullable(주문테이블));
        //when
        OrderTableResponse orderTableResponse = tableService.changeNumberOfGuests(주문테이블.getId(), request);
        //then
        assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(10);

    }

    @DisplayName("방문 손님을 변경할때 손님의 수는 0이상이어야 한다.")
    @Test
    void changeNumberOfGuestsZero() {
        //given
        OrderTableRequest request = new OrderTableRequest(-1, null);
        given(orderTableRepository.findById(1L)).willReturn(Optional.ofNullable(주문테이블));
        //when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문테이블.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("등록되지 않은 주문테이블의 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsNotRegistered() {
        //given
        OrderTableRequest request = new OrderTableRequest(10, null);
        given(orderTableRepository.findById(any())).willReturn(Optional.empty());
        //when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문테이블.getId(), request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

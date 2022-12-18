package kitchenpos.application;

import kitchenpos.order.domain.Order;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.ordertable.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.fixture.OrderLineItemTestFixture.주문정보;
import static kitchenpos.fixture.OrderTableTestFixture.*;
import static kitchenpos.fixture.TableGroupTestFixture.테이블그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("주문 테이블 관련 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class OrderTableServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    private OrderTableRequest 주문테이블1_요청;
    private OrderTableRequest 주문테이블2_요청;
    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;

    @BeforeEach
    void setUp() {
        주문테이블1_요청 = 주문테이블1_요청();
        주문테이블2_요청 = 주문테이블1_요청();
        주문테이블1 = 그룹_없는_주문테이블_생성(주문테이블1_요청);
        주문테이블2 = 그룹_없는_주문테이블_생성(주문테이블2_요청);
    }

    @DisplayName("주문 테이블 생성 작업을 성공한다.")
    @Test
    void create() {
        // given
        when(orderTableRepository.save(any())).thenReturn(주문테이블1);

        // when
        OrderTableResponse orderTable = tableService.create(주문테이블1_요청);

        // then
        assertThat(orderTable).isNotNull();
    }

    @DisplayName("주문 테이블 전체 목록 조회 작업을 성공한다.")
    @Test
    void list() {
        // given
        List<OrderTable> expected = Arrays.asList(주문테이블1, 주문테이블2);
        when(orderTableRepository.findAll()).thenReturn(expected);

        // when
        List<OrderTableResponse> actual = tableService.list();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(expected.size()),
                () -> assertThat(actual).containsExactly(OrderTableResponse.from(주문테이블1), OrderTableResponse.from(주문테이블2))
        );
    }

    @DisplayName("주문 테이블 빈좌석 여부에 대한 변경 작업을 성공한다.")
    @Test
    void changeEmpty() {
        // given
        주문테이블1.unGroup();
        주문테이블1.changeEmpty(true);
        when(orderTableRepository.findById(주문테이블1.getId())).thenReturn(Optional.of(주문테이블1));

        // when
        OrderTableResponse orderTable = tableService.changeEmpty(주문테이블1.getId(), 주문테이블1);

        // then
        assertThat(orderTable.isEmpty()).isEqualTo(주문테이블1.isEmpty());
    }

    @DisplayName("테이블의 빈좌석 상태를 변경할때, 테이블이 단체 지정이 되어 있으면 IllegalArgumentException을 반환한다.")
    @Test
    void changeEmptyWithException1() {
        // given
        OrderTable orderTable = 그룹_있는_주문테이블_생성(주문테이블(null, 1L, 10, true));
        setMenuGroup(테이블그룹(), orderTable);
        when(orderTableRepository.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableService.changeEmpty(orderTable.getId(), orderTable));
    }

    @DisplayName("테이블의 빈좌석 상태를 변경할때, 테이블이 주문 상태가 조리중이거나 식사중이면 IllegalArgumentException을 반환한다.")
    @Test
    void changeEmptyWithException2() {
        // given
        Order.of(주문테이블1, Collections.singletonList(주문정보(1L, 1)));
        when(orderTableRepository.findById(주문테이블1.getId())).thenReturn(Optional.of(주문테이블1));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableService.changeEmpty(주문테이블1.getId(), 주문테이블1));
    }

    @DisplayName("주문 테이블 고객 인원 수에 대한 변경 작업을 성공한다.")
    @Test
    void changeNumberOfGuestsInTable() {
        // given
        주문테이블1.changeNumberOfGuests(10);
        when(orderTableRepository.findById(주문테이블1.getId())).thenReturn(Optional.of(주문테이블1));
        when(orderTableRepository.save(주문테이블1)).thenReturn(주문테이블1);

        // when
        OrderTableResponse orderTable = tableService.changeNumberOfGuests(주문테이블1.getId(), 주문테이블1);

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
    }

    @DisplayName("고객 인원을 변경할 때, 인원 수가 0보다 작으면 IllegalArgumentException을 반환한다.")
    @Test
    void changeNumberOfGuestsWithException1() {
        // given
        OrderTable orderTable = 그룹_없는_주문테이블_생성(주문테이블(null, null, -1, false));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable));
    }


    @DisplayName("고객 인원을 변경할 때, 주문 테이블이 비어있으면 IllegalArgumentException을 반환한다.")
    @Test
    void changeNumberOfGuestsWithException2() {
        // given
        OrderTable orderTable = 그룹_없는_주문테이블_생성(주문테이블(null, 1L, 10, true));
        when(orderTableRepository.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable));
    }
}

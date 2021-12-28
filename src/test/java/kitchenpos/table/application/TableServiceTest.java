package kitchenpos.table.application;

import kitchenpos.order.domain.OrderTables;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
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
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    OrderTableRepository orderTableRepository;

    @InjectMocks
    TableService tableService;

    OrderTableRequest 빈_테이블_요청;
    OrderTableRequest 주문_테이블_요청;

    @BeforeEach
    void setUp() {
        빈_테이블_요청 = OrderTableRequest.of(0, true);
        주문_테이블_요청 = OrderTableRequest.of(5, false);

    }

    @Test
    void 빈_테이블_생성() {
        // given
        OrderTable 빈_테이블 = OrderTable.of(빈_테이블_요청.getNumberOfGuests(), 빈_테이블_요청.isEmpty());
        given(orderTableRepository.save(any())).willReturn(빈_테이블);

        // when
        OrderTableResponse actual = tableService.create(빈_테이블_요청);

        // then
        assertAll(() -> {
            assertThat(actual.getNumberOfGuests()).isZero();
            assertThat(actual.getTableGroupId()).isNull();
        });
    }

    @Test
    void 주문_테이블_조회() {
        // given
        OrderTable 빈_테이블 = OrderTable.of(빈_테이블_요청.getNumberOfGuests(), 빈_테이블_요청.isEmpty());
        OrderTable 주문_테이블 = OrderTable.of(주문_테이블_요청.getNumberOfGuests(), 주문_테이블_요청.isEmpty());

        List<OrderTable> orderTables = Arrays.asList(빈_테이블, 주문_테이블);
        given(orderTableRepository.findAll()).willReturn(orderTables);

        // when
        List<OrderTableResponse> actual = tableService.list();

        // then
        assertAll(() -> {
            assertThat(actual).hasSize(2);
            assertThat(actual).extracting("numberOfGuests")
                    .containsExactly(0, 5);
        });
    }

    @DisplayName("빈 테이블을 주문 테이블로 변경한다.")
    @Test
    void 주문_테이블_상태_변경() {
        // given
        OrderTable 빈_테이블 = OrderTable.of(빈_테이블_요청.getNumberOfGuests(), 빈_테이블_요청.isEmpty());
        OrderTableRequest 주문_테이블_변경_요청 = OrderTableRequest.of(빈_테이블.getNumberOfGuests(), false);
        OrderTable 변경된_빈_테이블 = OrderTable.of(빈_테이블.getNumberOfGuests(), false);

        given(orderTableRepository.findById(빈_테이블.getId())).willReturn(Optional.of(빈_테이블));

        // when
        OrderTableResponse actual = tableService.changeEmpty(빈_테이블.getId(), 주문_테이블_변경_요청);

        // then
        assertThat(actual.isEmpty()).isEqualTo(변경된_빈_테이블.isEmpty());
    }

    @Test
    void 주문_테이블_상태_변경_시_테이블은_반드시_존재해야한다() {
        // given
        given(orderTableRepository.findById(any())).willReturn(Optional.empty());

        // when
        ThrowingCallable throwingCallable = () -> tableService.changeEmpty(any(), 빈_테이블_요청);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }


    @Test
    void 단체_지정_된_주문_테이블은_상태를_변경할_수_없다() {
        // given
        OrderTableRequest 단체_지정_테이블_요청 = OrderTableRequest.of(5, true);
        OrderTable 첫번째_단체_지정_테이블 = OrderTable.of(단체_지정_테이블_요청.getNumberOfGuests(), 단체_지정_테이블_요청.isEmpty());
        OrderTable 두번째_단체_지정_테이블 = OrderTable.of(단체_지정_테이블_요청.getNumberOfGuests(), 단체_지정_테이블_요청.isEmpty());
        TableGroup.from(OrderTables.from(Arrays.asList(첫번째_단체_지정_테이블, 두번째_단체_지정_테이블)));
        given(orderTableRepository.findById(첫번째_단체_지정_테이블.getId())).willReturn(Optional.of(첫번째_단체_지정_테이블));

        // when
        ThrowingCallable throwingCallable = () -> tableService.changeEmpty(첫번째_단체_지정_테이블.getId(), 단체_지정_테이블_요청);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable)
                .withMessage("단체 지정 된 테이블은 상태를 변경할 수 없습니다.");
    }

    @Test
    void 방문한_손님_수_변경() {
        // given
        OrderTable 주문_테이블 = OrderTable.of(주문_테이블_요청.getNumberOfGuests(), 주문_테이블_요청.isEmpty());
        OrderTableRequest 방문한_손님_수_변경_요청 = OrderTableRequest.of(10, 주문_테이블_요청.isEmpty());
        OrderTable 방문자_수_변경된_주문_테이블 = OrderTable.of(방문한_손님_수_변경_요청.getNumberOfGuests(), 방문한_손님_수_변경_요청.isEmpty());

        given(orderTableRepository.findById(주문_테이블.getId())).willReturn(Optional.of(주문_테이블));

        // when
        OrderTableResponse actual = tableService.changeNumberOfGuests(주문_테이블.getId(), 방문한_손님_수_변경_요청);

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(방문자_수_변경된_주문_테이블.getNumberOfGuests());
    }

    @Test
    void 방문한_손님_수_변경_시_빈_테이블이면_변경할_수_없다() {
        // given
        OrderTable 빈_테이블 = OrderTable.of(빈_테이블_요청.getNumberOfGuests(), 빈_테이블_요청.isEmpty());
        given(orderTableRepository.findById(빈_테이블.getId())).willReturn(Optional.of(빈_테이블));

        // when
        ThrowingCallable throwingCallable = () -> tableService.changeNumberOfGuests(빈_테이블.getId(), 빈_테이블_요청);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable);
    }
}

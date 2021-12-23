package kitchenpos.table.application;

import com.google.common.collect.Lists;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.OrderTableSaveRequest;
import kitchenpos.menu.exception.IllegalQuantityException;
import kitchenpos.table.exception.OrderTableNotFoundException;
import kitchenpos.table.exception.TableEmptyUpdateException;
import kitchenpos.table.exception.TableGuestNumberUpdateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static kitchenpos.table.fixtures.OrderTableFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

/**
 * packageName : kitchenpos.application
 * fileName : TableServiceTest
 * author : haedoang
 * date : 2021/12/17
 * description :
 */
@DisplayName("테이블 통합 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    private final OrderTable 주문가능_다섯명테이블 = 주문가능_다섯명테이블();
    private final OrderTable 주문불가_다섯명테이블 = 주문불가_다섯명테이블();
    private final OrderTableSaveRequest request = 주문가능_다섯명테이블요청();

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    @Test
    @DisplayName("테이블 목록을 조회할 수 있다.")
    public void list() throws Exception {
        // given
        given(orderTableRepository.findAllJoinFetch()).willReturn(Lists.newArrayList(주문가능_다섯명테이블));

        // when
        List<OrderTableResponse> lists = tableService.list();

        // then
        assertThat(lists).hasSize(1);
    }

    @Test
    @DisplayName("테이블을 등록할 수 있다.")
    public void createTable() {
        // when
        given(orderTableRepository.save(any(OrderTable.class))).willReturn(주문가능_다섯명테이블);
        OrderTableResponse actual = tableService.create(request);

        // then
        assertAll(
                () -> assertThat(actual.getNumberOfGuests()).isEqualTo(request.getNumberOfGuest()),
                () -> assertThat(actual.getTableGroupId()).isNull()
        );
    }

    @Test
    @DisplayName("테이블의 상태를 변경할 수 있다.")
    public void changeTableStatus() {
        // given
        given(orderTableRepository.findOneWithOrderByIdJoinFetch(any(Long.class))).willReturn(Optional.of(주문가능_다섯명테이블));

        // when
        OrderTableResponse actual = tableService.changeEmpty(1L, 주문불가로_변경요청());

        // then
        assertThat(actual.isEmpty()).isTrue();

        // when
        OrderTableResponse actual2 = tableService.changeEmpty(1L, 주문가능으로_변경요청());

        // then
        assertThat(actual2.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("존재하지 않은 테이블은 상태를 변경할 수 없다.")
    public void changeTableStatusFailByUnknownTable() {
        // then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, 주문불가로_변경요청()))
                .isInstanceOf(OrderTableNotFoundException.class);
    }

    @Test
    @DisplayName("테이블이 그룹 테이블인 경우 상태를 변경할 수 없다.")
    public void changeTableStatusFailByGroupTable() {
        // given
        given(orderTableRepository.findOneWithOrderByIdJoinFetch(any(Long.class))).willReturn(Optional.of(그룹화된_테이블()));

        // then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, 주문가능으로_변경요청())).isInstanceOf(TableEmptyUpdateException.class);
    }

    @Test
    @DisplayName("테이블의 주문 상태가 조리, 식사인 경우 변경할 수 없다.")
    public void changeTableStatusFailByTableOrderStatus() throws Exception {
        // given
        given(orderTableRepository.findOneWithOrderByIdJoinFetch(any(Long.class))).willReturn(Optional.of(주문이_완료되지_않은_테이블()));

        // then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, 주문가능으로_변경요청())).isInstanceOf(TableEmptyUpdateException.class);
    }

    @Test
    @DisplayName("테이블의 사용자 수를 변경할 수 있다.")
    public void changeNumberOfGuests() throws Exception {
        // given
        given(orderTableRepository.findById(any(Long.class))).willReturn(Optional.of(주문가능_다섯명테이블));

        // when
        OrderTableResponse actual = tableService.changeNumberOfGuests(1L, 두명으로_변경요청());

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(2);

        // when
        OrderTableResponse actua2 = tableService.changeNumberOfGuests(1L, 다섯명으로_변경요청());

        // then
        assertThat(actua2.getNumberOfGuests()).isEqualTo(5);
    }

    @DisplayName("테이블 사용자 수가 올바르지 않을 경우 테이블을 등록할 수 없다.")
    @ParameterizedTest(name = "테이블 사용자 수는 0명 이상이어야 한다: " + ParameterizedTest.ARGUMENTS_PLACEHOLDER)
    @ValueSource(ints = {Integer.MIN_VALUE, -10, -5, -1})
    public void changeNumberOfGuestsByInvalidNumber(int candidate) {
        //given
        given(orderTableRepository.findById(any(Long.class))).willReturn(Optional.of(주문가능_다섯명테이블));

        //then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, 사용자수_변경요청(candidate))).isInstanceOf(IllegalQuantityException.class);
    }

    @Test
    @DisplayName("존재하지 않은 테이블의 사용자 수를 변경할 수 없다.")
    public void changeNumberOfGuestsByUnknownTable() throws Exception {
        // given
        given(orderTableRepository.findById(any(Long.class))).willThrow(OrderTableNotFoundException.class);

        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, 다섯명으로_변경요청())).isInstanceOf(OrderTableNotFoundException.class);
    }

    @Test
    @DisplayName("빈 테이블의 사용자 수를 변경할 수 없다.")
    public void changeNumberOfGuestsByEmptyTable() throws Exception {
        // given
        given(orderTableRepository.findById(any(Long.class))).willReturn(Optional.of(주문불가_다섯명테이블));

        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, 다섯명으로_변경요청())).isInstanceOf(TableGuestNumberUpdateException.class);
    }
}



package kitchenpos.table.application;

import kitchenpos.order.domain.*;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableValidator;
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
import java.util.NoSuchElementException;
import java.util.Optional;

import static kitchenpos.testfixture.CommonTestFixture.*;
import static kitchenpos.testfixture.CommonTestFixture.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableValidator tableValidator;
    @InjectMocks
    private TableService tableService;

    private OrderTableRequest 주문_테이블_요청;

    private OrderTable 주문_테이블;
    private OrderTable 빈_테이블;

    @BeforeEach
    void setUp() {
        주문_테이블_요청 = createOrderTableRequest(4, false);
        주문_테이블 = createOrderTable(1L, null, 4, false);
        빈_테이블 = createOrderTable(2L, null, 0, true);
    }

    @DisplayName("테이블을 등록한다.")
    @Test
    void create_success() {
        // given
        given(orderTableRepository.save(any())).willReturn(주문_테이블);

        // when
        OrderTableResponse saved = tableService.create(주문_테이블_요청);

        // then
        assertAll(
                () -> assertThat(saved).isNotNull(),
                () -> assertThat(saved.getId()).isEqualTo(주문_테이블.getId()),
                () -> assertThat(saved.getNumberOfGuests()).isEqualTo(주문_테이블.getNumberOfGuests())
        );
    }

    @DisplayName("테이블의 목록을 조회한다.")
    @Test
    void list() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
                createOrderTable(1L, null, 4, false),
                createOrderTable(2L, null, 0, true),
                createOrderTable(3L, null, 0, false)
        );
        given(orderTableRepository.findAll()).willReturn(orderTables);

        // when
        List<OrderTableResponse> list = tableService.list();

        // then
        assertThat(list).hasSize(3);
    }

    @DisplayName("빈 테이블로 변경한다")
    @Test
    void changeEmpty_success() {
        // given
        when(orderTableRepository.findById(주문_테이블.getId())).thenReturn(Optional.of(주문_테이블));
        when(orderTableRepository.save(주문_테이블)).thenReturn(주문_테이블);

        // when
        tableService.changeEmpty(주문_테이블.getId());

        // then
        assertThat(주문_테이블.isEmpty()).isTrue();
    }

    @DisplayName("빈 테이블로 변경에 실패한다. (테이블이 존재하지 않는 경우)")
    @Test
    void changeEmpty_fail_emptyTable() {
        // then
        assertThatThrownBy(() -> {
            tableService.changeEmpty(주문_테이블.getId());
        }).isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("빈 테이블로 변경에 실패한다. (단체 지정된 테이블인 경우)")
    @Test
    void changeEmpty_fail_tableGroup() {
        // given
        List<OrderTable> orderTables = Arrays.asList(
                createOrderTable(1L, null, 4, true),
                createOrderTable(2L, null, 5, true)
        );
        OrderTable 단체지정_테이블 = createOrderTable(1L, new TableGroup(orderTables), 4, false);
        when(orderTableRepository.findById(단체지정_테이블.getId())).thenReturn(Optional.of(단체지정_테이블));

        // then
        assertThatThrownBy(() -> {
            tableService.changeEmpty(단체지정_테이블.getId());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블로 변경에 실패한다. (테이블의 주문상태가 '조리' 또는 '식사' 인 경우)")
    @Test
    void changeEmpty_fail_orderStatus() {
        // given
        when(orderTableRepository.findById(주문_테이블.getId())).thenReturn(Optional.of(주문_테이블));
        willThrow(new IllegalArgumentException()).given(tableValidator).validateOrderStatus(주문_테이블.getId());

        // then
        assertThatThrownBy(() -> {
            tableService.changeEmpty(주문_테이블.getId());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 방문객 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        //given
        OrderTableRequest 변경_테이블_요청 = createOrderTableRequest(5, false);
        when(orderTableRepository.findById(주문_테이블.getId())).thenReturn(Optional.of(주문_테이블));
        when(orderTableRepository.save(주문_테이블)).thenReturn(주문_테이블);

        //when
        tableService.changeNumberOfGuests(주문_테이블.getId(), 변경_테이블_요청);

        //then
        assertThat(주문_테이블.getNumberOfGuests()).isEqualTo(변경_테이블_요청.getNumberOfGuests());
    }

    @DisplayName("테이블의 방문객 수를 변경에 실패한다. (변경할 방문객 수가 0명 미만인 경우)")
    @Test
    void changeNumberOfGuests_fail_guestZero() {
        // given
        willThrow(new IllegalArgumentException()).given(tableValidator).validateNumberOfGuests(-1);

        // then
        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(주문_테이블.getId(), createOrderTableRequest(-1, false));
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 방문객 수를 변경에 실패한다. (테이블이 존재하지 않는 경우)")
    @Test
    void changeNumberOfGuests_fail_empty() {
        // then
        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(Long.MAX_VALUE, 주문_테이블_요청);
        }).isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("테이블의 방문객 수를 변경에 실패한다. (빈 테이블의 방문객 수를 변경하려는 경우)")
    @Test
    void changeNumberOfGuests_fail_emptyTable() {
        // given
        when(orderTableRepository.findById(빈_테이블.getId())).thenReturn(Optional.of(빈_테이블));

        // then
        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(빈_테이블.getId(), 주문_테이블_요청);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}

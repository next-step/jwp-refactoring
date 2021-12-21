package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.application.fixture.TableFixture;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("테이블 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    OrderTable 빈_테이블;
    OrderTable 단체_테이블1;
    OrderTable 단체_테이블2;

    @BeforeEach
    void setUp() {
        빈_테이블 = TableFixture.create(1L, null, 0, true);
        단체_테이블1 = TableFixture.create(2L, 3, false);
        단체_테이블2 = TableFixture.create(3L, 4, false);
    }

    @DisplayName("테이블을 등록한다.")
    @Test
    void create() {
        given(orderTableRepository.save(빈_테이블)).willReturn(빈_테이블);

        OrderTable savedTable = tableService.create(빈_테이블);

        assertThat(savedTable).isEqualTo(빈_테이블);
        assertThat(savedTable.getTableGroup()).isNull();
    }

    @DisplayName("테이블 목록을 조회한다.")
    @Test
    void list() {
        given(orderTableRepository.findAll()).willReturn(Arrays.asList(빈_테이블, 단체_테이블1, 단체_테이블2));

        List<OrderTable> tables = tableService.list();

        assertThat(tables).containsExactly(빈_테이블, 단체_테이블1, 단체_테이블2);
    }

    @DisplayName("테이블을 빈 테이블 상태로 변경한다.")
    @Test
    void changeEmpty() {
        OrderTable 변경할_테이블 = TableFixture.create(1L, null, 0, true);

        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(false);
        given(orderTableRepository.findById(변경할_테이블.getId())).willReturn(Optional.of(변경할_테이블));
        given(orderTableRepository.save(변경할_테이블)).willReturn(변경할_테이블);

        OrderTable changedTable = tableService.changeEmpty(변경할_테이블.getId(), 변경할_테이블);

        assertThat(changedTable.isEmpty()).isTrue();
    }

    @DisplayName("등록되지 않은 테이블을 빈 테이블 상태로 변경하려고 하면 예외가 발생한다.")
    @Test
    void changeEmptyImpossible1() {
        OrderTable 등록안된_테이블 = TableFixture.create(1L, null, 2, false);
        given(orderTableRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeEmpty(1L, 등록안된_테이블))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체로 지정된 테이블을 빈 테이블 상태로 변경하려고 하면 예외가 발생한다.")
    @Test
    void changeEmptyImpossible2() {
        assertThatThrownBy(() -> tableService.changeEmpty(2L, 단체_테이블1))
            .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> tableService.changeEmpty(3L, 단체_테이블2))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 상태가 조리중이거나 식사중인 테이블을 빈 테이블 상태로 변경하려고 하면 예외가 발생한다.")
    @Test
    void changeEmptyImpossible3() {
        OrderTable 변경할_테이블 = TableFixture.create(1L, null, 2, false);
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(true);
        given(orderTableRepository.findById(변경할_테이블.getId())).willReturn(Optional.of(변경할_테이블));

        assertThatThrownBy(() -> tableService.changeEmpty(1L, 변경할_테이블))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블에 방문한 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        OrderTable 변경할_테이블 = TableFixture.create(1L, null, 1, false);
        given(orderTableRepository.findById(변경할_테이블.getId())).willReturn(Optional.of(변경할_테이블));
        given(orderTableRepository.save(변경할_테이블)).willReturn(변경할_테이블);
        변경할_테이블.setNumberOfGuests(2);

        OrderTable 변경된_테이블 = tableService.changeNumberOfGuests(변경할_테이블.getId(), 변경할_테이블);

        assertThat(변경된_테이블.getNumberOfGuests().getNumberOfGuests()).isEqualTo(2);
    }

    @DisplayName("테이블에 방문한 손님 수를 변경할 때, 등록되지 않은 테이블이면 예외가 발생한다.")
    @Test
    void changeNumberOfGuestsImpossible2() {
        OrderTable 등록안된_테이블 = TableFixture.create(1L, null, 2, false);
        given(orderTableRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, 등록안된_테이블))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
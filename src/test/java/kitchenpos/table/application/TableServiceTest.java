package kitchenpos.table.application;

import static kitchenpos.table.__fixture__.OrderTableTestFixture.주문_테이블_생성;
import static kitchenpos.table.__fixture__.TableGroupTestFixture.테이블_그룹_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.infra.OrderRepository;
import kitchenpos.table.infra.OrderTableRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("TableService 테스트")
@ExtendWith(MockitoExtension.class)
public class TableServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private TableService tableService;
    private OrderTable 주문_테이블;

    @BeforeEach
    void setUp() {
        주문_테이블 = 주문_테이블_생성(1L, null, 4, false);
    }

    @Test
    @DisplayName("주문 테이블 생성")
    public void create() {
        given(orderTableRepository.save(주문_테이블)).willReturn(주문_테이블);

        final OrderTable 생성된_주문_테이블 = tableService.create(주문_테이블);

        assertThat(생성된_주문_테이블.getId()).isEqualTo(주문_테이블.getId());
    }

    @Test
    @DisplayName("주문 테이블 조회")
    public void list() {
        given(orderTableRepository.findAll()).willReturn(Arrays.asList(주문_테이블));

        assertThat(tableService.list()).contains(주문_테이블);
    }

    @Test
    @DisplayName("주문 테이블 비울 때 주문 테이블 존재하지 않음 Exception")
    public void changeEmptyNotExistsException() {
        given(orderTableRepository.findById(주문_테이블.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeEmpty(1L, 주문_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블 비울 때 이미 단체로 지정됐을 경우 Exception")
    public void changeEmtpyAlreadyExistsInGroupException() {
        final TableGroup 테이블_그룹 = 테이블_그룹_생성(1L, LocalDateTime.now(), Collections.emptyList());
        final OrderTable 주문_테이블 = 주문_테이블_생성(1L, 테이블_그룹, 4, false);
        given(orderTableRepository.findById(주문_테이블.getId())).willReturn(Optional.of(주문_테이블));

        assertThatThrownBy(() -> tableService.changeEmpty(주문_테이블.getId(), 주문_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블 비울 때 COOKING 또는 MEAL 상태일 때 Exception")
    public void changeEmtpyOrderStatusIsCookingOrMeal() {
        final OrderTable 주문_테이블 = 주문_테이블_생성(1L, null, 4, false);

        given(orderTableRepository.findById(주문_테이블.getId())).willReturn(Optional.of(주문_테이블));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(주문_테이블.getId(),
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))).willReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(주문_테이블.getId(), 주문_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블 비움")
    public void changEmpty() {
        final OrderTable 주문_테이블_비움 = 주문_테이블_생성(1L, null, 4, true);

        given(orderTableRepository.findById(주문_테이블_비움.getId())).willReturn(Optional.of(주문_테이블_비움));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(주문_테이블_비움.getId(),
                Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))).willReturn(false);
        given(orderTableRepository.save(주문_테이블_비움)).willReturn(주문_테이블_비움);

        assertThat(tableService.changeEmpty(주문_테이블.getId(), 주문_테이블_비움).isEmpty()).isEqualTo(
                주문_테이블_비움.isEmpty());
    }

    @Test
    @DisplayName("고객 수 변경 시 0 미만이면 Exception")
    public void changeNumerOfGuestsIsMinusException() {
        final OrderTable 주문_테이블 = 주문_테이블_생성(1L, null, -1, true);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, 주문_테이블)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("고객 수 변경 시 존재하지 않는 테이블이면 Exception")
    public void changeNumerOfGuestsOrderTableNotExistsException() {
        given(orderTableRepository.findById(주문_테이블.getId())).willReturn(Optional.empty());
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, 주문_테이블)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("고객 수 변경 시 비어있는 테이블이면 Exception")
    public void changeNumerOfGuestsOrderTableIsEmptyException() {
        final OrderTable 빈_주문_테이블 = 주문_테이블_생성(1L, null, 4, true);
        given(orderTableRepository.findById(빈_주문_테이블.getId())).willReturn(Optional.of(빈_주문_테이블));
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, 빈_주문_테이블)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("고객 수 변경")
    public void changeNumerOfGuests() {
        given(orderTableRepository.findById(주문_테이블.getId())).willReturn(Optional.of(주문_테이블));
        given(orderTableRepository.save(주문_테이블)).willReturn(주문_테이블);

        assertThat(tableService.changeNumberOfGuests(1L, 주문_테이블).getNumberOfGuests())
                .isEqualTo(주문_테이블.getNumberOfGuests());
    }
}

package kitchenpos.application;

import static kitchenpos.__fixture__.OrderTableTestFixture.주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
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
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @InjectMocks
    private TableService tableService;
    private OrderTable 주문_테이블;

    @BeforeEach
    void setUp() {
        주문_테이블 = 주문_테이블_생성(1L, 1L, 4, false);
    }

    @Test
    @DisplayName("주문 테이블 생성")
    public void create() {
        given(orderTableDao.save(주문_테이블)).willReturn(주문_테이블);

        final OrderTable 생성된_주문_테이블 = tableService.create(주문_테이블);

        assertThat(생성된_주문_테이블.getId()).isEqualTo(주문_테이블.getId());
    }

    @Test
    @DisplayName("주문 테이블 조회")
    public void list() {
        given(orderTableDao.findAll()).willReturn(Arrays.asList(주문_테이블));

        assertThat(tableService.list()).contains(주문_테이블);
    }

    @Test
    @DisplayName("주문 테이블 비울 때 주문 테이블 존재하지 않음 Exception")
    public void changeEmptyNotExistsException() {
        given(orderTableDao.findById(주문_테이블.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeEmpty(1L, 주문_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블 비울 때 이미 단체로 지정됐을 경우 Exception")
    public void changeEmtpyAlreadyExistsInGroupException() {
        given(orderTableDao.findById(주문_테이블.getId())).willReturn(Optional.of(주문_테이블));

        assertThatThrownBy(() -> tableService.changeEmpty(주문_테이블.getId(), 주문_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블 비울 때 COOKING 또는 MEAL 상태일 때 Exception")
    public void changeEmtpyOrderStatusIsCookingOrMeal() {
        final OrderTable 주문_테이블 = 주문_테이블_생성(1L, null, 4, false);

        given(orderTableDao.findById(주문_테이블.getId())).willReturn(Optional.of(주문_테이블));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(주문_테이블.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(주문_테이블.getId(), 주문_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블 비움")
    public void changEmpty() {
        final OrderTable 주문_테이블_비움 = 주문_테이블_생성(1L, null, 4, true);

        given(orderTableDao.findById(주문_테이블_비움.getId())).willReturn(Optional.of(주문_테이블_비움));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(주문_테이블_비움.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
        given(orderTableDao.save(주문_테이블_비움)).willReturn(주문_테이블_비움);

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
        given(orderTableDao.findById(주문_테이블.getId())).willReturn(Optional.empty());
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, 주문_테이블)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("고객 수 변경 시 비어있는 테이블이면 Exception")
    public void changeNumerOfGuestsOrderTableIsEmptyException() {
        final OrderTable 빈_주문_테이블 = 주문_테이블_생성(1L, null, 4, true);
        given(orderTableDao.findById(빈_주문_테이블.getId())).willReturn(Optional.of(빈_주문_테이블));
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, 빈_주문_테이블)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("고객 수 변경")
    public void changeNumerOfGuests() {
        given(orderTableDao.findById(주문_테이블.getId())).willReturn(Optional.of(주문_테이블));
        given(orderTableDao.save(주문_테이블)).willReturn(주문_테이블);

        assertThat(tableService.changeNumberOfGuests(1L, 주문_테이블).getNumberOfGuests())
                .isEqualTo(주문_테이블.getNumberOfGuests());
    }
}

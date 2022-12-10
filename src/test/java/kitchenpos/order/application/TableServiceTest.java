package kitchenpos.order.application;

import static kitchenpos.order.domain.OrderTableTestFixture.generateOrderTable;
import static kitchenpos.order.domain.OrderTableTestFixture.generateOrderTableRequest;
import static kitchenpos.order.domain.TableGroupTestFixture.generateTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.order.application.TableService;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 테이블 관련 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    private OrderTable 주문테이블A;
    private OrderTable 주문테이블B;

    @BeforeEach
    void setUp() {
        주문테이블A = generateOrderTable(1L, null, 5, false);
        주문테이블B = generateOrderTable(2L, null, 4, false);
    }

    @DisplayName("주문 테이블을 생성한다.")
    @Test
    void createTable() {
        // given
        int numberOfGuests = 5;
        boolean empty = false;
        OrderTableRequest orderTableRequest = generateOrderTableRequest(numberOfGuests, empty);
        given(orderTableRepository.save(orderTableRequest.toOrderTable())).willReturn(주문테이블A);

        // when
        OrderTableResponse saveOrderTableResponse = tableService.create(orderTableRequest);

        // then
        assertAll(
                () -> assertThat(saveOrderTableResponse.getTableGroupId()).isNull(),
                () -> assertThat(saveOrderTableResponse.getNumberOfGuests()).isEqualTo(주문테이블A.getNumberOfGuests().value())
        );
    }

    @DisplayName("주문 테이블 전체 목록을 조회한다.")
    @Test
    void findAllTables() {
        // given
        List<OrderTable> orderTables = Arrays.asList(주문테이블A, 주문테이블B);
        given(orderTableRepository.findAll()).willReturn(orderTables);

        // when
        List<OrderTableResponse> findOrderTables = tableService.list();

        // then
        assertAll(
                () -> assertThat(findOrderTables).hasSize(orderTables.size()),
                () -> assertThat(findOrderTables.stream().map(OrderTableResponse::getId)).containsExactly(주문테이블A.getId(), 주문테이블B.getId())
        );
    }

    @DisplayName("주문 테이블의 비어있는지 여부를 변경한다.")
    @Test
    void changeTableEmpty() {
        // given
        boolean isEmpty = 주문테이블A.isEmpty();
        int numberOfGuests = 주문테이블A.getNumberOfGuests().value();
        OrderTableRequest changeOrderTableRequest = generateOrderTableRequest(numberOfGuests, !isEmpty);
        given(orderTableRepository.findById(주문테이블A.getId())).willReturn(Optional.of(주문테이블A));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(주문테이블A.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);

        // when
        OrderTableResponse resultOrderTable = tableService.changeEmpty(주문테이블A.getId(), changeOrderTableRequest);

        // then
        assertThat(resultOrderTable.isEmpty()).isEqualTo(!isEmpty);
    }

    @DisplayName("존재하지 않는 주문 테이블의 비어있는지 여부는 변경할 수 없다.")
    @Test
    void changeTableEmptyThrowErrorWhenOrderTableIsNotExists() {
        // given
        OrderTableRequest changeOrderTableRequest = generateOrderTableRequest(4, false);
        given(orderTableRepository.findById(10L)).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableService.changeEmpty(10L, changeOrderTableRequest));
    }

    @DisplayName("단체 지정이 되어 있는 테이블은 비어있는지 여부를 변경할 수 없다.")
    @Test
    void changeTableEmptyThrowErrorWhenTableGroupIdIsNotNull() {
        // given
        TableGroup tableGroup = generateTableGroup(Arrays.asList(주문테이블A, 주문테이블B));
        OrderTable orderTable = generateOrderTable(5L, tableGroup, 4, false);
        OrderTableRequest changeOrderTableRequest = generateOrderTableRequest(4, true);
        given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.of(orderTable));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableService.changeEmpty(orderTable.getId(), changeOrderTableRequest));
    }

    @DisplayName("주문 테이블의 주문 상태가 조리중이거나 식사중이면 비어있는지 여부를 변경할 수 없다.")
    @Test
    void changeTableEmptyThrowErrorWhenOrderStatusIsCooking() {
        // given
        OrderTableRequest changeOrderTableRequest = generateOrderTableRequest(3, true);
        given(orderTableRepository.findById(주문테이블A.getId())).willReturn(Optional.of(주문테이블A));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(주문테이블A.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableService.changeEmpty(주문테이블A.getId(), changeOrderTableRequest));
    }

    @ParameterizedTest(name = "주문 테이블에 방문한 손님 수를 변경한다. (변경된 손님 수: {0})")
    @ValueSource(ints = {6, 10, 13})
    void changeNumberOfGuestsInTable(int expectNumberOfGuests) {
        // given
        OrderTableRequest changeOrderTableRequest = generateOrderTableRequest(expectNumberOfGuests, false);
        given(orderTableRepository.findById(주문테이블A.getId())).willReturn(Optional.of(주문테이블A));

        // when
        OrderTableResponse resultOrderTable = tableService.changeNumberOfGuests(주문테이블A.getId(), changeOrderTableRequest);

        // then
        assertThat(resultOrderTable.getNumberOfGuests()).isEqualTo(expectNumberOfGuests);
    }

    @ParameterizedTest(name = "주문 테이블에 방문한 손님 수를 변경하고자 할 때, 변경하고자 하는 손님 수가 0보다 작으면 예외가 발생한다. (변경하고자 하는 손님 수: {0})")
    @ValueSource(ints = {-1, -3, -5})
    void changeNumberOfGuestsInTableThrowErrorWhenNumberOfGuestIsSmallerThanZero(int expectNumberOfGuests) {
        // given
        OrderTableRequest changeOrderTableRequest = generateOrderTableRequest(expectNumberOfGuests, false);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableService.changeNumberOfGuests(주문테이블A.getId(), changeOrderTableRequest));
    }

    @DisplayName("존재하지 않는 주문 테이블의 방문한 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestInTableThrowErrorWhenTableIsNotExists() {
        // given
        OrderTableRequest changeOrderTableRequest = generateOrderTableRequest(1, false);
        OrderTable orderTable = generateOrderTable(5L, null, 4, false);
        given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableService.changeNumberOfGuests(orderTable.getId(), changeOrderTableRequest));
    }

    @DisplayName("주문 테이블이 비어있는 경우, 방문한 손님 수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestInTableThrowErrorWhenTableIsEmpty() {
        // given
        OrderTable orderTable = generateOrderTable(5L, null, 4, true);
        OrderTableRequest changeOrderTableRequest = generateOrderTableRequest(1, true);
        given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.of(orderTable));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableService.changeNumberOfGuests(orderTable.getId(), changeOrderTableRequest));
    }
}

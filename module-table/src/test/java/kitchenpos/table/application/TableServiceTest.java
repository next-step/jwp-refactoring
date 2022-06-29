package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.fixture.OrderTableFixtureFactory;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;
    @Mock
    private TableValidator tableValidator;


    private OrderTable 주문테이블_EMPTY;
    private OrderTable 주문테이블_NON_EMPTY;

    @BeforeEach
    void setUp() {
        주문테이블_EMPTY = OrderTableFixtureFactory.createWithGuest(true, 4);
        주문테이블_NON_EMPTY = OrderTableFixtureFactory.createWithGuest(false, 4);
    }

    @DisplayName("주문 테이블을 생성할 수 있다.")
    @Test
    void create01() {
        // given
        OrderTableRequest orderTableRequest = OrderTableRequest.of(4, true);

        given(orderTableRepository.save(any(OrderTable.class))).willReturn(주문테이블_EMPTY);

        // when
        OrderTableResponse response = tableService.create(orderTableRequest);

        // then
        assertThat(response).isEqualTo(OrderTableResponse.from(주문테이블_EMPTY));
    }

    @DisplayName("주문 테이블을 조회할 수 있다.")
    @Test
    void find01() {
        // given
        given(orderTableRepository.findAll()).willReturn(Lists.newArrayList(주문테이블_EMPTY, 주문테이블_NON_EMPTY));

        // when
        List<OrderTableResponse> orderTables = tableService.list();

        // then
        assertThat(orderTables).containsExactly(OrderTableResponse.from(주문테이블_EMPTY),
                OrderTableResponse.from(주문테이블_NON_EMPTY));
    }

    @DisplayName("테이블의 상태를 빈 테이블 상태로 변경할 수 있다.")
    @Test
    void change01() {
        // given
        주문테이블_NON_EMPTY.changeEmpty(true);

        given(orderTableRepository.findById(any())).willReturn(Optional.ofNullable(주문테이블_NON_EMPTY));

        OrderTableRequest request = OrderTableRequest.of(0, true);

        // when
        OrderTableResponse response = tableService.changeEmpty(주문테이블_NON_EMPTY.getId(), request);

        // then
        assertAll(
                () -> assertThat(response).isEqualTo(OrderTableResponse.from(주문테이블_NON_EMPTY)),
                () -> assertThat(response.isEmpty()).isTrue()
        );
    }

    @DisplayName("테이블의 손님 수를 변경할 수 있다.")
    @Test
    void change05() {
        // given
        OrderTableRequest request = OrderTableRequest.of(10, false);

        given(orderTableRepository.findById(주문테이블_NON_EMPTY.getId())).willReturn(Optional.ofNullable(주문테이블_NON_EMPTY));

        // when
        OrderTableResponse response = tableService.changeNumberOfGuests(주문테이블_NON_EMPTY.getId(), request);

        // then
        assertAll(
                () -> assertThat(response).isEqualTo(OrderTableResponse.from(주문테이블_NON_EMPTY)),
                () -> assertThat(response.getNumberOfGuests()).isEqualTo(10)
        );
    }

    @DisplayName("테이블의 변경하려는 손님 수는 1명 이상이어야 한다.")
    @ParameterizedTest
    @ValueSource(ints = {-100, -1})
    void change06(int numberOfGuest) {
        // given
        OrderTableRequest request = OrderTableRequest.of(numberOfGuest, true);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeNumberOfGuests(주문테이블_NON_EMPTY.getId(), request));
    }

}
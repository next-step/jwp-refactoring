package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.application.fixture.OrderTableFixtureFactory;
import kitchenpos.application.fixture.TableGroupFixtureFactory;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.dto.table.OrderTableRequest;
import kitchenpos.dto.table.OrderTableResponse;
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
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    private TableGroup 단체_1;
    private OrderTable 빈_테이블;
    private OrderTable 주문_테이블;
    private OrderTable 주문_테이블_10명;
    private OrderTable 단체_1_주문_테이블;

    @BeforeEach
    void setUp() {
        빈_테이블 = OrderTableFixtureFactory.create(true);
        주문_테이블 = OrderTableFixtureFactory.create(false);
        주문_테이블_10명 = OrderTableFixtureFactory.createWithGuest(false, 10);

        단체_1_주문_테이블 = OrderTableFixtureFactory.create(true);
        단체_1 = TableGroupFixtureFactory.create(1L, Lists.newArrayList(단체_1_주문_테이블));
    }

    @DisplayName("테이블을 등록할 수 있다.")
    @Test
    void create01() {
        // given
        OrderTableRequest request = OrderTableRequest.of(0, true);

        given(orderTableRepository.save(any(OrderTable.class))).willReturn(빈_테이블);

        // when
        OrderTableResponse response = tableService.create(request);

        // then
        assertThat(response).isEqualTo(OrderTableResponse.from(빈_테이블));
    }

    @DisplayName("테이블 목록을 조회할 수 있다.")
    @Test
    void find01() {
        // given
        given(orderTableRepository.findAll()).willReturn(Lists.newArrayList(빈_테이블, 주문_테이블));

        // when
        List<OrderTableResponse> responses = tableService.list();

        // then
        assertThat(responses).containsExactly(OrderTableResponse.from(빈_테이블), OrderTableResponse.from(주문_테이블));
    }

    @DisplayName("테이블의 상태를 빈 테이블 상태로 변경할 수 있다.")
    @Test
    void change01() {
        // given
        주문_테이블.changeEmpty(true);

        given(orderTableRepository.findById(주문_테이블.getId())).willReturn(Optional.ofNullable(주문_테이블));

        OrderTableRequest request = OrderTableRequest.of(0, true);

        // when
        OrderTableResponse response = tableService.changeEmpty(주문_테이블.getId(), request);

        // then
        assertAll(
                () -> assertThat(response).isEqualTo(OrderTableResponse.from(주문_테이블)),
                () -> assertThat(response.isEmpty()).isTrue()
        );
    }

    @DisplayName("테이블이 존재하지 않으면 빈 테이블 상태로 변경할 수 없다.")
    @Test
    void change02() {
        // given
        OrderTableRequest request = OrderTableRequest.of(0, true);
        given(orderTableRepository.findById(주문_테이블.getId())).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeEmpty(주문_테이블.getId(), request));
    }

    @DisplayName("테이블 그룹이 존재하면 빈 테이블 상태로 변경할 수 없다.")
    @Test
    void change03() {
        // given
        OrderTableRequest request = OrderTableRequest.of(0, true);
        단체_1_주문_테이블.mappedByTableGroup(단체_1);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeEmpty(단체_1_주문_테이블.getId(), request));
    }

    @DisplayName("테이블의 주문 상태가 COOKING 혹은 MEAL 상태이면 빈 테이블 상태로 변경할 수 없다.")
    @Test
    void change04() {
        // given
        OrderTableRequest request = OrderTableRequest.of(0, true);
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(주문_테이블.getId(), Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))).willReturn(true);
        given(orderTableRepository.findById(주문_테이블.getId())).willReturn(Optional.ofNullable(주문_테이블));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeEmpty(주문_테이블.getId(), request));
    }

    @DisplayName("테이블의 손님 수를 변경할 수 있다.")
    @Test
    void change05() {
        // given
        OrderTableRequest request = OrderTableRequest.of(10, false);

        given(orderTableRepository.findById(주문_테이블.getId())).willReturn(Optional.ofNullable(주문_테이블));

        // when
        OrderTableResponse response = tableService.changeNumberOfGuests(주문_테이블.getId(), request);

        // then
        assertAll(
                () -> assertThat(response).isEqualTo(OrderTableResponse.from(주문_테이블)),
                () -> assertThat(response.getNumberOfGuests()).isEqualTo(주문_테이블_10명.getNumberOfGuests().getValue())
        );
    }

    @DisplayName("테이블의 변경하려는 손님 수는 1명 이상이어야 한다.")
    @ParameterizedTest
    @ValueSource(ints = {-100, -1})
    void change06(int numberOfGuest) {
        // given
        OrderTableRequest request = OrderTableRequest.of(numberOfGuest, true);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeNumberOfGuests(주문_테이블.getId(), request));
    }

    @DisplayName("테이블이 없으면 손님 수를 변경할 수 없다.")
    @Test
    void change07() {
        // given
        OrderTableRequest request = OrderTableRequest.of(10, true);
        given(orderTableRepository.findById(주문_테이블.getId())).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeNumberOfGuests(주문_테이블.getId(), request));
    }

    @DisplayName("테이블이 비어있으면 테이블 손님 수를 변경할 수 없다.")
    @Test
    void change08() {
        // given
        OrderTableRequest request = OrderTableRequest.of(10, true);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeNumberOfGuests(빈_테이블.getId(), request));
    }
}
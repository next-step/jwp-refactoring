package kitchenpos.orderTable.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.orderTable.domain.OrderTable;
import kitchenpos.orderTable.domain.OrderTableRepository;
import kitchenpos.orderTable.dto.OrderTableEmptyRequest;
import kitchenpos.orderTable.dto.OrderTableNumOfGuestRequest;
import kitchenpos.orderTable.dto.OrderTableRequest;
import kitchenpos.orderTable.dto.OrderTableResponse;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.domain.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static kitchenpos.utils.fixture.OrderTableFixtureFactory.createOrderTable;
import static kitchenpos.utils.fixture.TableGroupFixtureFactory.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@Transactional
public class TableServiceTest {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private TableGroupRepository tableGroupRepository;
    @Autowired
    private TableService tableService;

    private OrderTable 테이블_EMPTY;
    private OrderTable 테이블_NOT_EMPTY;

    @BeforeEach
    void setUp() {
        테이블_EMPTY = createOrderTable(0, true);
        테이블_NOT_EMPTY = createOrderTable(4, false);
    }

    @DisplayName("주문테이블을 등록할 수 있다")
    @Test
    void 주문테이블_등록(){
        //when
        OrderTableResponse savedTable = tableService.create(
                OrderTableRequest.of(테이블_EMPTY.getNumberOfGuests(), 테이블_EMPTY.isEmpty())
        );

        //then
        assertAll(
                () -> assertThat(savedTable.getId()).isNotNull(),
                () -> assertThat(savedTable.getNumberOfGuests()).isEqualTo(테이블_EMPTY.getNumberOfGuests())
        );
    }

    @DisplayName("주문테이블의 목록을 조회할 수 있다")
    @Test
    void 주문테이블_목록_조회(){
        //given
        OrderTableResponse savedTable = tableService.create(
                OrderTableRequest.of(테이블_EMPTY.getNumberOfGuests(), 테이블_EMPTY.isEmpty())
        );

        //when
        List<OrderTableResponse> list = tableService.list();

        //then
        assertThat(list).contains(savedTable);
    }

    @DisplayName("주문테이블의 비어있음 여부를 업데이트할 수 있다")
    @Test
    void 주문테이블_Empty_업데이트(){
        //given
        OrderTableResponse savedTable = tableService.create(
                OrderTableRequest.of(테이블_NOT_EMPTY.getNumberOfGuests(), 테이블_NOT_EMPTY.isEmpty())
        );

        //when
        OrderTableEmptyRequest emptyRequest = OrderTableEmptyRequest.from(true);
        tableService.changeEmpty(savedTable.getId(), emptyRequest);

        //then
        assertThat(orderTableRepository.findById(savedTable.getId()).get().isEmpty()).isTrue();
    }

    @DisplayName("주문테이블의 비어있음 여부를 업데이트시, 주문테이블이 존재해야 한다")
    @Test
    void 주문테이블_Empty_업데이트_주문테이블_검증(){
        //when
        OrderTableEmptyRequest emptyRequest = OrderTableEmptyRequest.from(true);

        //then
        assertThrows(IllegalArgumentException.class, () -> tableService.changeEmpty(0L, emptyRequest));
    }

    @DisplayName("주문테이블의 비어있음 여부를 업데이트시, 주문테이블이 테이블그룹에 등록되어있으면 안된다")
    @Test
    void 주문테이블_Empty_업데이트_주문테이블_테이블그룹_등록_검증(){
        //given
        OrderTable 테이블_1 = orderTableRepository.save(createOrderTable(0, true));
        OrderTable 테이블_2 = orderTableRepository.save(createOrderTable(0, true));
        TableGroup 테이블_그룹 = tableGroupRepository.save(createTableGroup(1L, LocalDateTime.now(), Arrays.asList(테이블_1, 테이블_2)));

        OrderTableEmptyRequest emptyRequest = OrderTableEmptyRequest.from(true);

        //then
        assertThrows(IllegalArgumentException.class, () -> tableService.changeEmpty(테이블_1.getId(), emptyRequest));
    }

    @DisplayName("주문테이블의 비어있음 여부를 업데이트시, 주문테이블에 COOKING이나 MEAL 상태의 주문이 있으면 안된다")
    @ParameterizedTest(name = "주문상태: {0}, 주문테이블의 비어있음 여부 업데이트 불가")
    @MethodSource("provideParametersForOrderTableUpdateWithOrderState")
    void 주문테이블_Empty_업데이트_주문_상태_검증(OrderStatus orderStatus){
        //given
        OrderTableResponse savedTable = tableService.create(
                OrderTableRequest.of(테이블_NOT_EMPTY.getNumberOfGuests(), 테이블_NOT_EMPTY.isEmpty())
        );
        OrderTable orderTable = orderTableRepository.findById(savedTable.getId()).get();
        Order order = new Order(orderTable, LocalDateTime.now());
        order.changeStatus(orderStatus);
        orderRepository.save(order);

        //when
        OrderTableEmptyRequest emptyRequest = OrderTableEmptyRequest.from(false);

        //then
        assertThrows(IllegalArgumentException.class, () -> tableService.changeEmpty(savedTable.getId(), emptyRequest));
    }

    private static Stream<Arguments> provideParametersForOrderTableUpdateWithOrderState() {
        return Stream.of(
                Arguments.of(OrderStatus.MEAL),
                Arguments.of(OrderStatus.COOKING)
        );
    }

    @DisplayName("주문테이블의 손님 수를 업데이트할 수 있다")
    @Test
    void 주문테이블_손님수_업데이트(){
        //given
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(테이블_NOT_EMPTY));
        given(orderTableRepository.save(any(OrderTable.class))).willReturn(createOrderTable(테이블_NOT_EMPTY.getId(),
                테이블_NOT_EMPTY.getTableGroup(), 1, false));

        //when
        OrderTableNumOfGuestRequest oneGuestRequest = OrderTableNumOfGuestRequest.from(1);
        tableService.changeNumberOfGuests(테이블_NOT_EMPTY.getId(), oneGuestRequest);

        //then
        assertThat(테이블_NOT_EMPTY.getNumberOfGuests()).isEqualTo(1);
    }

    @DisplayName("주문테이블의 손님 수를 업데이트 시, 손님 수는 0 이상이다")
    @Test
    void 주문테이블_손님수_업데이트_손님수_검증(){
        //when
        OrderTableNumOfGuestRequest invalidRequest = OrderTableNumOfGuestRequest.from(-1);

        //then
        assertThrows(IllegalArgumentException.class,
                () -> tableService.changeNumberOfGuests(테이블_NOT_EMPTY.getId(), invalidRequest));
    }

    @DisplayName("주문테이블의 손님 수를 업데이트 시, 업데이트할 주문테이블이 존재해야 한다")
    @Test
    void 주문테이블_손님수_업데이트_주문테이블_검증(){
        //given
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));

        //when
        OrderTableNumOfGuestRequest oneGuestRequest = OrderTableNumOfGuestRequest.from(1);

        //then
        assertThrows(IllegalArgumentException.class,
                () -> tableService.changeNumberOfGuests(테이블_NOT_EMPTY.getId(), oneGuestRequest));
    }

    @DisplayName("주문테이블의 손님 수를 업데이트 시, 주문테이블이 비어있으면 안된다")
    @Test
    void 주문테이블_손님수_업데이트_주문테이블_Empty_검증(){
        //given
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(테이블_EMPTY));

        //when
        OrderTableNumOfGuestRequest oneGuestRequest = OrderTableNumOfGuestRequest.from(1);

        //then
        assertThrows(IllegalArgumentException.class,
                () -> tableService.changeNumberOfGuests(테이블_EMPTY.getId(), oneGuestRequest));
    }
}

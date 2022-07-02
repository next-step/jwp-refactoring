package kitchenpos.ordertable.application;

import kitchenpos.order.exception.IllegalOrderException;
import kitchenpos.ordertable.exception.IllegalOrderTableException;
import kitchenpos.ordertable.exception.NoSuchOrderTableException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.dto.OrderTableEmptyRequest;
import kitchenpos.ordertable.dto.OrderTableNumOfGuestRequest;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static kitchenpos.utils.fixture.OrderFixtureFactory.*;
import static kitchenpos.utils.fixture.OrderTableFixtureFactory.createOrderTable;
import static kitchenpos.utils.fixture.TableGroupFixtureFactory.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@DisplayName("테이블 Service 테스트")
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
        assertThrows(NoSuchOrderTableException.class, () -> tableService.changeEmpty(0L, emptyRequest));
    }

    @DisplayName("주문테이블의 비어있음 여부를 업데이트시, 주문테이블이 테이블그룹에 등록되어있으면 안된다")
    @Test
    void 주문테이블_Empty_업데이트_주문테이블_테이블그룹_등록_검증(){
        //given
        OrderTable 테이블_1 = orderTableRepository.save(createOrderTable(0, true));
        OrderTable 테이블_2 = orderTableRepository.save(createOrderTable(0, true));
        TableGroup 테이블_그룹 = tableGroupRepository.save(createTableGroup(LocalDateTime.now(), Arrays.asList(테이블_1, 테이블_2)));

        OrderTableEmptyRequest emptyRequest = OrderTableEmptyRequest.from(true);

        //then
        assertThrows(IllegalOrderTableException.class, () -> tableService.changeEmpty(테이블_1.getId(), emptyRequest));
    }

    @DisplayName("주문테이블의 비어있음 여부를 업데이트시, 주문테이블에 COOKING이나 MEAL 상태의 주문이 있으면 안된다")
    @ParameterizedTest(name = "주문상태: {0}, 주문테이블의 비어있음 여부 업데이트 불가")
    @MethodSource("provideParametersForOrderTableUpdateWithOrderState")
    @Disabled
    void 주문테이블_Empty_업데이트_주문_상태_검증(OrderStatus orderStatus){
        //given
        OrderTableResponse savedTable = tableService.create(
                OrderTableRequest.of(테이블_NOT_EMPTY.getNumberOfGuests(), 테이블_NOT_EMPTY.isEmpty())
        );
        OrderTable orderTable = orderTableRepository.findById(savedTable.getId()).get();
        Order order = createOrder(orderTable, LocalDateTime.now(), new ArrayList<>());
        order.changeStatus(orderStatus);
        orderRepository.save(order);

        //when
        OrderTableEmptyRequest emptyRequest = OrderTableEmptyRequest.from(false);

        //then
        assertThrows(IllegalOrderException.class, () -> tableService.changeEmpty(savedTable.getId(), emptyRequest));
    }

    private static Stream<Arguments> provideParametersForOrderTableUpdateWithOrderState() {
        return Stream.of(
                Arguments.of(OrderStatus.MEAL),
                Arguments.of(OrderStatus.COOKING)
        );
    }

    @DisplayName("주문테이블의 손님 수를 업데이트할 수 있다")
    @Test
    @Disabled
    void 주문테이블_손님수_업데이트(){
        //given
        OrderTableResponse savedTable = tableService.create(
                OrderTableRequest.of(테이블_NOT_EMPTY.getNumberOfGuests(), 테이블_NOT_EMPTY.isEmpty())
        );

        //when
        OrderTableNumOfGuestRequest oneGuestRequest = OrderTableNumOfGuestRequest.from(1);
        tableService.changeNumberOfGuests(savedTable.getId(), oneGuestRequest);

        //then
        assertThat(orderTableRepository.findById(savedTable.getId()).get().getNumberOfGuests()).isEqualTo(1);
    }

    @DisplayName("주문테이블의 손님 수를 업데이트 시, 손님 수는 0 이상이다")
    @Test
    void 주문테이블_손님수_업데이트_손님수_검증(){
        //given
        OrderTableResponse savedTable = tableService.create(
                OrderTableRequest.of(테이블_NOT_EMPTY.getNumberOfGuests(), 테이블_NOT_EMPTY.isEmpty())
        );

        //when
        OrderTableNumOfGuestRequest invalidRequest = OrderTableNumOfGuestRequest.from(-1);

        //then
        assertThrows(IllegalOrderTableException.class,
                () -> tableService.changeNumberOfGuests(savedTable.getId(), invalidRequest));
    }

    @DisplayName("주문테이블의 손님 수를 업데이트 시, 업데이트할 주문테이블이 존재해야 한다")
    @Test
    void 주문테이블_손님수_업데이트_주문테이블_검증(){
        //when
        OrderTableNumOfGuestRequest oneGuestRequest = OrderTableNumOfGuestRequest.from(1);

        //then
        assertThrows(NoSuchOrderTableException.class,
                () -> tableService.changeNumberOfGuests(0L, oneGuestRequest));
    }

    @DisplayName("주문테이블의 손님 수를 업데이트 시, 주문테이블이 비어있으면 안된다")
    @Test
    void 주문테이블_손님수_업데이트_주문테이블_Empty_검증(){
        //given
        OrderTableResponse savedTable = tableService.create(
                OrderTableRequest.of(테이블_EMPTY.getNumberOfGuests(), 테이블_EMPTY.isEmpty())
        );

        //when
        OrderTableNumOfGuestRequest oneGuestRequest = OrderTableNumOfGuestRequest.from(1);

        //then
        assertThrows(IllegalOrderTableException.class,
                () -> tableService.changeNumberOfGuests(savedTable.getId(), oneGuestRequest));
    }
}

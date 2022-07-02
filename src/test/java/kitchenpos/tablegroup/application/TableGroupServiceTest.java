package kitchenpos.tablegroup.application;

import kitchenpos.exception.IllegalOrderException;
import kitchenpos.exception.IllegalOrderTableException;
import kitchenpos.exception.NoSuchTableGroupException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Stream;

import static kitchenpos.utils.fixture.OrderFixtureFactory.*;
import static kitchenpos.utils.fixture.OrderTableFixtureFactory.createOrderTable;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@DisplayName("테이블그룹 Service 테스트")
class TableGroupServiceTest {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private TableGroupRepository tableGroupRepository;
    @Autowired
    private TableGroupService tableGroupService;

    private OrderTable 테이블_1;
    private OrderTable 테이블_2;
    private OrderTable 테이블_3;

    @BeforeEach
    void setUp() {
        테이블_1 = orderTableRepository.save(createOrderTable(0, true));
        테이블_2 = orderTableRepository.save(createOrderTable(0, true));
        테이블_3 = orderTableRepository.save(createOrderTable(0, true));
    }

    @DisplayName("테이블그룹을 등록할 수 있다")
    @Test
    void 테이블그룹_등록(){
        //given
        TableGroupRequest 단체_테이블_request = TableGroupRequest.from(
                Arrays.asList(테이블_1.getId(), 테이블_2.getId(), 테이블_3.getId())
        );

        //when
        TableGroupResponse savedTableGroup = tableGroupService.create(단체_테이블_request);

        //then
        Assertions.assertAll(
                () -> assertThat(savedTableGroup.getOrderTables().size()).isEqualTo(3),
                () -> assertThat(orderTableRepository.findById(테이블_1.getId()).get().getTableGroup()).isNotNull(),
                () -> assertThat(orderTableRepository.findById(테이블_2.getId()).get().getTableGroup()).isNotNull(),
                () -> assertThat(orderTableRepository.findById(테이블_3.getId()).get().getTableGroup()).isNotNull()
        );
    }

    @DisplayName("등록하려는 테이블그룹의 주문테이블이 모두 존재해야 한다")
    @Test
    void 테이블그룹_등록_주문테이블_검증(){
        //given
        TableGroupRequest invalidRequest = TableGroupRequest.from(
                Arrays.asList(테이블_1.getId(), 테이블_2.getId(), 100L)
        );

        //then
        assertThrows(IllegalOrderTableException.class, () -> tableGroupService.create(invalidRequest));
    }

    @DisplayName("테이블 그룹을 삭제할 수 있다")
    @Test
    void 테이블그룹_삭제(){
        //given
        TableGroupRequest 단체_테이블_request = TableGroupRequest.from(
                Arrays.asList(테이블_1.getId(), 테이블_2.getId(), 테이블_3.getId())
        );
        TableGroupResponse savedTableGroup = tableGroupService.create(단체_테이블_request);

        //when
        tableGroupService.ungroup(savedTableGroup.getId());

        //then
        Assertions.assertAll(
                () -> assertThat(orderTableRepository.findById(테이블_1.getId()).get().getTableGroup()).isNull(),
                () -> assertThat(orderTableRepository.findById(테이블_2.getId()).get().getTableGroup()).isNull()
        );
    }

    @DisplayName("존재하지 않는 테이블그룹은 삭제할 수 없다")
    @Test
    void 테이블그룹_삭제_테이블그룹Id_검증(){
        //then
        assertThrows(NoSuchTableGroupException.class, () -> tableGroupService.ungroup(0L));
    }

    @DisplayName("테이블그룹 삭제시, 주문테이블에 COOKING이나 MEAL 상태의 주문이 있으면 안된다")
    @ParameterizedTest(name = "주문상태: {0}, 테이블그룹 삭제 불가")
    @MethodSource("provideParametersForTableGroupDeleteWithOrderState")
    void 테이블그룹_삭제_주문상태_검증(OrderStatus orderStatus){
        //given
        TableGroupRequest 단체_테이블_request = TableGroupRequest.from(
                Arrays.asList(테이블_1.getId(), 테이블_2.getId(), 테이블_3.getId())
        );
        TableGroupResponse savedTableGroup = tableGroupService.create(단체_테이블_request);
        Order order = createOrder(테이블_1, LocalDateTime.now());
        order.changeStatus(orderStatus);
        orderRepository.save(order);

        //then
        assertThrows(IllegalOrderException.class, () -> tableGroupService.ungroup(savedTableGroup.getId()));
    }

    private static Stream<Arguments> provideParametersForTableGroupDeleteWithOrderState() {
        return Stream.of(
                Arguments.of(OrderStatus.MEAL),
                Arguments.of(OrderStatus.COOKING)
        );
    }
}
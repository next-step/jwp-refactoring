package kitchenpos.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.orderTable.domain.OrderTable;
import kitchenpos.orderTable.domain.OrderTableRepository;
import kitchenpos.orderTable.dto.OrderTableResponse;
import kitchenpos.tableGroup.application.TableGroupService;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.domain.TableGroupRepository;
import kitchenpos.tableGroup.dto.TableGroupRequest;
import kitchenpos.tableGroup.dto.TableGroupResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static kitchenpos.factory.OrderTableFixtureFactory.createOrderTable;
import static kitchenpos.factory.TableGroupFixtureFactory.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@Transactional
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
        테이블_1 = createOrderTable(1L, null, 0, true);
        테이블_2 = createOrderTable(2L, null, 0, true);
        테이블_3 = createOrderTable(3L, null, 0, true);
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
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(invalidRequest));
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

    @DisplayName("주문테이블에 COOKING이나 MEAL 상태의 주문이 있으면 삭제할 수 없다")
    @Test
    void 테이블그룹_삭제_주문상태_검증(){
        //given
//        TableGroupRequest 단체_테이블_request = TableGroupRequest.from(
//                Arrays.asList(테이블_1.getId(), 테이블_2.getId(), 테이블_3.getId())
//        );
//        TableGroupResponse savedTableGroup = tableGroupService.create(단체_테이블_request);
//
//        //then
//        assertThrows(IllegalArgumentException.class, () -> tableGroupService.ungroup(단체_테이블.getId()));
    }
}
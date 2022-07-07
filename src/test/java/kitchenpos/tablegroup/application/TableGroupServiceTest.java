package kitchenpos.tablegroup.application;

import kitchenpos.order.exception.IllegalOrderException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.event.GroupTableEvent;
import kitchenpos.ordertable.exception.IllegalOrderTableException;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Arrays;

import static kitchenpos.utils.fixture.OrderTableFixtureFactory.createOrderTable;
import static kitchenpos.utils.fixture.TableGroupFixtureFactory.createTableGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private TableGroupRepository tableGroupRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @InjectMocks
    private TableGroupService tableGroupService;

    private TableGroup 단체_테이블;
    private OrderTable 테이블_1;
    private OrderTable 테이블_2;
    private OrderTable 테이블_Full;

    @BeforeEach
    void setUp() {
        테이블_1 = createOrderTable(1L, null, 0, true);
        테이블_2 = createOrderTable(2L, null, 0, true);
        단체_테이블 = createTableGroup(1L, Arrays.asList(테이블_1, 테이블_2));

        테이블_Full = createOrderTable(4L, null, 4, false);
    }

    @DisplayName("테이블그룹을 등록할 수 있다")
    @Test
    void 테이블그룹_등록(){
        //given
        OrderTable 테이블_1 = createOrderTable(1L, null, 0, true);
        OrderTable 테이블_2 = createOrderTable(2L, null, 0, true);
        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(단체_테이블);

        //when
        TableGroupRequest tableGroupRequest = TableGroupRequest.from(
                Arrays.asList(테이블_1.getId(), 테이블_2.getId())
        );
        TableGroupResponse savedTableGroup = tableGroupService.create(tableGroupRequest);

        //then
        assertThat(savedTableGroup).isEqualTo(TableGroupResponse.from(단체_테이블));
    }

    @DisplayName("테이블그룹 내 주문테이블은 2개 이상이어야 한다")
    @Test
    void 테이블그룹_등록_주문테이블_두개_이상_검증(){
        //given
        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(단체_테이블);
        willThrow(IllegalOrderTableException.class).given(eventPublisher).publishEvent(any(GroupTableEvent.class));
        OrderTable 테이블_1 = createOrderTable(1L, null, 0, true);
        TableGroupRequest invalidTableGroupRequest = TableGroupRequest.from(Arrays.asList(테이블_1.getId()));

        //then
        assertThrows(IllegalOrderTableException.class, () -> tableGroupService.create(invalidTableGroupRequest));
    }

    @DisplayName("등록하려는 테이블그룹의 주문테이블이 모두 존재해야 한다")
    @Test
    void 테이블그룹_등록_주문테이블_검증(){
        //given
        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(단체_테이블);
        willThrow(IllegalOrderTableException.class).given(eventPublisher).publishEvent(any(GroupTableEvent.class));
        OrderTable 테이블_1 = createOrderTable(1L, null, 0, true);
        OrderTable 테이블_2 = createOrderTable(2L, null, 0, true);

        //when
        TableGroupRequest invalidTableGroupRequest = TableGroupRequest.from(
                Arrays.asList(테이블_1.getId(), 테이블_2.getId())
        );

        //then
        assertThrows(IllegalOrderTableException.class, () -> tableGroupService.create(invalidTableGroupRequest));
    }

    @DisplayName("비어있는 주문테이블만 등록할 수 있다")
    @Test
    void 테이블그룹_등록_주문테이블_Empty_검증(){
        //given
        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(단체_테이블);
        willThrow(IllegalOrderTableException.class).given(eventPublisher).publishEvent(any(GroupTableEvent.class));
        OrderTable 테이블_1 = createOrderTable(1L, null, 0, true);

        //when
        TableGroupRequest invalidTableGroupRequest = TableGroupRequest.from(
                Arrays.asList(테이블_1.getId(), 테이블_Full.getId())
        );

        //then
        assertThrows(IllegalOrderTableException.class, () -> tableGroupService.create(invalidTableGroupRequest));
    }

    @DisplayName("이미 테이블그룹에 속해있는 주문테이블은 등록할 수 없다")
    @Test
    void 테이블그룹_등록_주문테이블_이미_테이블그룹에_속해있는지_검증(){
        //given
        given(tableGroupRepository.save(any(TableGroup.class))).willReturn(단체_테이블);
        willThrow(IllegalOrderTableException.class).given(eventPublisher).publishEvent(any(GroupTableEvent.class));
        OrderTable 테이블_1 = createOrderTable(1L, null, 0, true);

        //when
        TableGroupRequest invalidTableGroupRequest = TableGroupRequest.from(
                Arrays.asList(테이블_1.getId(), 테이블_2.getId())
        );

        //then
        assertThrows(IllegalOrderTableException.class, () -> tableGroupService.create(invalidTableGroupRequest));
    }
}
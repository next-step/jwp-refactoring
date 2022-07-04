package kitchenpos.ordertable.event;

import kitchenpos.ordertable.application.TableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.exception.IllegalOrderTableException;
import kitchenpos.tablegroup.domain.TableGroup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static kitchenpos.utils.fixture.OrderTableFixtureFactory.createOrderTable;
import static kitchenpos.utils.fixture.TableGroupFixtureFactory.createTableGroup;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class GroupTableEventHandlerTest {
    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private GroupTableEventHandler groupTableEventHandler;

    private TableGroup 단체_테이블;
    private OrderTable 테이블_1;
    private OrderTable 테이블_2;

    @BeforeEach
    void setUp() {
        테이블_1 = createOrderTable(1L, null, 0, true);
        테이블_2 = createOrderTable(2L, null, 0, true);
        단체_테이블 = createTableGroup(1L);
    }

    @DisplayName("테이블 그룹화 Event 성공")
    @Test
    void 테이블_그룹화_성공(){
        //given
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(Arrays.asList(테이블_1, 테이블_2));
        GroupTableEvent event = GroupTableEvent.of(단체_테이블.getId(), Arrays.asList(테이블_1.getId(), 테이블_2.getId()));

        //when
        groupTableEventHandler.handle(event);

        //then
        assertAll(
                () -> Assertions.assertThat(테이블_1.getTableGroupId()).isEqualTo(단체_테이블.getId()),
                () -> Assertions.assertThat(테이블_2.getTableGroupId()).isEqualTo(단체_테이블.getId())
        );
    }

    @DisplayName("테이블 그룹화 Event 실패: 주문테이블은 2개 이상이어야 한다")
    @Test
    void 테이블_그룹화_실패_주문테이블_2개_이상_검증(){
        //given
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(Arrays.asList(테이블_1));
        GroupTableEvent event = GroupTableEvent.of(단체_테이블.getId(), Arrays.asList(테이블_1.getId()));

        //then
        assertThrows(IllegalOrderTableException.class, () -> groupTableEventHandler.handle(event));
    }

    @DisplayName("테이블 그룹화 Event 실패: 주문테이블이 모두 존재해야 한다")
    @Test
    void 테이블_그룹화_실패_주문테이블_존재_검증(){
        //given
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(Arrays.asList(테이블_1));
        GroupTableEvent event = GroupTableEvent.of(단체_테이블.getId(), Arrays.asList(테이블_1.getId(), 테이블_2.getId()));

        //then
        assertThrows(IllegalOrderTableException.class, () -> groupTableEventHandler.handle(event));
    }

    @DisplayName("테이블 그룹화 Event 실패: 비어있는 주문테이블만 등록할 수 있다")
    @Test
    void 테이블_그룹화_실패_주문테이블_Empty_검증(){
        //given
        OrderTable 테이블_Full = createOrderTable(3L, null, 4, false);
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(Arrays.asList(테이블_1, 테이블_Full));
        GroupTableEvent event = GroupTableEvent.of(단체_테이블.getId(), Arrays.asList(테이블_1.getId(), 테이블_Full.getId()));

        //then
        assertThrows(IllegalOrderTableException.class, () -> groupTableEventHandler.handle(event));
    }


    @DisplayName("테이블 그룹화 Event 실패: 테이블그룹에 속해있지 않은 주문테이블만 등록할 수 있다")
    @Test
    void 테이블_그룹화_실패_주문테이블_이미_그룹화_검증(){
        //given
        OrderTable 테이블_Grouped = createOrderTable(3L, 2L, 4, false);
        given(orderTableRepository.findAllByIdIn(anyList())).willReturn(Arrays.asList(테이블_1, 테이블_Grouped));
        GroupTableEvent event = GroupTableEvent.of(단체_테이블.getId(), Arrays.asList(테이블_1.getId(), 테이블_Grouped.getId()));

        //when
        assertThrows(IllegalOrderTableException.class, () -> groupTableEventHandler.handle(event));
    }
}

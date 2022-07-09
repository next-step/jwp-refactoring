package kichenpos.order.application;

import kichenpos.order.domain.OrderTable;
import kichenpos.order.domain.OrderTableRepository;
import kichenpos.order.domain.TableGroup;
import kichenpos.order.domain.TableGroupRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {
    @Mock
    private OrderTableValidator orderTableValidator;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @InjectMocks
    private TableGroupService tableGroupService;

    @Test
    void 주문_테이블이_2개_미만이면_단체를_지정할_수_없다() {
        // when & then
        assertThatThrownBy(() ->
                tableGroupService.create(new TableGroup(Collections.singletonList(new OrderTable(1L))))
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("2개 이상의 테이블을 단체 지정할 수 있습니다.");
    }

    @Test
    void 주문_테이블이_저장되어_있지_않으면_단체를_지정할_수_없다() {
        // given
        TableGroup tableGroup = new TableGroup(createOrderTables());
        given(orderTableRepository.findAllByIdIn(createOrderTableIds()))
                .willReturn(Collections.singletonList(new OrderTable(1L)));

        // when & then
        assertThatThrownBy(() ->
                tableGroupService.create(tableGroup)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("중복된 테이블이 있거나 등록되지 않은 테이블이 있습니다.");
    }

    @Test
    void 주문_테이블이_빈_테이블이_아니거나_이미_단체_지정_되어_있으면_단체를_지정할_수_없다() {
        // given
        TableGroup tableGroup = new TableGroup(createOrderTables());
        given(orderTableRepository.findAllByIdIn(createOrderTableIds()))
                .willReturn(createOrderTables());

        // when & then
        assertThatThrownBy(() ->
                tableGroupService.create(tableGroup)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블이 아니거나 이미 단체가 지정되었습니다.");
    }

    @Test
    void 단체_지정을_등록한다() {
        // given
        TableGroup tableGroup = new TableGroup(createOrderTables());
        given(orderTableRepository.findAllByIdIn(createOrderTableIds()))
                .willReturn(createEmptyTables());
        given(tableGroupRepository.save(tableGroup))
                .willReturn(new TableGroup(1L, LocalDateTime.now()));

        // when
        TableGroup result = tableGroupService.create(tableGroup);

        // then
        assertThat(result.getId()).isNotNull();
    }

    @Test
    void 단체_지정을_해제할_수_있다() {
        // given
        List<OrderTable> orderTables = createOrderTables();
        Optional<TableGroup> tableGroup = Optional.of(new TableGroup(orderTables));

        given(tableGroupRepository.findById(1L))
                .willReturn(tableGroup);
        willDoNothing().given(orderTableValidator).validateUngroup(createOrderTableIds());

        // when
        tableGroupService.ungroup(1L);

        // then
        assertAll(
                () -> assertThat(orderTables.get(0).getTableGroupId()).isNull(),
                () ->assertThat(orderTables.get(1).getTableGroupId()).isNull()
        );
    }

    private List<OrderTable> createOrderTables() {
        return Arrays.asList(new OrderTable(1L), new OrderTable(2L));
    }

    private List<OrderTable> createEmptyTables() {
        OrderTable orderTable = new OrderTable(1L);
        orderTable.changeEmpty(true);

        OrderTable orderTable1 = new OrderTable(2L);
        orderTable1.changeEmpty(true);

        return Arrays.asList(orderTable, orderTable1);
    }

    public static List<Long> createOrderTableIds() {
        return Arrays.asList(1L, 2L);
    }
}

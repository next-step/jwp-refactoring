package kitchenpos.table.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import kitchenpos.common.event.GroupedTablesEvent;
import kitchenpos.common.event.UngroupedTablesEvent;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;

@SpringBootTest
class OrderTableEventHandlerTest {
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private ApplicationEventPublisher publisher;

    private TableGroup tableGroup;
    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        tableGroup = tableGroupRepository.save(new TableGroup());
        orderTable1 = new OrderTable(3, false);
        orderTable2 = new OrderTable(3, false);
    }

    @Test
    @DisplayName("단체지정 취소 이벤트 핸들러 처리 결과 확인")
    void ungroup_events() {
        // given
        orderTable1.groupBy(tableGroup.getId());
        orderTable2.groupBy(tableGroup.getId());
        orderTableRepository.saveAll(Arrays.asList(orderTable1, orderTable2));

        // when
        publisher.publishEvent(new UngroupedTablesEvent(Arrays.asList(orderTable1.getId(), orderTable2.getId())));

        // then
        OrderTable resultOrderTable1 = orderTableRepository.findById(orderTable1.getId()).get();
        OrderTable resultOrderTable2 = orderTableRepository.findById(orderTable2.getId()).get();
        assertAll(
                () -> assertThat(resultOrderTable1.hasTableGroup()).isFalse(),
                () -> assertThat(resultOrderTable2.hasTableGroup()).isFalse()
        );

    }

    @Test
    @DisplayName("단체지정 이벤트 핸들러 처리 결과 확인")
    void group_events() {
        // given
        orderTableRepository.saveAll(Arrays.asList(orderTable1, orderTable2));

        // when
        publisher.publishEvent(new GroupedTablesEvent(Arrays.asList(orderTable1.getId(), orderTable2.getId()), tableGroup.getId()));

        // then
        OrderTable resultOrderTable1 = orderTableRepository.findById(orderTable1.getId()).get();
        OrderTable resultOrderTable2 = orderTableRepository.findById(orderTable2.getId()).get();
        assertAll(
                () -> assertThat(resultOrderTable1.hasTableGroup()).isTrue(),
                () -> assertThat(resultOrderTable2.hasTableGroup()).isTrue()
        );
    }
}

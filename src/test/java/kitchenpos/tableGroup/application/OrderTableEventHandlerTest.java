package kitchenpos.tableGroup.application;


import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.tableGroup.domain.TableGroup;
import kitchenpos.tableGroup.domain.TableGroupRepository;
import kitchenpos.tableGroup.dto.OrderTableIdRequest;
import kitchenpos.tableGroup.event.OrderTableGroupEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class OrderTableEventHandlerTest {
    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private TableGroupValidator tableGroupValidator;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @DisplayName("주문테이블 그룹 검증")
    @Test
    void group() {
        List<OrderTableIdRequest> requests = Arrays.asList(new OrderTableIdRequest(1L), new OrderTableIdRequest(2L));
        final List<OrderTable> orderTables = tableGroupValidator.getOrderTable(requests);
        final TableGroup saved = tableGroupRepository.save(TableGroup.empty());

        OrderTableGroupEvent event = OrderTableGroupEvent.from(saved.getId(), orderTables);

        publisher.publishEvent(event);

        assertThat(orderTables.get(0).getTableGroupId()).isEqualTo(saved.getId());
        assertThat(orderTables.get(1).getTableGroupId()).isEqualTo(saved.getId());
    }
}

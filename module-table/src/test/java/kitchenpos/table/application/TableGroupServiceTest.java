package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.List;
import kitchenpos.table.application.handler.OrderTableGroupingEventHandler;
import kitchenpos.table.application.handler.OrderTableUngroupEventHandler;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import kitchenpos.table.domain.event.TableGroupingEvent;
import kitchenpos.table.domain.event.TableUngroupEvent;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.exception.CreateTableGroupException;
import kitchenpos.table.fixture.OrderTableFixtureFactory;
import kitchenpos.table.fixture.TableGroupFixtureFactory;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Component.class))
class TableGroupServiceTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableGroupingEventHandler groupingEventHandler;

    @Autowired
    private OrderTableUngroupEventHandler ungroupEventHandler;

    private TableGroup 단체_1;
    private OrderTable 주문_1_테이블;
    private OrderTable 주문_2_테이블;
    private OrderTable 주문_테이블_10명;

    @BeforeEach
    void setUp() {
        주문_1_테이블 = OrderTableFixtureFactory.createWithGuest(true, 2);
        주문_2_테이블 = OrderTableFixtureFactory.createWithGuest(true, 2);
        주문_테이블_10명 = OrderTableFixtureFactory.createWithGuest(false, 10);
        단체_1 = TableGroupFixtureFactory.create(1L);

        주문_1_테이블 = orderTableRepository.save(주문_1_테이블);
        주문_2_테이블 = orderTableRepository.save(주문_2_테이블);
        주문_테이블_10명 = orderTableRepository.save(주문_테이블_10명);

        단체_1 = tableGroupRepository.save(단체_1);
    }

    @DisplayName("단체를 지정할 수 있다.")
    @Test
    void create01() {
        // given
        TableGroupRequest request = TableGroupRequest.from(Lists.newArrayList(주문_1_테이블.getId(), 주문_2_테이블.getId()));

        // when
        TableGroupResponse response = tableGroupService.create(request);

        // then
        TableGroup findTableGroup = tableGroupRepository.findById(response.getId()).get();
        assertThat(response).isEqualTo(TableGroupResponse.from(findTableGroup));
    }

    @DisplayName("주문 테이블이 비어있으면 테이블을 단체로 지정할 수 없다.")
    @Test
    void create02() {
        // given
        List<Long> orderTableIds = Collections.emptyList();

        // when & then
        assertThrows(CreateTableGroupException.class, () -> groupingEventHandler.handle(new TableGroupingEvent(단체_1, orderTableIds)));
    }

    @DisplayName("주문 테이블이 1개이면 테이블을 단체로 지정할 수 없다.")
    @Test
    void create03() {
        // given
        List<Long> orderTableIds = Lists.newArrayList(주문_1_테이블.getId());

        // when & then
        assertThrows(CreateTableGroupException.class, () -> groupingEventHandler.handle(new TableGroupingEvent(단체_1, orderTableIds)));
    }

    @DisplayName("단체에 속하는 주문 테이블이 빈 테이블이 아니면 단체로 지정할 수 없다.")
    @Test
    void create05() {
        // given
        List<Long> orderTableIds = Lists.newArrayList(주문_테이블_10명.getId(), 주문_1_테이블.getId());

        // when & then
        assertThrows(CreateTableGroupException.class, () -> groupingEventHandler.handle(new TableGroupingEvent(단체_1, orderTableIds)));
    }

    @DisplayName("단체에 속하는 주문 테이블이 이미 테이블 그룹에 속해있으면 단체로 지정할 수 없다.")
    @Test
    void create06() {
        // given
        주문_1_테이블.mappedByTableGroup(단체_1.getId());
        List<Long> orderTableIds = Lists.newArrayList(주문_1_테이블.getId(), 주문_2_테이블.getId());

        // when & then
        assertThrows(CreateTableGroupException.class, () -> groupingEventHandler.handle(new TableGroupingEvent(단체_1, orderTableIds)));
    }

    @DisplayName("단체를 해제할 수 있다.")
    @Test
    void change01() {
        // given
        주문_1_테이블.mappedByTableGroup(단체_1.getId());
        주문_2_테이블.mappedByTableGroup(단체_1.getId());

        // when
        ungroupEventHandler.handle(new TableUngroupEvent(단체_1));

        // then
        assertAll(
                () -> assertThat(주문_1_테이블.getTableGroupId()).isNull(),
                () -> assertThat(주문_2_테이블.getTableGroupId()).isNull()
        );
    }
}
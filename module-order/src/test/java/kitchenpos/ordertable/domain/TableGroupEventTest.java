package kitchenpos.ordertable.domain;


import static org.mockito.Mockito.times;

import java.util.Arrays;
import javax.persistence.EntityManager;
import kitchenpos.ordertable.domain.event.TableGroupingEventListener;
import kitchenpos.ordertable.domain.event.TableUnGroupingEventListener;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.domain.TableGroupValidator;
import kitchenpos.tablegroup.domain.event.TableGroupingEvent;
import kitchenpos.tablegroup.domain.event.TableUnGroupingEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class TableGroupEventTest {

    @MockBean
    private TableGroupValidator tableGroupValidator;
    @MockBean
    private TableGroupingEventListener tableGroupingEventListener;
    @MockBean
    private TableUnGroupingEventListener tableUnGroupingEventListener;

    @Autowired
    TableGroupRepository tableGroupRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("테이블 그룹 맵핑 이벤트 호출 검증")
    void 테이블_그룹핑_이벤트_호출() {
        // given
        TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.of());
        savedTableGroup.group(tableGroupValidator, Arrays.asList(1L, 2L));

        // when
        tableGroupRepository.save(savedTableGroup);
        em.flush();

        // then
        Mockito.verify(tableGroupingEventListener, times(1))
            .onApplicationEvent(ArgumentMatchers.any(TableGroupingEvent.class));
    }


    @Test
    @DisplayName("테이블 그룹 해제 이벤트 호출 검증")
    void 테이블_그룹_해제_이벤트_호출() {
        // given
        TableGroup savedTableGroup = tableGroupRepository.save(TableGroup.of());
        savedTableGroup.ungroup(tableGroupValidator);

        // when
        tableGroupRepository.save(savedTableGroup);
        em.flush();

        // then
        Mockito.verify(tableUnGroupingEventListener, times(1))
            .onApplicationEvent(ArgumentMatchers.any(TableUnGroupingEvent.class));
    }
}

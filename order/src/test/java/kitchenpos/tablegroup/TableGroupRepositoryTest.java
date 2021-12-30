package kitchenpos.tablegroup;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TableGroupRepositoryTest {

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @DisplayName("주문 테이블 단체 지정하기")
    @Test
    void groupingTable() {

        //given
        TableGroup tableGroup = TableGroup.setUp();

        //when
        TableGroup actualTableGroup = tableGroupRepository.save(tableGroup);

        //then
        assertThat(actualTableGroup.getId()).isGreaterThan(0L);
        assertThat(actualTableGroup.getCreatedDate()).isNotNull();
    }

    @DisplayName("단체 지정 조회하기")
    @Test
    void findById() {

        //given
        OrderTable orderTableA = OrderTable.setting(3, true);
        OrderTable orderTableB = OrderTable.setting(5, true);
        orderTableRepository.save(orderTableA);
        orderTableRepository.save(orderTableB);

        TableGroup tableGroup = TableGroup.setUp();
        TableGroup actualTableGroup = tableGroupRepository.save(tableGroup);
        orderTableA.grouping(actualTableGroup.getId());
        orderTableB.grouping(actualTableGroup.getId());

        //when
        TableGroup findTableGroup = tableGroupRepository.findById(actualTableGroup.getId())
                .orElseThrow(() -> new IllegalArgumentException("단체 지정된 그룹을 찾을 수 없습니다."));

        List<OrderTable> orderTables = orderTableRepository.findByTableGroupId(findTableGroup.getId());

        //then
        assertThat(findTableGroup).isNotNull();
        Assertions.assertThat(orderTables).extracting(OrderTable::getNumberOfGuests).contains(3, 5);
    }
    
}

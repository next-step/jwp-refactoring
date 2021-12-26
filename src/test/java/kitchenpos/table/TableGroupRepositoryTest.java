package kitchenpos.table;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
        TableGroup tableGroup = TableGroup.create();

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
        OrderTable orderTableA = OrderTable.create(3, true);
        OrderTable orderTableB = OrderTable.create(5, true);

        TableGroup tableGroup = TableGroup.create();
        tableGroup.addOrderTable(orderTableA);
        tableGroup.addOrderTable(orderTableB);
        TableGroup actualTableGroup = tableGroupRepository.save(tableGroup);

        //when
        TableGroup findTableGroup = tableGroupRepository.findById(actualTableGroup.getId())
                .orElseThrow(() -> new IllegalArgumentException("단체 지정된 그룹을 찾을 수 없습니다."));

        //then
        assertThat(findTableGroup).isNotNull();
        assertThat(findTableGroup.findOrderTables()).extracting(OrderTable::getNumberOfGuests).contains(3, 5);
    }
    
}

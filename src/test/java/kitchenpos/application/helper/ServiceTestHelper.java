package kitchenpos.application.helper;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import kitchenpos.application.TableGroupService;
import kitchenpos.application.TableService;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.OrderTableFixtureFactory;
import kitchenpos.fixture.TableGroupFixtureFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceTestHelper {
    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;

    public TableGroup 테이블그룹_지정됨(int numberOfTables){
        List<OrderTable> orderTables = IntStream.range(0,numberOfTables)
                .mapToObj((index)-> 빈테이블_생성됨()).collect(toList());
        return tableGroupService.create(TableGroupFixtureFactory.createTableGroup(orderTables));
    }

    public TableGroup 테이블그룹_지정됨(OrderTable... orderTables){
        return tableGroupService.create(TableGroupFixtureFactory.createTableGroup(Arrays.asList(orderTables)));
    }

    public OrderTable 빈테이블_생성됨() {
        return tableService.create(OrderTableFixtureFactory.createEmptyOrderTable());
    }
    public OrderTable 비어있지않은테이블_생성됨(int numberOfGuests) {
        return tableService.create(OrderTableFixtureFactory.createNotEmptyOrderTable(numberOfGuests));
    }
}

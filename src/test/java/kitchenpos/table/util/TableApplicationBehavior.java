package kitchenpos.table.util;

import static java.util.stream.Collectors.toList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import kitchenpos.fixture.OrderTableFixtureFactory;
import kitchenpos.fixture.TableGroupFixtureFactory;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.application.TableService;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TableApplicationBehavior {
    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupService tableGroupService;


    public TableGroupResponse 테이블그룹_지정됨(int numberOfTables) {
        List<OrderTableResponse> orderTables = IntStream.range(0, numberOfTables)
                .mapToObj((index) -> 빈테이블_생성됨()).collect(toList());
        return tableGroupService.create(TableGroupFixtureFactory.createTableGroup(orderTables));
    }

    public TableGroupResponse 테이블그룹_지정됨(OrderTableResponse... orderTables) {
        return tableGroupService.create(TableGroupFixtureFactory.createTableGroup(Arrays.asList(orderTables)));
    }

    public OrderTableResponse 빈테이블_생성됨() {
        return tableService.create(OrderTableFixtureFactory.createEmptyOrderTable());
    }

    public OrderTableResponse 비어있지않은테이블로_변경(Long orderTableId) {
        return tableService.changeEmpty(orderTableId, OrderTableFixtureFactory.createParamForChangeEmptyState(false));
    }

    public OrderTableResponse 비어있지않은테이블_생성됨(int numberOfGuests) {
        return tableService.create(OrderTableFixtureFactory.createNotEmptyOrderTable(numberOfGuests));
    }

    public OrderTableResponse 빈테이블로_변경(Long orderTableId) {
        return tableService.changeEmpty(orderTableId, OrderTableFixtureFactory.createParamForChangeEmptyState(true));
    }

    public OrderTableResponse 테이블_인원수_변경(Long orderTableId, int updatedNumberOfGuests) {
        return tableService.changeNumberOfGuests(orderTableId,
                OrderTableFixtureFactory.createParamForChangeNumberOfGuests(updatedNumberOfGuests));
    }
}

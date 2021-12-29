package kitchenpos.tablegroup.application;

import kitchenpos.table.application.TableValidator;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.exception.IllegalOrderTableException;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.exception.NotSupportUngroupException;
import org.springframework.stereotype.Component;

import java.util.List;

import static kitchenpos.tablegroup.domain.TableGroup.MIN_NUMBER_TABLES;

/**
 * packageName : kitchenpos.tablegroup.application
 * fileName : TableGroupValidator
 * author : haedoang
 * date : 2021/12/27
 * description :
 */
@Component
public class TableGroupValidator {
    private final OrderTableRepository orderTableRepository;
    private final TableValidator tableValidator;

    public TableGroupValidator(OrderTableRepository orderTableRepository, TableValidator tableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.tableValidator = tableValidator;
    }

    public void validateGroup(List<Long> tableIds) {
        List<OrderTable> orderTables = getTables(tableIds);

        if (orderTables.isEmpty() || orderTables.size() < MIN_NUMBER_TABLES) {
            throw new IllegalOrderTableException("테이블의 개수가 작습니다.");
        }
        if (!allEmpty(orderTables)) {
            throw new IllegalOrderTableException("테이블 상태가 빈 테이블이 아닙니다.");
        }
        if (!ungrouped(orderTables)) {
            throw new IllegalOrderTableException("그룹된 테이블이 존재합니다.");
        }
    }

    private boolean ungrouped(List<OrderTable> orderTables) {
        return orderTables.stream().noneMatch(OrderTable::isGrouped);
    }

    public void validateUngroup(TableGroup tableGroup) {
        List<OrderTable> orderTables = getTables(tableGroup);

        if (existUsingTable(orderTables)) {
            throw new NotSupportUngroupException();
        }
    }

    private boolean existUsingTable(List<OrderTable> orderTables) {
        return orderTables.stream().anyMatch(tableValidator::usingTable);
    }

    private boolean allEmpty(List<OrderTable> orderTables) {
        return orderTables.stream()
                .allMatch(OrderTable::isEmpty);
    }

    public List<OrderTable> getTables(List<Long> tableIds) {
        return orderTableRepository.findAllById(tableIds);
    }

    public List<OrderTable> getTables(TableGroup tableGroup) {
        return orderTableRepository.findAllByTableGroupId(tableGroup.getId());
    }
}

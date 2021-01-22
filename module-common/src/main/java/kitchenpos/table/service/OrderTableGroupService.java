package kitchenpos.table.service;

import kitchenpos.domain.OrderTable;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.domain.TableGroup;
import kitchenpos.table.domain.TableValidator;
import kitchenpos.table.dto.OrderTableIdRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrderTableGroupService {
    private final OrderTableRepository orderTableRepository;
    private final OrderTableService orderTableService;
    private final TableValidator tableValidator;

    public OrderTableGroupService(OrderTableRepository orderTableRepository,
                                  OrderTableService orderTableService,
                                  TableValidator tableValidator) {
        this.orderTableRepository = orderTableRepository;
        this.orderTableService = orderTableService;
        this.tableValidator = tableValidator;
    }

    public List<OrderTableResponse> saveAll(TableGroup tableGroup, List<OrderTableIdRequest> orderTables) {
        return addTableGroupAll(tableGroup, orderTables);
    }

    private List<OrderTableResponse> addTableGroupAll(TableGroup tableGroup, List<OrderTableIdRequest> orderTables) {
        return orderTables
                .stream()
                .map(OrderTableIdRequest::getId)
                .map(orderTableService::findById)
                .map(table -> {
                    checkAddTableValidation(table);
                    table.saveTableGroup(tableGroup);
                    return OrderTableResponse.of(table);
                })
                .collect(Collectors.toList());
    }

    private void checkAddTableValidation(OrderTable table) {
        if (!table.isEmpty()) {
            throw new IllegalArgumentException("테이블이 비어있지 않습니다.");
        }

        if (table.hasTableGroup()) {
            throw new IllegalArgumentException("테이블에 단체가 지정 되어 있습니다.");
        }
    }

    public void ungroup(TableGroup groupById) {
        orderTableRepository.findAllByTableGroup(groupById)
                .forEach(table -> {
                    tableValidator.isPossibleUnGroup(table.getId());
                    table.removeGroupTable();
                });
    }

}

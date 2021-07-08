package kitchenpos.application;

import kitchenpos.domain.order.OrderTable;
import kitchenpos.exception.InvalidEntityException;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static kitchenpos.domain.order.TableGroup.EMPTY;

@Service
public class OrderTableService {

    private final OrderTableRepository orderTableRepository;


    public OrderTableService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTable getOrderTable(long id) {
        return orderTableRepository.findById(id).orElseThrow(() -> new InvalidEntityException("Not found OrderTable Id" + id));
    }

    public List<OrderTable> getAllOrderTablesByIds(List<Long> orderTableIds) {
        return orderTableRepository.findAllByIdIn(orderTableIds);
    }

    public List<OrderTable> getAllOrderTablesByGroupId(long tableGroupId) {
        return orderTableRepository.findAllByTableGroupId(tableGroupId);
    }

    public void makeTableGroupEmpty(OrderTable orderTable){
        orderTable.changeTableGroup(EMPTY);
        orderTableRepository.save(orderTable);
    }
}

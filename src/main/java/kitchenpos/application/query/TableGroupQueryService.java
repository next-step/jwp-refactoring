package kitchenpos.application.query;

import kitchenpos.dto.response.TableGroupViewResponse;
import kitchenpos.exception.EntityNotExistsException;
import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupQueryService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupQueryService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupViewResponse findById(Long id) {
        return TableGroupViewResponse.of(tableGroupRepository.findById(id)
                .orElseThrow(EntityNotExistsException::new));
    }
}

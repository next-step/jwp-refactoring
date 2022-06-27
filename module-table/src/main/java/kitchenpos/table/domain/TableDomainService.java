package kitchenpos.table.domain;

import kitchenpos.core.domain.DomainService;
import kitchenpos.table.exception.NotFoundOrderTableException;

@DomainService
public class TableDomainService {
    private final OrderTableRepository orderTableRepository;

    public TableDomainService(OrderTableRepository orderTableRepository) {
        this.orderTableRepository = orderTableRepository;
    }

    public OrderTable findById(Long id) {
        return orderTableRepository.findById(id)
                                   .orElseThrow(NotFoundOrderTableException::new);
    }
}

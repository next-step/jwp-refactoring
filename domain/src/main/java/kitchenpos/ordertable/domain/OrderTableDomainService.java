package kitchenpos.ordertable.domain;

public interface OrderTableDomainService {

    OrderTable findAvailableTableForOrder(Long id);
}

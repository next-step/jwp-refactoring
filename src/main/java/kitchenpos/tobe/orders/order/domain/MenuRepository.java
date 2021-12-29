package kitchenpos.tobe.orders.order.domain;

import java.util.List;

public interface MenuRepository {

    boolean existAll(final List<Long> menuIds);
}

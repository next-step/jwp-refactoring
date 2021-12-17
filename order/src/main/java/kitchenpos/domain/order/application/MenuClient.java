package kitchenpos.domain.order.application;

import java.util.List;

public interface MenuClient {

    void validateMenuExist(List<Long> menuIds);
}

package kitchenpos.dao;

import java.util.List;

public interface MenuDao {
    long countByIdIn(List<Long> ids);
}

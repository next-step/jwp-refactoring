package kitchenpos.menu.domain;

import java.util.List;

public interface Products {
	boolean containsAll(List<Long> ids);

	Product findById(Long id);
}

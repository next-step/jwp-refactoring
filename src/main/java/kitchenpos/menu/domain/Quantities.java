package kitchenpos.menu.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import kitchenpos.common.domian.Quantity;
import kitchenpos.common.error.CustomException;
import kitchenpos.common.error.ErrorInfo;

public class Quantities {
    private final Map<Long, Quantity> quantities;

    private Quantities() {
        this.quantities = new HashMap<>();
    }

    public Quantities(Map<Long, Quantity> quantities, int requestSize) {
        checkRequestSize(quantities, requestSize);
        this.quantities = Collections.unmodifiableMap(new HashMap<>(quantities));
    }

    public Quantity getByProductId(Long id) {
        return quantities.get(id);
    }

    public int size() {
        return quantities.size();
    }

    private void checkRequestSize(Map<Long, Quantity> quantities, int requestSize) {
        if (quantities.size() != requestSize) {
            throw new CustomException(ErrorInfo.NOT_EQUAL_REQUEST_SIZE);
        }
    }
}

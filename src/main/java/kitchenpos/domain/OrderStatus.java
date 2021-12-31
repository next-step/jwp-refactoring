package kitchenpos.domain;

import kitchenpos.common.exceptions.EmptyOrderStatusException;
import kitchenpos.common.exceptions.NotOrderStatusCompleteException;
import kitchenpos.common.exceptions.NotOrderStatusException;
import org.apache.logging.log4j.util.Strings;

import java.util.Objects;

public enum OrderStatus {
    COOKING("cooking", "meal"),
    MEAL("meal", "completion"),
    COMPLETION("completion", Strings.EMPTY);

    private String statusName;
    private String possibleStatus;

    OrderStatus(final String statusName, final String possibleStatus) {
        this.statusName = statusName;
        this.possibleStatus = possibleStatus;
    }

    public void validateStatus(final OrderStatus orderStatus) {
        if (Objects.isNull(orderStatus)) {
            throw new EmptyOrderStatusException();
        }
        if (this.equals(OrderStatus.COMPLETION)) {
            throw new NotOrderStatusCompleteException();
        }
        if (!this.possibleStatus.equals(orderStatus.statusName)) {
            throw new NotOrderStatusException();
        }
    }
}

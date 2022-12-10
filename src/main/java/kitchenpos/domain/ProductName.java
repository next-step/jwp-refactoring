package kitchenpos.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.exception.ExceptionMessage;
import kitchenpos.exception.InvalidProductNameSizeException;
import org.springframework.util.StringUtils;

@Embeddable
public class ProductName {

    @Column(nullable = false)
    private String name;

    protected ProductName() {
    }

    private ProductName(String name) {
        this.name = name;
    }

    public static ProductName from(String name) {
        checkNotNull(name);
        return new ProductName(name);
    }

    private static void checkNotNull(String name) {
        if (StringUtils.hasText(name)) {
            return;
        }

        throw new InvalidProductNameSizeException(ExceptionMessage.INVALID_PRODUCT_NAME_SIZE);
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductName that = (ProductName) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

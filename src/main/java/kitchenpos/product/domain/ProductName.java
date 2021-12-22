package kitchenpos.product.domain;

import kitchenpos.product.exception.IllegalProductNameException;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProductName {
    @Column
    private String name;

    protected ProductName() {
    }

    private ProductName(String name) {
        validate(name);
        this.name = name;
    }

    private void validate(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalProductNameException("이름은 빈값일 수 없습니다.");
        }
    }

    public static ProductName of(String name) {
        return new ProductName(name);
    }

    public boolean matchName(String targetName) {
        return this.name.equals(targetName);
    }

    public String getName() {
        return name;
    }
}

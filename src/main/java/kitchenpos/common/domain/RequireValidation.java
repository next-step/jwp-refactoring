package kitchenpos.common.domain;

import org.springframework.util.Assert;

public final class RequireValidation<T> {

    private final T target;

    private RequireValidation(T target) {
        Assert.notNull(target, "검증할 객체는 필수입니다.");
        this.target = target;
    }

    public static <T> RequireValidation<T> from(T target) {
        return new RequireValidation<>(target);
    }

    public T get(Validator<T> validator) {
        Assert.notNull(validator, "검증자는 필수입니다");
        validator.validate(target);
        return target;
    }
}

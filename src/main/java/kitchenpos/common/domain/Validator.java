package kitchenpos.common.domain;

public interface Validator<T> {

    static <T> Validator<T> fake() {
        return target -> {
        };
    }

    void validate(T target);
}

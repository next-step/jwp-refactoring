package kitchenpos.tobe.common.domain;

@FunctionalInterface
public interface Validator<T> {

    void validate(T t);
}

package kichenpos.common.domain;

public interface Validator<T> {

    void validate(T target);
}

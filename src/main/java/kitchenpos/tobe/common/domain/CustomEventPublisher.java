package kitchenpos.tobe.common.domain;

@FunctionalInterface
public interface CustomEventPublisher<T> {

    void publish(final T t);
}

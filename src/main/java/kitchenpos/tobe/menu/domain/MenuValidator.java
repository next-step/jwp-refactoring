package kitchenpos.tobe.menu.domain;

@FunctionalInterface
public interface MenuValidator {

    void validate(final Menu menu);
}

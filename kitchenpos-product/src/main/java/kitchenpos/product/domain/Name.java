package kitchenpos.product.domain;

public interface Name {

    default void validLength(String name) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("이름은 한 글자 이상이어야 합니다");
        }
    }
}

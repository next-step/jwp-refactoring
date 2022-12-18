package kitchenpos.common;

public class NameFixture {

    public static final String MENU_GROUP_A_NAME = "A";
    public static final String MENU_PRODUCT_NAME_A = "A";
    public static final String MENU_NAME_A = "A";

    public static Name nameMenuGroupA() {
        return new Name(MENU_GROUP_A_NAME);
    }

    public static Name nameProductA() {
        return new Name(MENU_PRODUCT_NAME_A);
    }

    public static Name nameMenuA() {
        return new Name(MENU_NAME_A);
    }
}

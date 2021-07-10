package kitchenpos.common;

public enum Message {

    ERROR_MENUGROUP_NAME_REQUIRED("반드시 메뉴그룹의 이름을 입력해야 합니다.");

    private String message;

    Message(String message) {
        this.message = message;
    }

    public String showText() {
        return message;
    }
}

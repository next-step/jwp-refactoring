package kitchenpos.sns.domain;

import kitchenpos.sns.strategy.SenderStrategy;

public class SmsSender implements SenderStrategy {
    private String message;

    private SmsSender(String message) {
        this.message = message;
    }

    public static SmsSender from(String message) {
        return new SmsSender(message);
    }

    @Override
    public void send() {
        System.out.println(message);
    }
}

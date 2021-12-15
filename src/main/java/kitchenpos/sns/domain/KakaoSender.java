package kitchenpos.sns.domain;

import kitchenpos.sns.strategy.SenderStrategy;

public class KakaoSender implements SenderStrategy {

    private String message;

    private KakaoSender(String message) {
        this.message = message;
    }

    public static KakaoSender from(String message) {
        return new KakaoSender(message);
    }

    @Override
    public void send() {
        System.out.println(message);
    }
}

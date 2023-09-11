package authentication;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.ToStringBuilder;

public final class TokenBuilder {
    public static Token getToken() {
        return Token.builder()
                .username(System.getenv("USER_NAME"))
                .password(System.getenv("PASSWORD"))
                .build();
    }
}

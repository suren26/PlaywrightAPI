package authentication;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang.builder.ToStringBuilder;

@Builder
@Getter
public class Token {
    private String username;
    private String password;

    @Override
    public String toString()
    {
        return new ToStringBuilder(this)
                .append("\n username",username)
                .append("\n password",password)
                .toString();
    }
}

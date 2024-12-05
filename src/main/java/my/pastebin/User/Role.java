package my.pastebin.User;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Role {
    @JsonProperty("USER")
    USER,
    @JsonProperty("ADMIN")
    ADMIN
}

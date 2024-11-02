package my.pastebin.EventUserStatus;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Status {
    @JsonProperty("REGISTERED")
    REGISTERED,
    @JsonProperty("CONFIRMED")
    CONFIRMED,
    @JsonProperty("ATTENDED")
    ATTENDED,
}

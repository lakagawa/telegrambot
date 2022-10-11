package telegrambot.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Data
@Getter
@Setter
public class Joke {
    private boolean error;
    private String category;
    private String type;
    private String setup;
    private String delivery;
    private Map<String, Boolean> flags;
    private int id;
    private boolean safe;
    private String lang;
}

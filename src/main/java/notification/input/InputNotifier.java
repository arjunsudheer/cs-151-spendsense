package notification.input;

import java.util.Optional;
import javafx.util.Pair;

public interface InputNotifier {
    Optional<Pair<String, String>> pushInputPrompt(
            String title,
            String header,
            String field1Name,
            String field2Name
    );
}
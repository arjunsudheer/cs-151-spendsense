package notification.input;

import java.util.Optional;
import javafx.util.Pair;

// Interface for user input dialog notifications
public interface InputNotifier {
    Optional<Pair<String, String>> pushInputPrompt(
            String title,
            String header,
            String field1Name,
            String field2Name);
}
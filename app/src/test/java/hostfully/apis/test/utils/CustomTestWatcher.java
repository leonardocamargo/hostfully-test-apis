package hostfully.apis.test.utils;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import java.util.Optional;

public class CustomTestWatcher implements TestWatcher {

    @Override
    public void testSuccessful(ExtensionContext context) {
        System.out.println("Test " + context.getDisplayName() + " passed!");
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        System.out.println("Test " + context.getDisplayName() + " failed with: " + cause.getMessage());
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        System.out.println("Test " + context.getDisplayName() + " is disabled" +
                           (reason.isPresent() ? " with reason: " + reason.get() : ""));
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        System.out.println("Test " + context.getDisplayName() + " was aborted.");
    }
}

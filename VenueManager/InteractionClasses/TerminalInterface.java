package VenueManager.InteractionClasses;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TerminalInterface {
    private String uiHeader;
    private String uiHeaderOnExit;
    private String sep = "_".repeat(50) + "";

    private int currentMenuState = 0;

    public TerminalInterface(String uiHeader) {
        setHeaderText(uiHeader);
        this.uiHeaderOnExit = "" + uiHeader + "\n" + sep + "\n";
    }

    public void setHeaderText(String uiHeader) {
        this.uiHeader = "" + uiHeader + "\n" + sep + "\n";
    }

    public void render() {
        System.out.print("\n".repeat(50) + "\033[H\033[2J");
        System.out.flush();

        if (currentMenuState == -1) {
            System.out.println(uiHeaderOnExit);
            System.out.println("Exiting...");
        } else if (currentMenuState == 0) {
            System.out.println(uiHeaderOnExit);
            System.out.println("There are no options to show.");
            renderExitPrompt();
        } else {
            System.out.println(uiHeader);
        }
    }

    private void renderExitPrompt() {
        this.currentMenuState = 1;
        System.out.println("Press enter to exit.");
        try {
            System.in.read();
            this.currentMenuState = -1;
            this.render();
        } catch (Exception e) {
            // do nothing
        }
        this.currentMenuState = 0;
    }

    public String renderYNPrompt(String title, String description) {
        Boolean isAcceptedAnswer = true;
        do {
            this.currentMenuState = 1;
            this.render();
            System.out.println(title);
            System.out.println(description);
            System.out.print(this.sep + "\n\n" + "Press Y to confirm, N to cancel");
            if (isAcceptedAnswer == false) {
                System.out.print(" [Invalid input, please try again.]");
            }
            System.out.print(": ");
            try {
                int input = readChoiceInput();
                if (input == 'Y' || input == 'y') {
                    renderBlank();
                    this.currentMenuState = 0;
                    return "Y";
                } else if (input == 'N' || input == 'n') {
                    renderBlank();
                    this.currentMenuState = 0;
                    return "N";
                } else {
                    renderBlank();
                    isAcceptedAnswer = false;
                }
            } catch (Exception e) {
                isAcceptedAnswer = false;
            }
        } while (!isAcceptedAnswer);
        this.currentMenuState = 0;
        return "";
    }

    public Integer renderMultiChoicePrompt(String title, String description, String[] options) {
        Boolean isAcceptedAnswer = true;
        do {
            this.currentMenuState = 1;
            this.render();
            System.out.println(title);
            System.out.println(description + "\n\n0. Previous Menu");
            for (int i = 0; i < options.length; i++) {
                System.out.println((i + 1) + ". " + options[i]);
            }
            System.out.print(this.sep + "\n\n" + "Select an option");
            if (isAcceptedAnswer == false) {
                System.out.print(" [Invalid input, please try again.]");
            }
            System.out.print(": ");
            try {
                int input = readChoiceInput();
                if (input == '0') {
                    renderBlank();
                    this.currentMenuState = 0;
                    return 0;
                } else if (input >= '1' && input <= '0' + options.length) {
                    renderBlank();
                    this.currentMenuState = 0;
                    return input - '0';
                } else {
                    renderBlank();
                    isAcceptedAnswer = false;
                }
            } catch (Exception e) {
                isAcceptedAnswer = false;
            }
        } while (!isAcceptedAnswer);
        this.currentMenuState = 0;
        return 0;
    }

    public String renderTextPrompt(String title, String description) {
        return renderTextPrompt(title, description, "Enter text", false);
    }

    public String renderTextPrompt(String title, String description, String promptLabel, boolean allowEmpty) {
        String inputText = "";
        boolean isAcceptedAnswer = true;

        do {
            this.currentMenuState = 1;
            this.render();
            System.out.println(title);
            System.out.println(description);
            System.out.print(this.sep + "\n\n" + promptLabel);
            if (!isAcceptedAnswer) {
                System.out.print(" [Invalid input, please try again.]");
            }
            System.out.print(": ");

            try {
                inputText = readLineInput();
                if (!allowEmpty && inputText.trim().isEmpty()) {
                    isAcceptedAnswer = false;
                } else {
                    renderBlank();
                    this.currentMenuState = 0;
                    return inputText;
                }
            } catch (Exception e) {
                isAcceptedAnswer = false;
            }
        } while (!isAcceptedAnswer);

        this.currentMenuState = 0;
        return "";
    }

    public Integer renderDisplayPrompt(String title, String text) {
        boolean isAcceptedAnswer = true;

        do {
            this.currentMenuState = 1;
            this.render();
            System.out.println(title);
            System.out.println(text);
            System.out.print(this.sep + "\n\nPress 0 to return");
            if (!isAcceptedAnswer) {
                System.out.print(" [Invalid input, please try again.]");
            }
            System.out.print(": ");

            try {
                int input = readChoiceInput();
                if (input == '0') {
                    renderBlank();
                    this.currentMenuState = 0;
                    return 0;
                }
                renderBlank();
                isAcceptedAnswer = false;
            } catch (Exception e) {
                isAcceptedAnswer = false;
            }
        } while (!isAcceptedAnswer);

        this.currentMenuState = 0;
        return 0;
    }

    private int readChoiceInput() {
        String ttyConfig = null;
        try {
            ttyConfig = runShellCommand("stty -g < /dev/tty").trim();
            runShellCommand("stty -icanon -echo min 1 time 0 < /dev/tty");
            int input = System.in.read();

            // Keep prompt output tidy after non-canonical input.
            System.out.println();
            return input;
        } catch (Exception e) {
            return readChoiceInputFallback();
        } finally {
            try {
                if (ttyConfig != null && !ttyConfig.isEmpty()) {
                    runShellCommand("stty " + ttyConfig + " < /dev/tty");
                } else {
                    runShellCommand("stty sane < /dev/tty");
                }
            } catch (Exception ignored) {
                // do nothing
            }
        }
    }

    private int readChoiceInputFallback() {
        try {
            int input = System.in.read();
            if (System.in.available() > 0) {
                while (System.in.available() > 0) {
                    System.in.read();
                }
            }
            return input;
        } catch (Exception e) {
            return -1;
        }
    }

    private String readLineInput() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = reader.readLine();
        if (input == null) {
            return "";
        }
        return input;
    }

    private String runShellCommand(String command) throws Exception {
        Process process = Runtime.getRuntime().exec(new String[] { "/bin/sh", "-c", command });
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line);
        }
        process.waitFor();
        return output.toString();
    }

    private void renderBlank() {
        this.currentMenuState = 1;
        this.render();
        this.currentMenuState = 0;
    }

}

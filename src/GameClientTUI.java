import java.util.Scanner;

public class GameClientTUI {
    private GameClient client;
    private Scanner in;
    
    public GameClientTUI(GameClient client) {
        this.client = client;
        this.in = new Scanner(System.in);
    }

    public void start() throws ServerUnavailableException {
        boolean runs = true;
        while (runs) {
            showMessage("Enter command: ");
            String input = in.nextLine();
            try {
                handleUserInput(input);
            } catch (ExitProgram e) {
                runs = false;
                in.close();
                client.sendExit();
            }
        }
    }

    public void showMessage(String message) {
		System.out.println(message);
    }
    
    public void handleUserInput(String input) throws ExitProgram, ServerUnavailableException {
        client.sendMessage(input);
    } 
}

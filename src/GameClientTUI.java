import java.util.Scanner;

import exceptions.ExitProgram;
import exceptions.ServerUnavailableException;

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

    public int getInt(String question) {
		System.out.print(question);
		while(true){
			try {
				return Integer.parseInt(in.next());
			} catch(NumberFormatException ne) {
				System.out.print("That's not a valid number.\n"+question);
			}
		}
    }

    public String getString(String question) {
		System.out.print(question);
		return in.nextLine();
	}

    public void showMessage(String message) {
		System.out.println(message);
    }
    
    public void handleUserInput(String input) throws ExitProgram, ServerUnavailableException {
        client.sendMessage(input);
    } 
}

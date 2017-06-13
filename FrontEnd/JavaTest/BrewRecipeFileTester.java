import java.io.Console;
import de.mikrosikaru.brausteuerungapp.*;
/**
 *
 * @author andre
 */
public class BrewRecipeFileTester {

	public static void waitForEnter(String message, Object... args) {
		Console c = System.console();
		if (c != null) {
			// printf-like arguments
			if (message != null)
				c.format(message, args);
			c.format("\nPress ENTER to proceed.\n");
			c.readLine();
		}
	}

    public static void main(String[] args) {
	
		waitForEnter(null);
		
		if ( 1 != args.length ) return;
		
		System.out.println("Filename: " + args[0]);
		
		String fileName = args[0];
		SystemSettings settings = new SystemSettings();
		BrewRecipeFileXsud recipeFile = new BrewRecipeFileXsud();
		recipeFile.Load(settings,fileName);
		
	}
}

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * IOSMediaTransferPrune deletes original images and videos if an edited
 * version exists and cleans up .AAE files leftover from IPhone media transfers.
 *
 * @author Danny Nguyen
 * @version 2.0.1
 * @since 1.0
 */
public class IOSMediaTransferPrune {
  /**
   * Deletes original media if an edited version exists.
   *
   * @param args user provided arguments
   */
  public static void main(String[] args) {
    File[] directory = new File("").listFiles(); // INSERT THE DIRECTORY PATH TO BE PRUNED HERE
    try {
      Arrays.sort(directory);
      Set<String> editedIds = new HashSet<>();
      int filesDeleted = 0;
      for (int i = directory.length - 1; i > 0; i--) {
        File file = directory[i];
        String name = file.getName();
        if (name.endsWith(".AAE")) {
          file.delete();
          filesDeleted++;
        } else if (name.startsWith("IMG_E")) {
          editedIds.add(name.substring(5, name.indexOf(".")));
        } else if (name.startsWith("IMG_")) {
          if (editedIds.contains(name.substring(4, name.indexOf(".")))) {
            file.delete();
            filesDeleted++;
          }
        }
      }
      System.out.println("Directory pruned of (" + filesDeleted + ") files.");
    } catch (NullPointerException e) {
      System.out.println("No files in directory.");
    }
  }
}

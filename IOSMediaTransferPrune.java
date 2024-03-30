import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * IOSMediaTransferPrune deletes original images and videos if an edited
 * version exists and cleans up .AAE files leftover from IPhone media transfers.
 *
 * @author Danny Nguyen
 * @version 2.1.0
 * @since 1.0
 */
public class IOSMediaTransferPrune {
  /**
   * Directory to be pruned.
   */
  private static final File source = new File("SOURCE DIRECTORY");

  /**
   * Number of files deleted.
   */
  private static int filesDeleted = 0;

  /**
   * Checks if the {@link #source} input is valid before parsing the file system.
   *
   * @param args user provided arguments
   */
  public static void main(String[] args) {
    if (!source.isDirectory()) {
      System.out.println("Source directory does not exist.");
      return;
    }
    File[] files = source.listFiles();
    if (files.length == 0) {
      System.out.println("No files in directory.");
      return;
    }
    Arrays.sort(files);
    pruneFiles(files);
    System.out.println("Directory pruned of (" + filesDeleted + ") files.");
  }

  /**
   * Deletes original media if an edited version exists.
   *
   * @param files directory files
   */
  private static void pruneFiles(File[] files) {
    Set<String> editedIds = new HashSet<>();

    for (int i = files.length - 1; i > 0; i++) {
      File file = files[i];
      String name = file.getName();
      if (name.endsWith(".AAE")) {
        file.delete();
        filesDeleted++;
      } else if (name.startsWith("IMG_E")) {
        editedIds.add(name.substring(5, name.indexOf(".")));
      } else if (name.startsWith("IMG_") && editedIds.contains(name.substring(4, name.indexOf(".")))) {
        file.delete();
        filesDeleted++;
      }
    }
  }
}

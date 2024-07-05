import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Deletes original media if an edited version exists and 
 * removes leftover .AAE files from iPhone media transfers.
 *
 * @author Danny Nguyen
 * @version 2.3.0
 * @since 1.0
 */
public class PruneIOSMediaTransfer {
  /**
   * Directory to be pruned.
   */
  private static final File source = new File("SOURCE DIRECTORY");

  /**
   * Checks if the {@link #source} input is valid before traversing the file system.
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
      System.out.println("Source directory is empty.");
      return;
    }

    long start = System.currentTimeMillis();
    Arrays.sort(files);
    int filesDeleted = pruneFiles(files);

    long end = System.currentTimeMillis();
    System.out.println("Pruned " + filesDeleted + " files in " + millisecondsToMinutesSeconds(end - start) + ".");
  }

  /**
   * Deletes original media if an edited version exists.
   *
   * @param files directory files
   * @return number of files deleted
   */
  private static int pruneFiles(File[] files) {
    Set<String> editedIds = new HashSet<>();
    int filesDeleted = 0;

    for (int i = files.length - 1; i > 0; i--) {
      File file = files[i];
      if (!file.isFile()) {
        continue;
      }

      String name = file.getName();
      if (name.endsWith(".AAE")) {
        System.out.println("Deleted file: " + file.getPath().substring(source.getPath().length()));
        file.delete();
        filesDeleted++;
      } else if (name.startsWith("IMG_E")) {
        editedIds.add(name.substring(5, name.indexOf(".")));
      } else if (name.startsWith("IMG_") && editedIds.contains(name.substring(4, name.indexOf(".")))) {
        System.out.println("Deleted file: " + file.getPath().substring(source.getPath().length()));
        file.delete();
        filesDeleted++;
      }
    }
    return filesDeleted;
  }

  /**
   * Converts milliseconds to minutes and seconds.
   *
   * @param duration elapsed time in milliseconds
   * @return minutes and seconds
   */
  private static String millisecondsToMinutesSeconds(long duration) {
    long minutes = duration / 60000L % 60;
    long seconds = duration / 1000L % 60;
    return ((minutes == 0 ? "" : minutes + "m ") + (seconds == 0 ? "0s" : seconds + "s ")).trim();
  }
}

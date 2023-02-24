import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * IOSImageTransferPrune deletes original images if an edited version
 * exists and cleans up .AAE files leftover from IPhone image transfers.
 *
 * @author Danny Nguyen
 * @version 1.3
 * @since 1.0
 */
public class IOSImageTransferPrune {
  private static ArrayList<File> originalImageFiles = new ArrayList<>();
  private static HashSet<String> originalImageIDs = new HashSet<>();
  private static HashSet<String> editedImageIDs = new HashSet<>();
  private static int filesDeleted = 0;

  public static void main(String[] args) {
    File directoryPath = new File(""); // INSERT THE DIRECTORY PATH TO BE PRUNED HERE
    categorizeImageFiles(directoryPath);
    originalImageIDs.retainAll(editedImageIDs);
    deleteMatchedOriginalImageIDs();
    System.out.println("Directory pruned of (" + filesDeleted + ") files.");
  }

  /**
   * Classifies which files in the directoryPath are images, then tracks
   * their IDs for later comparison between original and edited images.
   * Any .AAE files found are deleted.
   *
   * @param directoryPath the directory containing all the files to be examined
   * @throws Exception
   */
  private static void categorizeImageFiles(File directoryPath) {
    try {
      for (File file : directoryPath.listFiles()) {
        String fileName = file.getName();
        int fileLength = fileName.length();

        // Skip file names too short to be classified
        if (fileLength < 4) {
          continue;
        }

        // Delete .AAE files
        if (fileName.startsWith(".AAE", fileLength - 4)) {
          file.delete();
          filesDeleted++;
        }

        boolean isImageFile = fileName.startsWith("IMG_");
        boolean isOriginalImage = !fileName.contains("E");

        if (isImageFile) {
          if (isOriginalImage) {
            originalImageFiles.add(file);
            originalImageIDs.add(fileName.substring(4, 8));
          } else {
            editedImageIDs.add(fileName.substring(5, 9));
          }
        }
      }
    } catch (NullPointerException e) {
      System.out.println("There are no files within this directory path.");
    } catch (Exception e) {
      System.out.println("An unknown error occurred while parsing the directory.");
    }
  }

  /**
   * Deletes all original image files whose
   * ID matches with edited image IDs.
   */
  private static void deleteMatchedOriginalImageIDs() {
    for (File file : originalImageFiles) {
      if (originalImageIDs.contains(file.getName().substring(4, 8))) {
        file.delete();
        filesDeleted++;
      }
    }
  }
}

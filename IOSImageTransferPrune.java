import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

public class IOSImageTransferPrune {
  public static void main(String[] args) {
    File directoryPath = new File(""); // INSERT THE DIRECTORY PATH TO BE PRUNED HERE

    try {
      ArrayList<File> images = new ArrayList<File>();
      HashSet<String> originalImageIDs = new HashSet<String>();
      HashSet<String> editedImageIDs = new HashSet<String>();
      int filesDeleted = 0;

      for (File file : directoryPath.listFiles()) {
        String fileName = file.getName();
        int fileLength = fileName.length();

        // Skip too short file names
        if (fileLength < 4) {
          continue;
        }

        // Delete .AAE files or sort image IDs
        if (fileName.substring(fileLength - 4, fileLength).equals(".AAE")) {
          file.delete();
          filesDeleted++;
        } else if (fileName.substring(0, 4).equals("IMG_")) {
          if (!fileName.contains("E")) { // Original
            images.add(file);
            originalImageIDs.add(fileName.substring(4, 8));
          } else { // Edited
            editedImageIDs.add(fileName.substring(5, 9));
          }
        }
      }

      // Only keep original image IDs that match with an edited image ID
      originalImageIDs.retainAll(editedImageIDs);

      // Delete matched original images
      for (File file : images) {
        if (originalImageIDs.contains(file.getName().substring(4, 8))) {
          file.delete();
          filesDeleted++;
        }
      }

      System.out.println("Directory pruned of (" + filesDeleted + ") files.");
      
    } catch (NullPointerException e) {
      System.out.println("There are no files within this directory path.");
    } catch (Exception e) {
      System.out.println("An error occurred while parsing the directory.");
    }
  }
}

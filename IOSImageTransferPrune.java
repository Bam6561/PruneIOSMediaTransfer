import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

public class IOSImageTransferPrune {
  public static void main(String[] args) {
    File directoryPath = new File(""); // INSERT THE DIRECTORY PATH TO BE PRUNED HERE

    File[] directoryFiles = directoryPath.listFiles();

    if (directoryFiles == null) {
      System.out.println("There are no files within this directory path.");
    } else {
      try {
        ArrayList<File> imageFiles = new ArrayList<File>();
        HashSet<String> originalImages = new HashSet<String>();
        HashSet<String> editedImages = new HashSet<String>();
        int pruneCounter = 0;

        for (File file : directoryFiles) {
          String fileName = file.getName();
          int fileLength = fileName.length();

          if (fileLength >= 4) {
            // Delete edit configuration files
            if (fileName.substring(fileLength - 4, fileLength).equals(".AAE")) {
              file.delete();
              pruneCounter++;
            }
            // Classify image files
            if (fileName.substring(0, 4).equals("IMG_")) {
              if (!fileName.contains("E")) { // Original
                imageFiles.add(file);
                originalImages.add(fileName.substring(4, 8));
              } else { // Edited
                editedImages.add(fileName.substring(5, 9));
              }
            }
          }
        }

        // Check for intersections
        originalImages.retainAll(editedImages);

        // Delete original image if an edited version of it exists
        for (File file : imageFiles) {
          if (originalImages.contains(file.getName().substring(4, 8))) {
            file.delete();
            pruneCounter++;
          }
        }
        System.out.println("Directory pruned of (" +pruneCounter +") files.");
      } catch (Exception e) {
        System.out.println("An error occurred while parsing the directory.");
      }
    }
  }
}
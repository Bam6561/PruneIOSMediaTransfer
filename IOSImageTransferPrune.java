import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

public class IOSImageTransferPrune {
  public static void main(String[] args) {
    File directoryPath = new File(""); // INSERT DIRECTORY PATH TO BE PRUNED HERE
    File[] directoryFiles = directoryPath.listFiles();
    if (directoryFiles == null) {
      System.out.println("There are no files within this directory path.");
    } else {
      try { 
        ArrayList<File> imageFiles = new ArrayList<File>();
        HashSet<String> originalImages = new HashSet<String>();
        HashSet<String> editedImages = new HashSet<String>();
        int pruneCounter = 0;
        for (File file : directoryFiles) { // Parse directory
          String fileName = file.getName();
          int fileLength = fileName.length();
          if (fileLength >= 4) {
            if (fileName.substring(fileLength - 4, fileLength).equals(".AAE")) { // Delete edit configuration files
              file.delete();
              pruneCounter++;
            }
            if (fileName.substring(0, 4).equals("IMG_")) { // Identify image files
              if (!fileName.contains("E")) { // Original
                imageFiles.add(file); // Track image for later processing
                originalImages.add(fileName.substring(4, 8));
              } else { // Edited
                editedImages.add(fileName.substring(5, 9));
              }
            }
          }
        }
        originalImages.retainAll(editedImages); // Check for intersection
        for (File file : imageFiles) { // Parse images tracked
          if (originalImages.contains(file.getName().substring(4, 8))) { // Delete original image if an edited version exists
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
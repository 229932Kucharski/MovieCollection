package manager;

import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ImageManager {

    /**
     * Read file .jpg from file and convert it to byte array
     * @return image in byte array
     */
    public static byte[] chooseCoverFromFile() throws IOException {
        byte[] cover;
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Image Files", "jpg", "jpeg");
        fileChooser.setSelectedExtensionFilter(filter);
        fileChooser.setTitle("Choose a cover (.jpg)");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        File file = fileChooser.showOpenDialog(null);
        if (file == null) {
            return null;
        }
        cover = ImageManager.imageToByteArray(file.getAbsolutePath());
        byteArrayToImage(0, cover);

        return cover;
    }

    /**
     * Convert image to byte array
     * @param movieId movie id that you want to get image
     * @return image in byte array
     */
    public static byte[] imageToByteArray(int movieId) throws IOException {
        File file = new File("src/main/assets/img/movieCover/" + movieId + ".jpg");
        BufferedImage bImage= ImageIO.read(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "jpg", bos);
        return bos.toByteArray();
    }

    /**
     * Convert image to byte array
     * @param path path to image
     * @return image in byte array
     */
    public static byte[] imageToByteArray(String path) throws IOException {
        File file = new File(path);
        BufferedImage bImage= ImageIO.read(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "jpg", bos);
        return bos.toByteArray();
    }

    /**
     * Convert byte array to iamge
     * @param movieId id of movie that images belong to
     * @param data image in byte array
     */
    public static void byteArrayToImage(int movieId, byte[] data) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        BufferedImage bImage = ImageIO.read(bis);
        File file = new File("src/main/assets/img/movieCover/" + movieId + ".jpg");
        if(!file.exists()) {
            file.mkdirs();
        }
        ImageIO.write(bImage, "jpg", new File("src/main/assets/img/movieCover/" + movieId + ".jpg"));
    }

}

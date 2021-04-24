package model.movie;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

public class ImageConverter {

    public static byte[] imageToByteArray(int movieId) throws IOException {

        File file = new File("src/main/resources/img/movieCover/" + movieId + ".jpg");
        BufferedImage bImage= ImageIO.read(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "jpg", bos);
        return bos.toByteArray();
    }

    public static byte[] imageToByteArray(String path) throws IOException {

        File file = new File(path);
        BufferedImage bImage= ImageIO.read(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bImage, "jpg", bos);
        return bos.toByteArray();
    }

    public static void byteArrayToImage(int movieId, byte[] data) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        BufferedImage bImage = ImageIO.read(bis);
        ImageIO.write(bImage, "jpg", new File("src/main/resources/img/movieCover/" + movieId + ".jpg"));
    }

}

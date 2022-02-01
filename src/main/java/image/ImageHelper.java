package image;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import javax.imageio.ImageIO;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 图片助手
 *
 * @author zhaowang
 */
public class ImageHelper {

    /**
     * 图片下载
     *
     * @param sourceImageUrl    下载的图片的 URL
     * @param desImageDirectory 图片下载后存储的目录
     */
    public static void download(String sourceImageUrl, String desImageDirectory) {
        try (/* 输入流 */ DataInputStream inputStream = new DataInputStream(new URL(sourceImageUrl).openStream());
                /* 输出流 */ FileOutputStream outputStream = new FileOutputStream(new File(desImageDirectory + "1.jpg"));
                /* 输出缓冲流 */ ByteArrayOutputStream outputBufferStream = new ByteArrayOutputStream()
        ) {
            // 将输入数据写入输出缓冲数据
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputBufferStream.write(buffer, 0, length);
            }
            // 将输出缓冲数据写入输出流
            outputStream.write(outputBufferStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("download fail, e", e);
        }
    }

    /**
     * 图片旋转90度，270度
     *
     * @param sourceImage 原图片
     * @param desImage    旋转后的图片
     */
    public static void rotate(String sourceImage, String desImage) {
        try {
            // 读取源文件
            BufferedImage src = ImageIO.read(new File(sourceImage));
            // 旋转90度
            Rectangle rectangle = new Rectangle(new Dimension(src.getHeight(), src.getWidth()));
            BufferedImage des = new BufferedImage(rectangle.width, rectangle.height, BufferedImage.TYPE_INT_BGR);
            Graphics2D graphics = des.createGraphics();
            graphics.translate((rectangle.width - src.getWidth()) / 2, (rectangle.height - src.getHeight()) / 2);
            graphics.rotate(Math.toRadians(90), src.getWidth() / 2, src.getHeight() / 2);
            graphics.drawImage(src, null, null);
            // 输出新文件
            ImageIO.write(des, "jpg", new File(desImage));
        } catch (IOException e) {
            throw new RuntimeException("rotate fail, e", e);
        }
    }

    /**
     * 多张图片转成一个pdf
     *
     * @param sourceImageDirectory 多张图片所在的目录
     * @param desPdf               生成的pdf
     */
    public static void toPdf(String sourceImageDirectory, String desPdf) {
        // 创建 Document
        Document document = new Document();
        try {
            document.setMargins(0, 0, 0, 0);
            // 创建 PdfWriter
            PdfWriter.getInstance(document, new FileOutputStream(new File(desPdf)));
            // 打开 Document
            document.open();

            File directory = new File(sourceImageDirectory);
            String[] fileArray = directory.list();
            List<String> files = Arrays.stream(fileArray).filter(x -> x.toLowerCase().endsWith(".jpg")).collect(Collectors.toList());
            for (String file : files) {
                // 创建 Image
                Image image = Image.getInstance(sourceImageDirectory + file);
                image.setAlignment(Image.ALIGN_CENTER);
                // 设置页面大小
                document.setPageSize(new com.itextpdf.text.Rectangle(image.getWidth(), image.getHeight()));
                document.newPage();
                // 将图片放到文件中（多张图片可以放到一个pdf中）
                document.add(image);
            }
        } catch (Exception e) {
            throw new RuntimeException("toPdf fail, e", e);
        } finally {
            // 关闭文档
            document.close();
        }
    }

    public static void main(String[] args) {
        String devDirectory = "/Users/zhaowang/Desktop/dev/";
        // 测试 downloadImage
//        download("https://t7.baidu.com/it/u=4162611394,4275913936&fm=193&f=GIF", devDirectory);
        // 测试旋转
//        rotate(devDirectory + "1.jpg", devDirectory + "1-r.jpg");
        // 测试图片生成 PDF
        toPdf(devDirectory, devDirectory + "1.pdf");
    }
}

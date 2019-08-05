package com.zhatianbang.utils;

import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;


public class PDFToImageutil {

    private static Logger logger = LoggerFactory.getLogger(PDFToImageutil.class);

    /**
     * 功能描述:
     * <pdf文件转成png图片>
     * @param dirPath 1 pdf文件路径
     * @param exportPath 2 图片输出路径
     * @param imageFormat 3 转换的图片格式(png jpg)
     * @return : void
     * @author : zm
     * @date : 2019/8/5
     */
    public static void pdfToImage(String dirPath, String exportPath,String imageFormat) {
        File PDFfile = new File(dirPath);
        File ImageFile = new File(exportPath);
        if(!ImageFile.exists()){
            ImageFile.mkdirs();//生成Image文件夹
        }
        if (PDFfile.exists()) {
            File[] files = PDFfile.listFiles();
            if (files.length == 0) {
                logger.info("---------------pdfToImage------文件夹是空的!-----");
                return;
            } else {
                for (File file2 : files) {
                    logger.info("---------pdfToImage-------文件:" + file2.getAbsolutePath());
                    String filePath = file2.getAbsolutePath();
                    Document document = new Document();
                    try {
                        document.setFile(filePath);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    // 缩放比例
                    float scale = 2.0f;
                    // 旋转角度
                    float rotation = 0f;
                    for (int i = 0; i < document.getNumberOfPages(); i++) {
                        try {
                            BufferedImage image = (BufferedImage)
                            document.getPageImage(i, GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, rotation, scale);
                            RenderedImage rendImage = image;
                            // pdf文件名
                            String oldName = file2.getName();
                            // png图片文件名
                            String filename = oldName;
                            String imageUrl = exportPath+"/"+filename+ i + "."+imageFormat;
                            String pdfUrl = dirPath+"/"+oldName;
                            logger.info("-------图片路径是-------："+ imageUrl);
                            logger.info("-------pdf路径是-------："+ pdfUrl);
                            File file3 = new File(imageUrl);
                            ImageIO.write(rendImage, imageFormat, file3);
                            String localFile = imageUrl;
                            File file4= new File(localFile);
                            if (!file4.isFile()) {
                                throw new Exception(file4+" 不是图片文件!");
                            }
                            image.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    document.dispose();
                }
            }
        } else {
            logger.info("--------------pdfToImage------文件不存在!");
        }
    }

    public static void main(String[] args) {
        String dirPath = "D:/Application/pdfToImage/渤海银行-众车贷-24期";
        String exportPath = "D:/Application/pdfToImage/cbhbank";
        pdfToImage(dirPath,exportPath,"jpg");
    }
}

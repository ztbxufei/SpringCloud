package com.zhatianbang.utils.picture;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.zhatianbang.utils.baidu.GpsToBaidu;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.zhatianbang.utils.baidu.BaiduApiUtil.getGeocoderAddress;

/**
 * 读取照片信息
 *
 * @author: xufei
 * @date: 2021/7/8
 */
@Slf4j
public class ReadPictureInfo {

    /**
     * 读取照片文件信息
     *
     * @param picFilePath
     * @return
     */
    public static Map<String,Object> readPictureInfo(String picFilePath){
        Map<String,Object> resultMap = new HashMap<>();
        Tag tag;
        File picFile = new File(picFilePath);
        Metadata metadata;
        try {
            metadata = JpegMetadataReader.readMetadata(picFile);
            for (Directory exif : metadata.getDirectories()) {
                for (Tag value : exif.getTags()) {
                    tag = value;
                    log.info(tag.getTagName() + "--" + tag.getDescription());
                    resultMap.put(tag.getTagName(),tag.getDescription());
                }
            }
        } catch (JpegProcessingException | IOException e) {
            e.printStackTrace();
        }
        return resultMap;
    }


    public static void main(String[] args) {
        //传入照片的绝对路径
        Map<String, Object> pictureInfo = readPictureInfo("/Users/xufei/Pictures/WechatIMG5.jpeg");
        String latitudeStr = null;
        String longitudeStr = null;
        String make = null;
        String model = null;
        String dateStr = null;
        for (String key : pictureInfo.keySet()) {
            if ("GPS Latitude".equals(key)){
                latitudeStr = pictureInfo.get(key).toString();
            }else if ("GPS Longitude".equals(key)){
                longitudeStr = pictureInfo.get(key).toString();
            }else if ("Make".equals(key)){
                make = pictureInfo.get(key).toString();
            }else if ("Model".equals(key)){
                model = pictureInfo.get(key).toString();
            }else if ("Date/Time".equals(key)){
                dateStr = pictureInfo.get(key).toString();
            }
        }
        String address = null;
        if (StringUtils.isNotBlank(latitudeStr) && StringUtils.isNotBlank(longitudeStr)){
            double[] doubles = GpsToBaidu.latitude_and_longitude_convert_to_decimal_system(longitudeStr, latitudeStr);
            log.info("longitude={},latitude={}",doubles[1],doubles[0]);
            double[] gcj2bd = GpsToBaidu.wgs2bd(doubles[0], doubles[1]);
            address = getGeocoderAddress(gcj2bd[1], gcj2bd[0]);
            log.info("address={}",address);
        }
        log.info("该照片于{}在{}处使用{}-{}拍摄",dateStr,address,make,model);
    }
}

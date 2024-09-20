package com.hoangphi.service.image;

import com.hoangphi.service.impl.images.item.GetMediasItem;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageServiceUtils {
    String getImage(String fileName);

//    GetMediasItem getMedias(String fileName, String pathName);
    List<String> uploadFiles(List<MultipartFile> files);
    void deleteImage(String fileName);

}

package com.hoangphi.service.image;

import com.hoangphi.service.impl.images.item.GetMediasItem;

public interface ImageService {
    byte[] getImage(String fileName);

    byte[] getImage(String fileName, String pathName);

    byte[] getImage(String fileName, String pathName, String defaultImageWhenWrong);

    GetMediasItem getMedias(String fileName, String pathName);


}

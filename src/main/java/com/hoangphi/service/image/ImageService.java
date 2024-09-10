package com.hoangphi.service.image;

public interface ImageService {
    byte[] getImage(String fileName);

    byte[] getImage(String fileName, String pathName);

    byte[] getImage(String fileName, String pathName, String defaultImageWhenWrong);
}

package com.hoangphi.service.impl.image;

import com.hoangphi.service.image.ImageService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class ImageUserServiceImpl implements ImageService {
    @Override
    public byte[] getImage(String fileName) {
        String filePath = "images/" + fileName;

        byte[] images;
        try {
            images = Files.readAllBytes(new File(filePath).toPath());
        } catch (IOException e) {
            try {
                images = Files.readAllBytes(new File("images/no-product-image.jpg").toPath());
            } catch (IOException e1) {
                System.out.println("Error in getImage" + e.getMessage());
                images = null;
            }
        }
        return images;
    }

    @Override
    public byte[] getImage(String fileName, String pathName) {
        String filePath = "images/" + pathName + "/" + fileName;

        byte[] images;
        try {
            images = Files.readAllBytes(new File(filePath).toPath());
        } catch (IOException e) {
            try {
                images = Files.readAllBytes(new File("images/no-product-image.jpg").toPath());
            } catch (IOException e1) {
                System.out.println("Error in getImage" + e.getMessage());
                images = null;
            }
        }
        return images;
    }

    @Override
    public byte[] getImage(String fileName, String pathName, String defaultImageWhenWrong) {
        String filePath = "images/" + pathName + "/" + fileName;

        byte[] images;
        try {
            images = Files.readAllBytes(new File(filePath).toPath());
        } catch (IOException e) {
            try {
                images = Files.readAllBytes(new File("images/" + defaultImageWhenWrong).toPath());
            } catch (IOException e1) {
                System.out.println("Error in getImage" + e.getMessage());
                images = null;
            }
        }
        return images;
    }
}

package com.hoangphi.service.impl.image;

import com.hoangphi.entity.social.Medias;
import com.hoangphi.repository.MediasRepository;
import com.hoangphi.service.image.ImageService;
import com.hoangphi.service.impl.images.item.GetMediasItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
public class ImageUserServiceImpl implements ImageService {
    private final MediasRepository mediasRepository;
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

    @Override
    public GetMediasItem getMedias(String fileName, String pathName) {
        Medias media = mediasRepository.findByName(fileName);

        GetMediasItem getMediasItem = GetMediasItem.builder()
                .data(this.getImage(fileName, pathName, "planhorder-image.png")).build();

        if (media != null) {
            File file = new File("images/medias/" + media.getName());
            String mimeType = URLConnection.guessContentTypeFromName(file.getName());

            getMediasItem.setContentType(mimeType);
            getMediasItem.setOriginaFile(file);
        }

        return getMediasItem;
    }
}

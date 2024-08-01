package com.hoangphi.utils;

import com.hoangphi.utils.parent.OptionCreateAndSaveFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

public class ImageUtils {
    public static String getExtensionFile(File file){
        String fileName=file.getName();
        int index = fileName.lastIndexOf(".");
        return fileName.substring(index+1);
    }

    public static String getExtensionFile(MultipartFile file){
        String fileName=file.getOriginalFilename()==null?file.getName():file.getOriginalFilename();
        if(fileName!=null){
            int index=fileName.lastIndexOf(".");
            if(index>0){
                return fileName.substring(index+1);
            }
        }
        throw new Error("Something went wrong when get name of file");
    }

    public static File createFileAndSave(String path, MultipartFile originalFile, OptionCreateAndSaveFile optionCreateAndSaveFile) {
        try {
            UUID uuid = UUID.randomUUID();
            OptionCreateAndSaveFile unwrapOptionCreateAndSaveFile=optionCreateAndSaveFile==null
                    ?new OptionCreateAndSaveFile():optionCreateAndSaveFile;
            String extension=unwrapOptionCreateAndSaveFile.getDefaultExtention();
            if(originalFile!=null){
                String nonValidateExtension=getExtensionFile(originalFile);
                if(ListUtils.includes(unwrapOptionCreateAndSaveFile.getAcceptExtentions(),nonValidateExtension)){
                    extension=nonValidateExtension;
                }
            }
            File resultFile=new File("images\\"+path+uuid.toString()+"."+extension);
            if(originalFile!=null){
                originalFile.transferTo(new File(resultFile.getAbsolutePath()));
            }
            return resultFile;

        }catch (Exception e){
            System.out.println("in images createFileAndSave untils" + e.getMessage());
            return null;
        }

    }
    public static void deleteImg(String fileName) {
        File imgFile = new File("images/" + fileName);
        if (imgFile.exists())
            imgFile.delete();
    }
}

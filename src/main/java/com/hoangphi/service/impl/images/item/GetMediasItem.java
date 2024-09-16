package com.hoangphi.service.impl.images.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetMediasItem {
    private byte[] data;
    private File originaFile;
    private String contentType;
}
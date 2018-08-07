package com.veeritsolutions.uhelpme.enums;

/**
 * Created by ${Hitesh} on 26-Apr-17.
 */

public enum ImageUpload {

    ClientProfile(1),
    ClientBanner(2),
    ClientPet(3),
    VetProfile(4),
    VetBanner(5),
    LegalDocument(6);

    int imageUpload;

    ImageUpload(int i) {
        imageUpload = i;
    }

    public int getImageUpload() {
        return imageUpload;
    }

    public void setImageUpload(int imageUpload) {
        this.imageUpload = imageUpload;
    }
}

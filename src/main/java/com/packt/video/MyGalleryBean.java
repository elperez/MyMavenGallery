/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.packt.video;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.ServletContext;

/** This class is responsible to sustain upload process and provide collection 
 * of photos that will be listed in the data table and the gallery.
 *
 * @author kit.perez
 */
@Named(value = "myGalleryBean")
@ViewScoped
public class MyGalleryBean implements Serializable {
    
    private List<MyPhoto> photos = new ArrayList<>();

    /**
     * Creates a new instance of MyGalleryBean
     */
    public MyGalleryBean() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        Path path = Paths.get(((ServletContext) externalContext.getContext())
                .getRealPath(File.separator + "resources" + File.separator + "photos"));
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(path)) {
            for (Path file: ds) {
                MyPhoto photo = new MyPhoto(file.getFileName().toString(), false);
                photos.add(photo);
            }
        } catch (IOException ex) {
            Logger.getLogger(MyGalleryBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<MyPhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<MyPhoto> photos) {
        this.photos = photos;
    }
    
    
    
}
